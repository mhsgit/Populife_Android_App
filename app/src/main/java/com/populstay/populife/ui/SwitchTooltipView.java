package com.populstay.populife.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.SwitchCompat;

import com.populstay.populife.R;
import com.populstay.populife.databinding.ViewSwitchTooltipBinding;

public class SwitchTooltipView extends ConstraintLayout {

    private ViewSwitchTooltipBinding binding;

    public SwitchTooltipView(Context context) {
        this(context, null);
    }

    public SwitchTooltipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchTooltipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = ViewSwitchTooltipBinding.inflate(LayoutInflater.from(context), this, true);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchTooltipView);

            String title = ta.getString(R.styleable.SwitchTooltipView_titleText);
            String tooltip = ta.getString(R.styleable.SwitchTooltipView_tooltipText);
            boolean checked = ta.getBoolean(R.styleable.SwitchTooltipView_checked, false);

            ta.recycle();

            if (title != null) setTitleText(title);
            if (tooltip != null) setTooltipText(tooltip);
            setChecked(checked);
        }

        // 预览模式显示默认值
        if (isInEditMode()) {
            if (binding.itvTooltip.getTitleText() == null) {
                binding.itvTooltip.setTitleText("远程解锁");
            }
            if (binding.itvTooltip.getTooltipText() == null) {
                binding.itvTooltip.setTooltipText("允许远程解锁");
            }
            setChecked(true);
        }
    }

    /** 设置标题 */
    public void setTitleText(String text) {
        binding.itvTooltip.setTitleText(text);
    }

    /** 设置 tooltip 文本 */
    public void setTooltipText(String text) {
        binding.itvTooltip.setTooltipText(text);
    }

    /** 设置开关状态 */
    public void setChecked(boolean checked) {
        binding.scSwitch.setChecked(checked);
    }

    /** 获取开关状态 */
    public boolean isChecked() {
        return binding.scSwitch.isChecked();
    }

    /** 获取 Switch 进行监听 */
    public SwitchCompat getSwitch() {
        return binding.scSwitch;
    }

    /** 获取内部 InfoTooltipView */
    public InfoTooltipView getInfoTooltipView() {
        return binding.itvTooltip;
    }
}
