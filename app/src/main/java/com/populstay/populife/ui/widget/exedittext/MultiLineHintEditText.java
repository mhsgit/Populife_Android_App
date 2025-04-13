package com.populstay.populife.ui.widget.exedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.populstay.populife.R;

public class MultiLineHintEditText extends RelativeLayout {

    private TextView tvHint;
    private EditText etInput;

    public MultiLineHintEditText(Context context) {
        this(context, null);
    }

    public MultiLineHintEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLineHintEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttributes(context, attrs);
        setupListeners();
    }

    private void initView(Context context) {
        // 动态添加子 View
        LayoutInflater.from(context).inflate(R.layout.layout_multi_line_hint_edittext, this, true);
        tvHint = findViewById(R.id.tv_hint);
        etInput = findViewById(R.id.et_input);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiLineHintEditText);
        // 读取自定义属性
        String hintText = a.getString(R.styleable.MultiLineHintEditText_hintText);
        int hintColor = a.getColor(R.styleable.MultiLineHintEditText_hintColor, Color.GRAY);
        float hintSize = a.getDimension(R.styleable.MultiLineHintEditText_hintSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        // 应用属性到控件
        tvHint.setText(hintText);
        tvHint.setTextColor(hintColor);
        tvHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintSize);
        a.recycle();
    }

    private void setupListeners() {
        // 焦点变化监听
        etInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvHint.setVisibility(View.GONE);
            } else {
                updateHintVisibility();
            }
        });

        // 文本变化监听
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateHintVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateHintVisibility() {
        if (TextUtils.isEmpty(etInput.getText())) {
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvHint.setVisibility(View.GONE);
        }
    }

    // 暴露 EditText 的常用方法 ----------
    public String getText() {
        return etInput.getText().toString();
    }

    public void setText(String text) {
        etInput.setText(text);
        updateHintVisibility();
    }

    public void setHint(String hint) {
        tvHint.setText(hint);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        etInput.addTextChangedListener(watcher);
    }

    public void setSelection(int index) {
        etInput.setSelection(index);
    }

    // 可选：直接获取内部 EditText 对象（按需暴露更多方法）
    public EditText getEditText() {
        return etInput;
    }
}
