package com.populstay.populife.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.populstay.populife.R;

public class SmartToast {
    private static final int SHORT_DURATION = 2000; // 2秒
    private static final int LONG_DURATION = 3500;  // 3.5秒
    private static final int CHAR_PER_SECOND = 400; // 每秒阅读400字符（经验值）
    private static volatile Toast toast; // 静态持有当前 Toast，volatile 保证可见性



    public static void show(Activity activity, @StringRes int resId) {
        String text = activity.getString(resId); // 解析资源ID
        show(activity, text);
    }

    public static void show(Activity activity, @StringRes int resId, Object... args) {
        String text = activity.getString(resId, args); // 支持占位符（如 %s, %d）
        show(activity, text);
    }

    public static void show(Activity activity, String text) {
        activity.runOnUiThread(() -> {
            // 取消前一个 Toast
            cancelCurrentToast();
            Context appContext = activity.getApplicationContext();
            // 继承系统默认 Toast 布局参数
            toast = new Toast(appContext);

            // 加载自定义布局
            View view = LayoutInflater.from(appContext).inflate(R.layout.toast_custom,null,false);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setText(text);

            // 动态计算显示时长
            int duration = calculateDuration(text);

            // 设置属性
            toast.setView(view);
            toast.setDuration(duration > LONG_DURATION ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100); // 保持系统默认位置

            // 显示前强制测量布局宽度
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            // 限制最大宽度（避免超出屏幕）
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            if (view.getMeasuredWidth() > screenWidth * 0.9) {
                tvMessage.setMaxWidth((int) (screenWidth * 0.9));
            }

            // 显示前强制更新视图布局
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            toast.show();
        });
    }

    // 取消当前 Toast
    private static void cancelCurrentToast() {
        if (toast != null) {
            toast.cancel(); // 关键：立即从队列中移除
            toast = null;
        }
    }

    private static int calculateDuration(String text) {
        int baseDuration = text.length() > 50 ? LONG_DURATION : SHORT_DURATION;
        return Math.min(baseDuration + (text.length() * 1000 / CHAR_PER_SECOND), 5000); // 最长5秒
    }
}
