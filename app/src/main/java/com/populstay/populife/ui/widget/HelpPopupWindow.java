package com.populstay.populife.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.populstay.populife.R;

public class HelpPopupWindow {

    private PopupWindow mPopupWindow;
    private Context mContext;
    private View mContentView;
    private int mContentViewWidth, mContentViewHeight;
    private int mLeftSpace,mRightSpace;
    private TextView tv_msg;



    public HelpPopupWindow(Context context) {
        this(context,R.layout.help_popup_window_layout3,R.dimen.help_win_width,R.dimen.help_win_height);
    }

    public HelpPopupWindow(Context context, @LayoutRes int contentLayoutRes, @DimenRes int widthRes, @DimenRes int heightRes) {
        this.mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(contentLayoutRes, null);
        tv_msg = mContentView.findViewById(R.id.tv_msg);
        if (null != tv_msg){
            tv_msg.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        mPopupWindow = new PopupWindow();

        mLeftSpace = mRightSpace = (int) mContext.getResources().getDimension(R.dimen.common_page_left_right_space);
        mContentViewWidth = (int) mContext.getResources().getDimension(widthRes);
        mContentViewHeight = (int) mContext.getResources().getDimension(heightRes);
        mPopupWindow.setWidth(mContentViewWidth);
        mPopupWindow.setHeight(mContentViewHeight);
        mPopupWindow.setContentView(mContentView);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void show(View anchor,int gravity){
        show(anchor, gravity,null);
    }

    public void show(View anchor,int gravity,String msg){
        if (null == mPopupWindow){
            return;
        }
        if (mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else {
            if (null != tv_msg && !TextUtils.isEmpty(msg)){
                tv_msg.setText(msg);
            }
            setShowLocation(anchor, gravity);
        }
    }

    private void setShowLocation(View anchor, int gravity) {
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        switch (gravity) {
            // 左下角
            case Gravity.LEFT:
                mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, mLeftSpace, location[1] + anchor.getHeight());
                break;
            // 右下角
            case Gravity.RIGHT:
                mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
                break;
            case Gravity.BOTTOM:
                mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, (location[0] + anchor.getWidth() / 2) - mContentViewWidth / 2, location[1] - mContentViewHeight);
                break;

        }
    }

    public void dismiss(){
        if (null == mPopupWindow){
            return;
        }
        mPopupWindow.dismiss();
    }


}
