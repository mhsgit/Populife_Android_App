package com.populstay.populife.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.populstay.populife.R;
import com.populstay.populife.databinding.ViewInfoTooltipBinding;
import com.populstay.populife.databinding.ViewTooltipBinding;

public class InfoTooltipView extends LinearLayout {

    private ViewInfoTooltipBinding binding;
    private String tooltipText;

    public InfoTooltipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setClickable(true); // 防止事件穿透

        // 正确 inflate merge 布局
        binding = ViewInfoTooltipBinding.inflate(LayoutInflater.from(context), this);

        // 读取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InfoTooltipView);
        setTitleText(ta.getString(R.styleable.InfoTooltipView_titleText));
        tooltipText = ta.getString(R.styleable.InfoTooltipView_tooltipText);
        ta.recycle();

        binding.llContainer.setOnClickListener(v -> showTooltip(binding.tvTitle));

    }


    /**
     * 显示 Tooltip
     */
    private void showTooltip(View anchor) {
        ViewTooltipBinding tipBinding = ViewTooltipBinding.inflate(LayoutInflater.from(getContext()));
        tipBinding.tvTip.setText(tooltipText);

        PopupWindow popupWindow = new PopupWindow(
                tipBinding.getRoot(),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setElevation(8f);

        tipBinding.getRoot().measure(
                MeasureSpec.UNSPECIFIED,
                MeasureSpec.UNSPECIFIED
        );

        int xOffset = anchor.getWidth() / 2 - tipBinding.getRoot().getMeasuredWidth() / 2;
        int yOffset = -anchor.getHeight() - tipBinding.getRoot().getMeasuredHeight() - 12;

        popupWindow.showAsDropDown(anchor, xOffset, yOffset);
    }

    /**
     * 可动态更新标题或 tooltip
     */
    public void setTitleText(String title) {
        binding.tvTitle.setText(title);
    }

    public void setTooltipText(String tip) {
        tooltipText = tip;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return tooltipText;
    }

    public String getTitleText() {
        return binding.tvTitle.getText().toString();
    }
}