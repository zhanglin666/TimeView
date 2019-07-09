package timeview.yxt.com.timeview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeView extends View {
    private Paint mPaint;
    private Paint mPaintSelect;
    //年
    private int year;
    //月
    private int month;
    //天
    private int day;
    //周
    private int week;
    //时
    private int hour;
    //分
    private int branch;
    //秒
    private int second;
    //当前有多少天
    private int dayCount;

    //秒针转动度数
    private float secondDegrees;
    //分针转动度数
    private float branchDegrees;
    //月份的转动度数
    private float monthDegrees;
    //天的转动度数
    private float dayDegrees;
    //小时的转动度数
    private float hourDegrees;
    //周的转动度数
    private float weekDegrees;
    private int width;
    private int height;

    private float yuan = 0f;
    //计算月份前进的系数
    private int monthX = 0;
    private String[] countTag = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五"
            , "十六", "十七", "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十"
            , "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九", "四十", "四十一", "四十二", "四十三", "四十四", "四十五"
            , "四十六", "四十七", "四十八", "四十九", "五十", "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九", "零"};

    public TimeView(Context context) {
        this(context, null);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
//        DisplayMetrics metric = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
//        width = metric.widthPixels; // 屏幕宽度（像素）
//        height = metric.heightPixels; // 屏幕高度（像素）


        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(20f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaintSelect = new Paint();
        mPaintSelect.setColor(Color.RED);
        mPaintSelect.setAntiAlias(true);
        mPaintSelect.setTextSize(20f);
        mPaintSelect.setTextAlign(Paint.Align.CENTER);

        year = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("yyyy")));
        month = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("MM")));
        day = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("dd")));
        hour = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("HH")));
        branch = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("mm")));
        second = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("ss")));
        week = TimeUtils.getWeek();
        Log.e("11111", week + "    " + month);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 0);
        dayCount = cal.get(Calendar.DAY_OF_MONTH);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(20);
//                        rot++;
                        if (yuan != 360f) {
                            yuan++;
                        } else {
                            year = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("yyyy")));
                            month = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("MM")));
                            day = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("dd")));
                            hour = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("HH")));
                            branch = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("mm")));
                            second = Integer.valueOf(TimeUtils.getCurTimeString(new SimpleDateFormat("ss")));
                        }
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

//
//    // 这里不用管测量模式是什么，因为咱们有屏幕短边保底，只取其中一个小值即可。测量宽高和屏幕短边作对比，返回最小值
//    private int measureSize(int measureSpec) {
//        int size = MeasureSpec.getSize(measureSpec);
//        width = Math.min(width, size);
//        return width;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
//        canvas.save();
//        canvas.rotate(90, 460, 300);
        canvas.save();
        if (monthDegrees > -(360f / 12) * (month - 1)) {
            monthDegrees--;
        } else {
            monthDegrees = -(360f / 12) * (month - 1);
        }
        canvas.rotate(monthDegrees, 530, 900);
        onDrawContent(canvas, "月", 12, 620, 900, month - 1);
        canvas.restore();

        canvas.save();
        if (dayDegrees > -(360f / dayCount) * (day - 1)) {
            dayDegrees--;
        } else {
            dayDegrees = -(360f / dayCount) * (day - 1);
        }
        canvas.rotate(dayDegrees, 530, 900);
        onDrawContent(canvas, "日", dayCount, 690, 900, day - 1);
        canvas.restore();


        canvas.save();
        if (weekDegrees > -(360f / 7) * (week - 1)) {
            weekDegrees--;
        } else {
            weekDegrees = -(360f / 7) * (week - 1);
        }
        canvas.rotate(weekDegrees, 530, 900);
        onDrawContent(canvas, "星期", 7, 750, 900, week - 1);
        canvas.restore();

        canvas.save();
        if (hourDegrees > -(360f / 24) * (hour - 1)) {
            hourDegrees--;
        } else {
            hourDegrees = -(360f / 24) * (hour - 1);
        }
        canvas.rotate(hourDegrees, 530, 900);
        onDrawContent(canvas, "时", 24, 830, 900, hour - 1);
        canvas.restore();


        canvas.save();
        if (branchDegrees > -(360f / 60) * (branch - 1)) {
            branchDegrees--;
        } else {
            branchDegrees = -(360f / 60) * (branch - 1);
        }
        canvas.rotate(branchDegrees, 530, 900);
        onDrawContent(canvas, "分", 60, 915, 900, branch - 1);
        canvas.restore();


        canvas.save();
        if (secondDegrees > -(360f / 60) * (second - 1)) {
            secondDegrees--;
        } else {
            secondDegrees = -(360f / 60) * (second - 1);
        }
        canvas.rotate(secondDegrees, 530, 900);
        onDrawContent(canvas, "秒", 60, 1000, 900, second - 1);
        canvas.restore();
        canvas.drawText("二零一九年", 530, 900, mPaintSelect);
    }

    //X为控件距离原点的间距
    private void onDrawContent(Canvas canvas, String Company, int count, int x, int y, int Selection) {
        canvas.save();
        for (int i = 0; i < count; i++) {
            String text = countTag[i] + Company;
            if (i == Selection) {
                canvas.drawText(text, x, y, mPaintSelect);
            } else {
                canvas.drawText(text, x, y, mPaint);
            }
            canvas.rotate(yuan / count, 530, 900);
        }
        canvas.restore();
    }
}
