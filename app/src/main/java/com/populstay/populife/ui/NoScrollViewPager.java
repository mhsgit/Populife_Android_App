package com.populstay.populife.ui;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Jerry
 */
public class NoScrollViewPager extends ViewPager {
	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
		return false;
	}

	public boolean onTouchEvent(MotionEvent motionEvent) {
		return false;
	}
}