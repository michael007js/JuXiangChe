package com.sss.car.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.activity.BaseActivity;
import com.blankj.utilcode.constant.RequestModel;
import com.blankj.utilcode.customwidget.Dialog.YWLoadingDialog;
import com.blankj.utilcode.dao.LoadImageCallBack;
import com.blankj.utilcode.okhttp.callback.StringCallback;
import com.blankj.utilcode.util.APPOftenUtils;
import com.blankj.utilcode.util.BitmapUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sss.car.Config;
import com.sss.car.EventBusModel.OrderCommentListChanged;
import com.sss.car.MyApplication;
import com.sss.car.R;
import com.sss.car.RequestWeb;
import com.sss.car.custom.ListViewOrderComment;
import com.sss.car.model.OrderModel_goods_data;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;


/**
 * 评论交易订单(买家版)
 * Created by leilei on 2017/10/9.
 */

public class ActivityOrderCommentBuyer extends BaseActivity implements LoadImageCallBack, ListViewOrderComment.ListViewOrderCommentCallBack {
    @BindView(R.id.back_top)
    LinearLayout backTop;
    @BindView(R.id.title_top)
    TextView titleTop;
    @BindView(R.id.right_button_top)
    TextView rightButtonTop;
    @BindView(R.id.activity_order_comment)
    LinearLayout activityOrderComment;


    YWLoadingDialog ywLoadingDialog;
    List<OrderModel_goods_data> data;
    @BindView(R.id.content_activity_order_comment)
    ListViewOrderComment contentActivityOrderComment;


    @Override
    protected void TRIM_MEMORY_UI_HIDDEN() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        backTop = null;
        titleTop = null;
        rightButtonTop = null;
        activityOrderComment = null;
        if (data != null) {
            data.clear();
        }
        data = null;
        contentActivityOrderComment = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment_buyer);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtils.showShortToast(getBaseActivityContext(), "数据传输错误!");
            finish();
        }
        customInit(activityOrderComment, false, true, false);
        titleTop.setText("发表评价");
        rightButtonTop.setText("发布");
        rightButtonTop.setTextColor(getResources().getColor(R.color.mainColor));
        data = getIntent().getParcelableArrayListExtra("data");
        contentActivityOrderComment.setListViewOrderCommentCallBack(this);
        contentActivityOrderComment.setLoadImageCallBack(this);
        contentActivityOrderComment.setList(getBaseActivityContext(), data, getIntent().getExtras().getString("targetPic"));

    }

    @OnClick({R.id.back_top, R.id.right_button_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_top:
                finish();
                break;
            case R.id.right_button_top:
                publish();
                break;
        }
    }


    void publish() {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        JSONArray goods_id = new JSONArray();
        JSONArray contents = new JSONArray();
        JSONArray grade = new JSONArray();
        JSONArray picture = new JSONArray();
        if (contentActivityOrderComment.getData().size() == 0) {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "您没有待评论的商品");
            return;
        }
        boolean isReturn = false;
        for (int i = 0; i < contentActivityOrderComment.getData().size(); i++) {
            if (StringUtils.isEmpty(contentActivityOrderComment.getData().get(i).customGrade) || StringUtils.isEmpty(contentActivityOrderComment.getData().get(i).customContent)) {
                isReturn = true;
            }
            goods_id.put(contentActivityOrderComment.getData().get(i).goods_id);
            contents.put(contentActivityOrderComment.getData().get(i).customContent);
            grade.put(contentActivityOrderComment.getData().get(i).customGrade);
            for (int j = 0; j < contentActivityOrderComment.getData().get(i).photo.size(); j++) {
                if (!StringUtils.isEmpty(contentActivityOrderComment.getData().get(i).photo.get(j))) {
                    if (!"default".equals(contentActivityOrderComment.getData().get(i).photo.get(j))) {
                        picture.put(ConvertUtils.bitmapToBase64(BitmapUtils.decodeSampledBitmapFromFile(contentActivityOrderComment.getData().get(i).photo.get(j), 480, 960)));
                    }
                }
            }
        }
        if (!isReturn) {
            commentOrder(goods_id, contents, grade, picture);
        } else {
            if (ywLoadingDialog != null) {
                ywLoadingDialog.disMiss();
            }
            ToastUtils.showShortToast(getBaseActivityContext(), "请填写商品评论或对商品进行星级评价");
        }

    }

    /**
     * 订单评论
     */
    public void commentOrder(JSONArray goods_id, JSONArray contents, JSONArray grade, JSONArray picture) {
        if (ywLoadingDialog != null) {
            ywLoadingDialog.disMiss();
        }
        ywLoadingDialog = null;
        ywLoadingDialog = new YWLoadingDialog(getBaseActivityContext());
        ywLoadingDialog.show();

        try {
            addRequestCall(new RequestModel(System.currentTimeMillis() + "", RequestWeb.commentOrder(
                    new JSONObject()
                            .put("order_id", getIntent().getExtras().getString("targetOrderId"))
                            .put("member_id", Config.member_id)
                            .put("goods_id", goods_id)
                            .put("contents", contents)
                            .put("grade", grade)
                            .put("picture", picture)
                            .toString()//用户Id
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
                                    EventBus.getDefault().post(new OrderCommentListChanged(getIntent().getExtras().getString("targetOrderId")));
                                    finish();
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


    @Override
    public void onLoad(ImageView imageView) {
        addImageViewList(imageView);
    }

    @Override
    public void onClickImage(int i, int position, List<OrderModel_goods_data> data) {
        if (getBaseActivityContext() != null) {
            int a = position--;
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < data.get(position).photo.size(); j++) {
                if (!data.get(position).photo.get(j).equals("default")) {
                    temp.add(data.get(position).photo.get(j));
                }
            }
            startActivity(new Intent(getBaseActivityContext(), ActivityImages.class)
                    .putStringArrayListExtra("data", (ArrayList<String>) temp)
                    .putExtra("current", a));
        }
    }

    @Override
    public void onAddPhoto(final int i, int position, final List<OrderModel_goods_data> list) {
        APPOftenUtils.createPhotoChooseDialog(0, 9, weakReference, MyApplication.getFunctionConfig(), new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (resultList == null || resultList.size() == 0) {
                    return;
                }
                List<String> temp = list.get(i).photo;
                for (int i = 0; i < resultList.size(); i++) {
                    temp.add(resultList.get(i).getPhotoPath());
                }
                list.get(i).photo = temp;
                data = list;
                contentActivityOrderComment.setList(getBaseActivityContext(), data, getIntent().getExtras().getString("targetPic"));

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }
}
