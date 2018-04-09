package com.blankj.utilcode.AdressDataChoose;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.AdressDataChoose.wheelview.OnItemSelectedListener;
import com.blankj.utilcode.AdressDataChoose.wheelview.WheelView;
import com.blankj.utilcode.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描    述：
 * 创建日期：2017/7/20 14:26
 * 作    者：Chengfu
 * 邮    箱：
 * 备    注：
 */
public class DatePicker extends Dialog implements View.OnClickListener {

    private static int MIN_YEAR = 1900;
    private static int MAX_YEAR = 2200;
    private View view;
    private WheelView mYearWheelView;
    private WheelView mMonthWheelView;
    private WheelView mDayWheelView;
    private TextView mTvConfirm;
    private TextView mTvCancel;
    private OnDateCListener mOnDateCListener;

    private List<String> years = new ArrayList<>();
    private List<String> months = new ArrayList<>();
    private List<String> days = new ArrayList<>();

    private int yearPos;
    private int monthPos;
    private int dayPos;


    public void clear() {
        view = null;
        mYearWheelView = null;
        mMonthWheelView = null;
        mDayWheelView = null;
        mTvConfirm = null;
        mTvCancel = null;
        mOnDateCListener = null;
        if (years != null) {
            years.clear();
        }
        years = null;

        if (months != null) {
            months.clear();
        }
        months = null;

        if (days != null) {
            days.clear();
        }
        days = null;
    }


    public DatePicker(Context context) {
        super(context, R.style.transparentWindowStyle);

        view = View.inflate(context, R.layout.layout_address_picker, null);

        initView();
        initData();
        setListener();

        this.setContentView(view);

        this.setCanceledOnTouchOutside(true);

        //从底部弹出
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.windowAnimationStyle);  //添加动画

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    public static void setMinYear(int minYear) {
        MIN_YEAR = minYear;
    }

    public static void setMaxYear(int maxYear) {
        MAX_YEAR = maxYear;
    }

    /**
     * 回调接口
     */
    public interface OnDateCListener {
        void onDateSelected(String year, String month, String day);
    }

    public void setDateListener(OnDateCListener mOnDateCListener) {
        this.mOnDateCListener = mOnDateCListener;
    }

    private void initView() {
        mYearWheelView = (WheelView) view.findViewById(R.id.wv_province);
        mMonthWheelView = (WheelView) view.findViewById(R.id.wv_city);
        mDayWheelView = (WheelView) view.findViewById(R.id.wv_district);
        mTvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        // 设置可见条目数量
        mYearWheelView.setVisibleItemCount(9);
        mMonthWheelView.setVisibleItemCount(9);
        mDayWheelView.setVisibleItemCount(9);

        mYearWheelView.setLabel("年");
        mMonthWheelView.setLabel("月");
        mDayWheelView.setLabel("日");

        mYearWheelView.isCenterLabel(true);
        mMonthWheelView.isCenterLabel(true);
        mDayWheelView.isCenterLabel(true);
    }

    private void setListener() {
        //年份*****************************
        mYearWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                yearPos = index;
                //设置日
                setDay();
            }
        });
        //月份********************
        mMonthWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                monthPos = index;
                //设置日
                setDay();
            }
        });
        //日********************
        mDayWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                dayPos = index;
            }
        });

        mTvConfirm.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    private void initData() {

        Calendar nowCalendar = Calendar.getInstance();
        yearPos = nowCalendar.get(Calendar.YEAR) - MIN_YEAR;
        monthPos = nowCalendar.get(Calendar.MONTH);
        dayPos = nowCalendar.get(Calendar.DAY_OF_MONTH) - 1;

        //初始化年
        for (int i = 0; i <= MAX_YEAR - MIN_YEAR; i++) {
            years.add(format(MIN_YEAR + i));
        }
        mYearWheelView.setItems(years);
        mYearWheelView.setCurrentItem(yearPos);
        //初始化月
        for (int i = 0; i < 12; i++) {
            months.add(format(i + 1));
        }
        mMonthWheelView.setItems(months);
        mMonthWheelView.setCurrentItem(monthPos);

        //设置日
        setDay();
    }

    /**
     * 设置日
     */
    private void setDay() {
        if (years != null&&months!=null&&days!=null) {
            boolean isRun = isRunNian(Integer.parseInt(years.get(yearPos)));
            int dayCount = 0;
            switch (Integer.parseInt(months.get(monthPos))) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    dayCount = 31;
                    break;
                case 2:
                    if (isRun) {
                        dayCount = 29;
                    } else {
                        dayCount = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    dayCount = 30;
                    break;
            }
            days.clear();
            for (int i = 0; i < dayCount; i++) {
                days.add(format(i + 1));
            }

            mDayWheelView.setItems(days);
            dayPos = dayPos >= days.size() - 1 ? days.size() - 1 : dayPos;
            mDayWheelView.setCurrentItem(dayPos);
        }


    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_confirm) {
            if (mOnDateCListener != null) {
                int currentItem = mDayWheelView.getCurrentItem();
                currentItem = currentItem >= days.size() - 1 ? days.size() - 1 : currentItem;
                mOnDateCListener.onDateSelected(years.get(mYearWheelView.getCurrentItem()), months.get(mMonthWheelView.getCurrentItem()), days.get(currentItem));
            }
        }
        cancel();
    }

    /**
     * 判断是否是闰年
     *
     * @param year
     * @return
     */
    private boolean isRunNian(int year) {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    private String format(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }
}
