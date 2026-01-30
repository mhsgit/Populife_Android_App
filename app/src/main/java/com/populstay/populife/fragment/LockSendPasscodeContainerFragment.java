package com.populstay.populife.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.populstay.populife.R;
import com.populstay.populife.app.MyApplication;
import com.populstay.populife.base.BaseFragment;
import com.populstay.populife.base.BasePagerAdapter;
import com.populstay.populife.entity.Key;
import com.populstay.populife.keypwdmanage.KeyPwdConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送“永久密码” Fragment
 * Created by Jerry
 */

public class LockSendPasscodeContainerFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id = group.getId();
        if (id == R.id.rg_pwd_type) {
            setPwdType(checkedId);
        }
    }

    public enum Type {
        RANDOM,
        CUSTOM
    }

    private static final String ARG_TYPE = "arg_type";

    private static final String KEY_KEY = "key_key";
    private static final String KEY_LOCK_ID = "key_lock_id";
    private static final String KEY_KEY_ID = "key_key_id";
    private static final String KEY_LOCK_NAME = "key_lock_name";
    private static final String KEY_LOCK_MAC = "key_lock_mac";
    private static final String KEY_PASSWORD_LIST = "key_password_list";
    private Type mType = Type.RANDOM;

    private ViewPager mViewPager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleArr = new ArrayList<>();
    private BasePagerAdapter mAdapter;

    private String[] mPwdAccessTypeTitleArr;
    private String[] mPwdAccessTypeNameArr;
    private RadioGroup mRgPwdType;
    private TextView mPwdTypeHint;

    private String mKeyPwdType;
    private int[] mPwdAccessTypeItemArr = {R.id.rb_1, R.id.rb_2, R.id.rb_3, R.id.rb_4};
    private int[] mPwdTypeHintArr = {R.string.create_time_limited_pwd_hint, R.string.create_permanent_pwd_hint, R.string.create_recurring_pwd_hint, R.string.create_one_time_pwd_hint};
    private String[] mPwdAccessTypeArr = {KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_PERIOD,
            KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_PERMANENT,
            KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_RECURRING,
            KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_ONE_TIME};
    private int mSelectAccessTypeIndex = 0;

    private AlertDialog mPwdTypeDialog;
//    private LinearLayout mLlShowPwdDialogBtn;

    public static LockSendPasscodeContainerFragment newInstance(Key key, Type type, int lockId, int keyId, String lockName, String lockMac, ArrayList<String> passwordList) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_KEY, key);
        args.putString(ARG_TYPE, type.name());
        args.putInt(KEY_LOCK_ID, lockId);
        args.putInt(KEY_KEY_ID, keyId);
        args.putString(KEY_LOCK_NAME, lockName);
        args.putString(KEY_LOCK_MAC, lockMac);
        args.putStringArrayList(KEY_PASSWORD_LIST, passwordList);

        LockSendPasscodeContainerFragment fragment = new LockSendPasscodeContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // 获取外部传入的 type
