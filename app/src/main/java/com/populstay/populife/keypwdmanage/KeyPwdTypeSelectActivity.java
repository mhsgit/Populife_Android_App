package com.populstay.populife.keypwdmanage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.entity.Key;
import com.populstay.populife.util.DensityUtils;

import androidx.annotation.Nullable;

public class KeyPwdTypeSelectActivity extends BaseActivity implements View.OnClickListener {

	public static final String KEY_PWD_TYPE_SELECTED = "KEY_PWD_TYPE_SELECTED";
	public static final int KEY_PWD_TYPE_REQUEST_CODE = 0x01;
	private LinearLayout mLlKeyPwdTypePermanent, mLlKeyPwdTypePeriod, mLlKeyPwdTypeOneTime, mLlKeyPwdTypeCustom, mLlKeyPwdTypeKeyBtKey;
	private String currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_KEY_BT_KEY;
	private TextView tv_tap_create_key;
	private static final String KEY_KEY = "KEY_KEY";
	private Key mKey;

	public static void actionStartForResult(Activity context, Key key) {
		Intent intent = new Intent(context, KeyPwdTypeSelectActivity.class);
		intent.putExtra(KEY_KEY, key);
		context.startActivityForResult(intent, KEY_PWD_TYPE_REQUEST_CODE);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_pwd_type_select);
		mKey = getIntent().getParcelableExtra(KEY_KEY);
		initView();
		setItemNameAndIcon();

	}

	private void initView() {

		TextView mPageTitle = findViewById(R.id.page_title);
		mPageTitle.setText(getResources().getString(R.string.select_key_pwd_type));
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mLlKeyPwdTypePermanent = findViewById(R.id.ll_key_pwd_type_permanent);
		mLlKeyPwdTypePeriod = findViewById(R.id.ll_key_pwd_type_period);
		mLlKeyPwdTypeOneTime = findViewById(R.id.ll_key_pwd_type_one_time);
		mLlKeyPwdTypeCustom = findViewById(R.id.ll_key_pwd_type_custom);
		tv_tap_create_key = findViewById(R.id.tv_tap_create_key);

		mLlKeyPwdTypePermanent.setOnClickListener(this);
		mLlKeyPwdTypePeriod.setOnClickListener(this);
		mLlKeyPwdTypeOneTime.setOnClickListener(this);
		mLlKeyPwdTypeCustom.setOnClickListener(this);
		tv_tap_create_key.setOnClickListener(this);
	}

	private void setItemNameAndIcon() {

		int iconPadding = DensityUtils.dp2px(this, 12);

		ImageView ivPwdPermanent = mLlKeyPwdTypePermanent.findViewById(R.id.iv_item_icon);
		ivPwdPermanent.setImageResource(R.drawable.pwd_permanent_icon);
		TextView tvPwdPermanent = mLlKeyPwdTypePermanent.findViewById(R.id.tv_item_name);
		tvPwdPermanent.setText(R.string.key_pwd_permanent);
		TextView tvDescPermanent = mLlKeyPwdTypePermanent.findViewById(R.id.tv_desc);
		tvDescPermanent.setText(R.string.key_pwd_permanent_create_desc);

		ImageView ivPwdPeriod = mLlKeyPwdTypePeriod.findViewById(R.id.iv_item_icon);
		ivPwdPeriod.setImageResource(R.drawable.pwd_period_icon);
		TextView tvPwdPeriod = mLlKeyPwdTypePeriod.findViewById(R.id.tv_item_name);
		tvPwdPeriod.setText(R.string.key_pwd_period);
		TextView tvDescPwdPeriod = mLlKeyPwdTypePeriod.findViewById(R.id.tv_desc);
		tvDescPwdPeriod.setText(R.string.key_pwd_period_create_desc);


		ImageView ivPwdOneTime = mLlKeyPwdTypeOneTime.findViewById(R.id.iv_item_icon);
		ivPwdOneTime.setImageResource(R.drawable.pwd_one_time_icon);
		TextView tvPwdOneTime = mLlKeyPwdTypeOneTime.findViewById(R.id.tv_item_name);
		tvPwdOneTime.setText(R.string.key_pwd_one_time);
		TextView tvDescPwdOneTime = mLlKeyPwdTypeOneTime.findViewById(R.id.tv_desc);
		tvDescPwdOneTime.setText(R.string.key_pwd_one_time_create_desc);

		ImageView ivPwdCustom = mLlKeyPwdTypeCustom.findViewById(R.id.iv_item_icon);
		ivPwdCustom.setImageResource(R.drawable.pwd_custom_icon);
		TextView tvPwdCustom = mLlKeyPwdTypeCustom.findViewById(R.id.tv_item_name);
		tvPwdCustom.setText(R.string.key_pwd_custom);
		TextView tvDescPwdCustom = mLlKeyPwdTypeCustom.findViewById(R.id.tv_desc);
		if (mKey.getLockId() < 0) {
			tvDescPwdCustom.setText(R.string.key_pwd_mh_custom_create_desc);
		}else {
			tvDescPwdCustom.setText(R.string.key_pwd_custom_create_desc);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_key_pwd_type_permanent:
				currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_PERMANENT;
				setResult();
				break;
			case R.id.ll_key_pwd_type_period:
				currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_PERIOD;
				setResult();
				break;
			case R.id.ll_key_pwd_type_one_time:
				currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_ONE_TIME;
				setResult();
				break;
			case R.id.ll_key_pwd_type_custom:
				currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_CUSTOM;
				setResult();
				break;
			case R.id.tv_tap_create_key:
				currentKeyPwdType = KeyPwdConstant.IKeyPwdType.KEY_PWD_TYPE_KEY_BT_KEY;
				setResult();
				break;
		}
	}

	private void setResult() {
		Intent data = new Intent();
		data.putExtra(KEY_PWD_TYPE_SELECTED, currentKeyPwdType);
		setResult(RESULT_OK, data);
		finish();
	}

}
