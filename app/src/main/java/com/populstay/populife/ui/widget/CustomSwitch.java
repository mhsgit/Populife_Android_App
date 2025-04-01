package com.populstay.populife.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Switch;

public class CustomSwitch extends Switch {
    private static final int SLIDE_THRESHOLD = 10; // 滑动阈值，单位：像素
    private float startX, startY;

    public CustomSwitch(Context context) {
        super(context);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float dx = Math.abs(endX - startX);
                float dy = Math.abs(endY - startY);
                // 若移动距离超过阈值，视为滑动，消耗事件（阻止状态改变）
                if (dx > SLIDE_THRESHOLD || dy > SLIDE_THRESHOLD) {
                    return true; // 消耗事件，不执行滑动逻辑
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 点击事件正常交给父类处理
                break;
        }
        return super.onTouchEvent(event);
    }
}
