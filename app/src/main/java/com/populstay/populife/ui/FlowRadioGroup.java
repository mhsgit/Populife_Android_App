package com.populstay.populife.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import com.populstay.populife.R;

public class FlowRadioGroup extends RadioGroup {

    private int columnCount = 2;
    private int columnGap = 0;
    private int rowGap = 0;

    public FlowRadioGroup(Context context) {
        super(context);
        init(context, null);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray ta = context.obtainStyledAttributes(
                attrs, R.styleable.FlowRadioGroup);

        columnCount = ta.getInt(
                R.styleable.FlowRadioGroup_columnCount, 2);

        columnGap = ta.getDimensionPixelSize(
                R.styleable.FlowRadioGroup_columnGap, 0);

        rowGap = ta.getDimensionPixelSize(
                R.styleable.FlowRadioGroup_rowGap, 0);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

        int totalGapWidth = columnGap * (columnCount - 1);
        int childWidth = (parentWidth - totalGapWidth) / columnCount;

        int totalHeight = 0;
        int lineHeight = 0;
        int columnIndex = 0;

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthSpec = MeasureSpec.makeMeasureSpec(
                    childWidth - lp.leftMargin - lp.rightMargin,
                    MeasureSpec.EXACTLY);

            int childHeightSpec = MeasureSpec.makeMeasureSpec(
                    0, MeasureSpec.UNSPECIFIED);

            child.measure(childWidthSpec, childHeightSpec);

            lineHeight = Math.max(
                    lineHeight,
                    child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);

            columnIndex++;

            if (columnIndex == columnCount) {
                totalHeight += lineHeight + rowGap;
                lineHeight = 0;
                columnIndex = 0;
            }
        }

        if (columnIndex != 0) {
            totalHeight += lineHeight;
        } else if (totalHeight > 0) {
            totalHeight -= rowGap; // 去掉最后一行多加的 gap
        }

        setMeasuredDimension(parentWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int parentWidth = getMeasuredWidth();
        int totalGapWidth = columnGap * (columnCount - 1);
        int childWidth = (parentWidth - totalGapWidth) / columnCount;

        int x;
        int y = 0;
        int lineHeight = 0;
        int columnIndex = 0;

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            x = columnIndex * (childWidth + columnGap) + lp.leftMargin;

            int childTop = y + lp.topMargin;
            int childBottom = childTop + child.getMeasuredHeight();

            child.layout(
                    x,
                    childTop,
                    x + child.getMeasuredWidth(),
                    childBottom
            );

            lineHeight = Math.max(
                    lineHeight,
                    child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);

            columnIndex++;

            if (columnIndex == columnCount) {
                y += lineHeight + rowGap;
                lineHeight = 0;
                columnIndex = 0;
            }
        }
    }
}
