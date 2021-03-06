package com.sss.car.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.customwidget.JingDongCountDownView.SecondDownTimerView;
import com.blankj.utilcode.customwidget.JingDongCountDownView.base.OnCountDownTimerListener;
import com.blankj.utilcode.dao.OnAskDialogCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sss.car.EventBusModel.ChangedOrderModel;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderSellerDetails;
import com.sss.car.model.OrderSellerModel;
import com.sss.car.model.OrderSellerModel_Order_Goods;
import com.sss.car.order_new.Constant;
import com.sss.car.utils.OrderUtils;
import com.sss.car.view.ActivityShopInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的订单==>服务提示类
 * Created by leilei on 2017/9/29.
 */

@SuppressWarnings("ALL")
public class OrderGoodsOrderTip extends BaseActivity {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.people_name_order_goods_order_tip)
    TextView peopleNameOrderGoodsMyOrderBuyer;
    @BindView(R.id.mobile_name_order_goods_order_tip)
    TextView mobileNameOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_choose_car_order_goods_order_tip)
    LinearLayout clickChooseAddressOrderGoodsMyOrderBuyer;
    @BindView(R.id.list_order_goods_order_tip)
    ListViewOrderSellerDetails listOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_coupon_order_goods_order_tip)
    TextView showCouponOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_coupon_order_goods_order_tip)
    LinearLayout clickCouponOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_order_time_order_goods_order_tip)
    TextView showOrderTimeOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_order_time_order_service)
    LinearLayout clickOrderTimeOrderService;
    @BindView(R.id.show_penal_sum_order_goods_order_tip)
    TextView showPenalSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_penal_sum_order_goods_order_tip)
    LinearLayout clickPenalSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.show_other_order_goods_order_tip)
    TextView showOtherOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_other_sum_order_goods_order_tip)
    LinearLayout clickOtherSumOrderGoodsMyOrderBuyer;
    @BindView(R.id.order_goods_order_tip)
    LinearLayout OrderGoodsMyOrderBuyer;
    YWLoadingDialog ywLoadingDialog;
    OrderSellerModel orderSellerModel;


    @BindView(R.id.price_order_goods_order_tip)
    TextView priceOrderGoodsMyOrderBuyer;
    @BindView(R.id.click_s)
    TextView clicks;
    @BindView(R.id.content_order_goods_order_tip)
    TextView contentOrderGoodsMyOrderBuyer;
    @BindView(R.id.location_pic)
    SimpleDraweeView locationPic;
    @BindView(R.id.address_or_car)
    TextView addressOrCar;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.company)
    EditText company;
    @BindView(R.id.expressage_code)
    EditText expressageCode;
    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.countdown)
    SecondDownTimerView countdown;
    @BindView(R.id.parent_countdown)
    LinearLayout parentCountdown;
    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdown != null) {
            countdown.cancelDownTimer();
        }
        countdown = null;
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        peopleNameOrderGoodsMyOrderBuyer = null;
        mobileNameOrderGoodsMyOrderBuyer = null;
        clickChooseAddressOrderGoodsMyOrderBuyer = null;
        listOrderGoodsMyOrderBuyer = null;
        showCouponOrderGoodsMyOrderBuyer = null;
        clickCouponOrderGoodsMyOrderBuyer = null;
        showOrderTimeOrderGoodsMyOrderBuyer = null;
        clickOrderTimeOrderService = null;
        showPenalSumOrderGoodsMyOrderBuyer = null;
        clickPenalSumOrderGoodsMyOrderBuyer = null;
        showOtherOrderGoodsMyOrderBuyer = null;
        clickOtherSumOrderGoodsMyOrderBuyer = null;
        OrderGoodsMyOrderBuyer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.order_goods_order_tip);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传递错误");
            finish();
        }
        ButterKnife.bind(this);
        customInit(OrderGoodsMyOrderBuyer, false, true, false);
        listOrderGoodsMyOrderBuyer.setListViewOrderSellerDetailsCallBack(new ListViewOrderSellerDetails.ListViewOrderSellerDetailsCallBack() {
            @Override
            public void onClickShopName(String shop_id) {
                if (getBaseActivityContext() != null) {
                    startActivity(new Intent(getBaseActivityContext(), ActivityShopInfo.class)
                            .putExtra("shop_id", shop_id));
                }
            }
        });

        initView();
        countdown.setDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ToastUtils.showShortToast(getBaseActivityContext(), "该订单已经过期");
                EventBus.getDefault().post(new ChangedOrderModel());
                finish();
            }
        });

        getOrderDetailsSeller();
    }

    void initView() {
        if (getIntent().getExtras().getBoolean("grayness")) {
            clicks.setBackgroundColor(getResources().getColor(R.color.grayness));
        }
        titleTop.setText("实物订单详情");
        clicks.setText(getIntent().getExtras().getString("buttonTitle"));
        if (getIntent().getExtras().getBoolean("hideButton")) {
            company.setVisibility(View.GONE);
            expressageCode.setVisibility(View.GONE);
            clicks.setVisibility(View.GONE);
            parent.setVisibility(View.GONE);
        }
    }


    void showCompany() {
        LogUtils.e(getIntent().getExtras().getInt("status"));
        switch (getIntent().getExtras().getInt("status")) {
            case Constant.Have_Already_Delivery_Awating_Sign_For:
                parent.setVisibility(View.VISIBLE);
                company.setText(orderSellerModel.express);
                expressageCode.setText(orderSellerModel.waybill);
                break;
        }
    }

    @OnClick({R.id.back_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back_top:
                finish();
                break;

        }
    }


    /**
     * 获取订单详情
     */
    public void getOrderDetailsSeller() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();
        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.getOrderDetailsSeller_detail(
                    new JSONObject()
                            .put("order_id", getIntent().getExtras().getString("order_id"))
                            .toString()
                    , new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            ToastUtils.showShortToast(getBaseActivityContext(), e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (ywLoadingDialog != null) {
                                ywLoadingDialog.disMiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    orderSellerModel = new OrderSellerModel();
                                    orderSellerModel = new OrderSellerModel();
                                    orderSellerModel.order_id = jsonObject.getJSONObject("data").getString("order_id");
                                    orderSellerModel.mobile = jsonObject.getJSONObject("data").getString("mobile");
                                    orderSellerModel.recipients = jsonObject.getJSONObject("data").getString("recipients");
                                    orderSellerModel.address = jsonObject.getJSONObject("data").getString("address");
                                    orderSellerModel.vehicle_name = jsonObject.getJSONObject("data").getString("vehicle_name");
                                    orderSellerModel.remark = jsonObject.getJSONObject("data").getString("remark");
                                    orderSellerModel.order_code = jsonObject.getJSONObject("data").getString("order_code");
                                    orderSellerModel.delivery_time = jsonObject.getJSONObject("data").getString("delivery_time");
                                    orderSellerModel.damages = jsonObject.getJSONObject("data").getString("damages");
                                    orderSellerModel.total = jsonObject.getJSONObject("data").getString("total");
                                    orderSellerModel.deduct_price = jsonObject.getJSONObject("data").getString("deduct_price");
                                    orderSellerModel.coupon_price = jsonObject.getJSONObject("data").getString("coupon_price");
                                    orderSellerModel.number = jsonObject.getJSONObject("data").getString("number");
                                    orderSellerModel.integral = jsonObject.getJSONObject("data").getString("integral");
                                    orderSellerModel.coupon_id = jsonObject.getJSONObject("data").getString("coupon_id");
                                    orderSellerModel.create_time = jsonObject.getJSONObject("data").getString("create_time");
                                    orderSellerModel.shop_id = jsonObject.getJSONObject("data").getString("shop_id");
                                    orderSellerModel.member_id = jsonObject.getJSONObject("data").getString("member_id");
                                    orderSellerModel.mode_payment = jsonObject.getJSONObject("data").getString("mode_payment");
                                    orderSellerModel.type = jsonObject.getJSONObject("data").getString("type");
                                    orderSellerModel.status = jsonObject.getJSONObject("data").getString("status");
                                    orderSellerModel.shop_name = jsonObject.getJSONObject("data").getString("shop_name");
                                    orderSellerModel.shop_logo = jsonObject.getJSONObject("data").getString("shop_logo");
                                    orderSellerModel.start_time = jsonObject.getJSONObject("data").getString("start_time");
                                    orderSellerModel.express = jsonObject.getJSONObject("data").getString("express");
                                    orderSellerModel.waybill = jsonObject.getJSONObject("data").getString("waybill");
                                    orderSellerModel.coupon_name = jsonObject.getJSONObject("data").getString("coupon_name");
                                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("goods_data");
                                    List<OrderSellerModel_Order_Goods> list = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        OrderSellerModel_Order_Goods orderSellerModel_order_goods = new OrderSellerModel_Order_Goods();
                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                        orderSellerModel_order_goods.price = jsonArray.getJSONObject(i).getString("price");
                                        orderSellerModel_order_goods.number = jsonArray.getJSONObject(i).getString("number");
                                        orderSellerModel_order_goods.goods_id = jsonArray.getJSONObject(i).getString("goods_id");
                                        orderSellerModel_order_goods.title = jsonArray.getJSONObject(i).getString("title");
                                        list.add(orderSellerModel_order_goods);
                                    }
                                    orderSellerModel.goods_data = list;
                                    int start_time = jsonObject.getJSONObject("data").getInt("start_time");
                                    if (start_time > 0) {
                                        countdown.setDownTime(Long.valueOf(start_time*1000));
                                        countdown.startDownTimer();
                                        parentCountdown.setVisibility(View.VISIBLE);
                                    }
                                    showData();

                                } else {
                                    ToastUtils.showShortToast(getBaseActivityContext(), jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
                                e.printStackTrace();
                            }
                        }
                    })));
        } catch (JSONException e) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据解析错误Err:order-0");
            e.printStackTrace();
        }
    }


    void showData() {
        peopleNameOrderGoodsMyOrderBuyer.setText(orderSellerModel.recipients);
        contentOrderGoodsMyOrderBuyer.setText(orderSellerModel.address);
        mobileNameOrderGoodsMyOrderBuyer.setText(orderSellerModel.mobile);
        priceOrderGoodsMyOrderBuyer.setText(orderSellerModel.total);
        showCouponOrderGoodsMyOrderBuyer.setText(orderSellerModel.coupon_name);
        showOrderTimeOrderGoodsMyOrderBuyer.setText(orderSellerModel.delivery_time);
        showPenalSumOrderGoodsMyOrderBuyer.setText(orderSellerModel.damages);
        showOtherOrderGoodsMyOrderBuyer.setText(orderSellerModel.remark);
        listOrderGoodsMyOrderBuyer.setData(getBaseActivityContext(), orderSellerModel);

        showCompany();
    }

}
