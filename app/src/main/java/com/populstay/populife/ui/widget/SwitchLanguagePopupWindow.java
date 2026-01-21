package com.populstay.populife.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.populstay.populife.R;

public class SwitchLanguagePopupWindow implements View.OnClickListener {

    private PopupWindow mPopupWindow;
    private Context mContext;
    private View mContentView;
    private int mContentViewWidth, mContentViewHeight;
    private int mLeftSpace,mRightSpace;
    private TextView tv_lan_english,tv_lan_chinese,tv_language_japanese,tv_language_german,tv_language_french,tv_language_italian,tv_language_spanish;
    // 1: 英文, 2: 简体中文, 3: 日语, 4: 德语, 5: 法语, 6: 意大利语, 7: 西班牙语
    private int mLanguageType = 1;
    private SelectLanguageListener mSelectLanguageListener;


    public SwitchLanguagePopupWindow(Context context) {
        this(context,R.layout.switch_language_popup_window_layout,R.dimen.help_win_width,R.dimen.help_win_height);
    }

    public SwitchLanguagePopupWindow(Context context, @LayoutRes int contentLayoutRes, @DimenRes int widthRes, @DimenRes int heightRes) {
        this.mContext = context;
        mContentView = LayoutInflater.from(mContext).inflate(contentLayoutRes, null);
        initLanItemView();
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

    private void initLanItemView(){
        tv_lan_english = mContentView.findViewById(R.id.tv_lan_english);
        tv_lan_chinese = mContentView.findViewById(R.id.tv_lan_chinese);
        tv_language_japanese = mContentView.findViewById(R.id.tv_language_japanese);
         tv_language_german = mContentView.findViewById(R.id.tv_language_german);
         tv_language_french = mContentView.findViewById(R.id.tv_language_french);
         tv_language_italian = mContentView.findViewById(R.id.tv_language_italian);
         tv_language_spanish = mContentView.findViewById(R.id.tv_language_spanish);

        tv_lan_english.setOnClickListener(this);
        tv_lan_chinese.setOnClickListener(this);
        tv_language_japanese.setOnClickListener(this);
        tv_language_german.setOnClickListener(this);
        tv_language_french.setOnClickListener(this);
        tv_language_italian.setOnClickListener(this);
        tv_language_spanish.setOnClickListener(this);
    }

    public void show(View anchor,int gravity){
        if (null == mPopupWindow){
            return;
        }
        if (mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else {
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
            case Gravity.TOP:
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_lan_english) {
            mLanguageType = 1;
        } else if (id == R.id.tv_lan_chinese) {
            mLanguageType = 2;
        } else if (id == R.id.tv_language_japanese) {
            mLanguageType = 3;
        } else if (id == R.id.tv_language_french) {
            mLanguageType = 4;
        } else if (id == R.id.tv_language_german) {
            mLanguageType = 5;
        } else if (id == R.id.tv_language_italian) {
            mLanguageType = 6;
        } else if (id == R.id.tv_language_spanish) {
            mLanguageType = 7;
        }

        if (mSelectLanguageListener != null) {
            mSelectLanguageListener.onSelectLanguage(mLanguageType);
        }
        dismiss();
    }

    public void setSelectLanguageListener(SelectLanguageListener selectLanguageListener) {
        this.mSelectLanguageListener = selectLanguageListener;
    }

    public interface SelectLanguageListener{

        void onSelectLanguage(int languageType);
    }
}
