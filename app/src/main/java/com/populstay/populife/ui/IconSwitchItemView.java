package com.populstay.populife.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.populstay.populife.R;
import com.populstay.populife.databinding.ViewIconSwitchItemBinding;

public class IconSwitchItemView extends ConstraintLayout {

    private ViewIconSwitchItemBinding binding;

    public IconSwitchItemView(Context context) {
        this(context, null);
    }

    public IconSwitchItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconSwitchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = ViewIconSwitchItemBinding.inflate(LayoutInflater.from(context), this, true);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconSwitchItemView);
            String title = ta.getString(R.styleable.IconSwitchItemView_titleText);
            int iconRes = ta.getResourceId(R.styleable.IconSwitchItemView_iconSrc, R.mipmap.ic_info);
            boolean checked = ta.getBoolean(R.styleable.IconSwitchItemView_checked, false);
            ta.recycle();

            binding.tvTitle.setText(title);
            binding.ivIcon.setImageResource(iconRes);
            setChecked(checked);
        }

        // 预览模式显示默认内容
        if (isInEditMode()) {
            if (binding.tvTitle.getText() == null || binding.tvTitle.getText().length() == 0) {
                binding.tvTitle.setText("远程解锁");
            }
            binding.ivIcon.setImageResource(R.mipmap.ic_info);
            setChecked(true);
        }
    }

    /** 设置左侧图标 */
    public void setIcon(int resId) {
        binding.ivIcon.setImageResource(resId);
    }

    /** 设置标题 */
    public void setTitle(String text) {
        binding.tvTitle.setText(text);
    }

    /** 设置选中状态 */
    public void setChecked(boolean checked) {
        if (checked) {
            binding.ivYes.setVisibility(View.VISIBLE);
            binding.ivNo.setVisibility(View.GONE);
        } else {
            binding.ivYes.setVisibility(View.GONE);
            binding.ivNo.setVisibility(View.VISIBLE);
        }
    }

    /** 获取当前状态 */
    public boolean isChecked() {
        return binding.ivYes.getVisibility() == View.VISIBLE;
    }

    /** 获取内部控件，方便进一步操作 */
    public TextView getTextView() {
        return binding.tvTitle;
    }

    public ImageView getIconView() {
        return binding.ivIcon;
    }

    public ImageView getYesView() {
        return binding.ivYes;
    }

    public ImageView getNoView() {
        return binding.ivNo;
    }
}