//        if (getArguments() != null) {
//            String typeName = getArguments().getString(ARG_TYPE, Type.RANDOM.name());
//            mType = Type.valueOf(typeName);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_send_passcode_container, null);
        getIntentData();
        initView(view);
        initListener();
        initTab();
        return view;
    }
    private Key mKey = MyApplication.CURRENT_KEY;
    private ArrayList<String> mPasswordList = new ArrayList<>();
    private int mLockId, mKeyId;
    private String mLockMac, mLockName;
    private void getIntentData(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            String typeName = bundle.getString(ARG_TYPE, Type.RANDOM.name());
            mType = Type.valueOf(typeName);
            Key key = bundle.getParcelable(KEY_KEY);
            if (null != key) {
                mKey = bundle.getParcelable(KEY_KEY);
            }
            mLockId = bundle.getInt(KEY_LOCK_ID, 0);
            mKeyId = bundle.getInt(KEY_KEY_ID, 0);
            mLockName = bundle.getString(KEY_LOCK_NAME);
            mLockMac = bundle.getString(KEY_LOCK_MAC);
            mPasswordList = bundle.getStringArrayList(KEY_PASSWORD_LIST);
        }
    }
    private void initView(View view) {
//        mLlShowPwdDialogBtn = view.findViewById(R.id.ll_show_pwd_dialog_btn);
        mViewPager = view.findViewById(R.id.vp_lock_send_passcode);
        mRgPwdType = view.findViewById(R.id.rg_pwd_type);
        mPwdTypeHint = view.findViewById(R.id.tv_pwd_type_hint);
        if (mType == Type.CUSTOM) {
            view.findViewById(R.id.rb_pwd_type_recurring).setVisibility(View.GONE);
            view.findViewById(R.id.rb_pwd_type_one_time).setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mRgPwdType.setOnCheckedChangeListener(this);
    }

    protected void initTab() {

        Resources res = getResources();
        String customTip;
        if (mKey.getLockId()<0){
            customTip = res.getString(R.string.key_pwd_mh_custom_create_desc);
        }else {
            customTip = res.getString(R.string.key_pwd_custom_create_desc);
        }
        mPwdAccessTypeTitleArr = new String[]{
                res.getString(R.string.create_permanent_pwd_tips), res.getString(R.string.create_time_limited_pwd_tips),
                res.getString(R.string.create_one_time_pwd_tips), customTip};
        mPwdAccessTypeNameArr = new String[]{
                res.getString(R.string.key_pwd_permanent), res.getString(R.string.key_pwd_period),
                res.getString(R.string.key_pwd_one_time), res.getString(R.string.key_pwd_custom)};

        mFragmentList.add(LockSendPasscodeNewFragment.newInstance(mKey, mType, LockSendPasscodeNewFragment.VAL_TAB_TYPE_PERIOD,
                mLockId, mKeyId, mLockName, mLockMac, mPasswordList));//限时密码
        mFragmentList.add(LockSendPasscodeNewFragment.newInstance(mKey, mType, LockSendPasscodeNewFragment.VAL_TAB_TYPE_PERMANENT,
                mLockId, mKeyId, mLockName, mLockMac, mPasswordList));//永久密码
        mFragmentList.add(LockSendPasscodeNewFragment.newInstance(mKey, mType, LockSendPasscodeNewFragment.VAL_TAB_TYPE_CYCLIC,
                mLockId, mKeyId, mLockName, mLockMac, mPasswordList));//循环密码
        mFragmentList.add(LockSendPasscodeNewFragment.newInstance(mKey, mType, LockSendPasscodeNewFragment.VAL_TAB_TYPE_ONE_TIME,
                mLockId, mKeyId, mLockName, mLockMac, mPasswordList));//单次密码

        mAdapter = new BasePagerAdapter(getChildFragmentManager(), mFragmentList, mPwdAccessTypeTitleArr);
        mViewPager.setAdapter(mAdapter);
        setCurrentPage(mSelectAccessTypeIndex);
    }

    public void setCurrentPage(int position){
        mKeyPwdType = mPwdAccessTypeArr[position];
        mPwdTypeHint.setText(mPwdTypeHintArr[position]);
        mViewPager.setCurrentItem(position, false);
    }

    private void setPwdType(int id) {
        if (id == R.id.rb_pwd_type_period) {
            mSelectAccessTypeIndex = 0;
        } else if (id == R.id.rb_pwd_type_permanent) {
            mSelectAccessTypeIndex = 1;
        } else if (id == R.id.rb_pwd_type_recurring) {
            mSelectAccessTypeIndex = 2;
        } else if (id == R.id.rb_pwd_type_one_time) {
            mSelectAccessTypeIndex = 3;
        }
        setCurrentPage(mSelectAccessTypeIndex);
    }


}
