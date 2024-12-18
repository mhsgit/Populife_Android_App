package com.populstay.populife.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.home.entity.HomeDeviceInfo;

import androidx.annotation.Nullable;

public class AddDeviceFailActivity extends BaseActivity implements View.OnClickListener {

	private static final String KEY_LOCK_TYPE = "KEY_LOCK_TYPE";

	private TextView mTvTryPairingAgain, mTvBackToSmart;
	private String mLockType;
	private TextView tvTheFollowing, tvTheFollowingDetail;

	public static void actionStart(Context context, String lockType) {
		Intent intent = new Intent(context, AddDeviceFailActivity.class);
		intent.putExtra(KEY_LOCK_TYPE, lockType);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device_fail);

		getIntentData();
		initView();
		setListener();
	}

	private void getIntentData() {
		mLockType = getIntent().getStringExtra(KEY_LOCK_TYPE);
	}

	private void initView() {
		initTitleBar();
		mTvTryPairingAgain = findViewById(R.id.tv_try_pairing_again);
		mTvBackToSmart = findViewById(R.id.tv_back_to_smart);
		tvTheFollowing = findViewById(R.id.tv_the_following);
		tvTheFollowingDetail = findViewById(R.id.tv_the_following_detail);

		if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_GATEWAY)) {
			tvTheFollowing.setText(R.string.failed_to_add_gateway_hint);
			tvTheFollowingDetail.setVisibility(View.GONE);
		} else if (HomeDeviceInfo.isDeadboltLack(mLockType)) {
			tvTheFollowingDetail.setText(R.string.the_following_detail_deadbolt);
		} else if (HomeDeviceInfo.isKeyBox(mLockType)) {
			tvTheFollowingDetail.setText(R.string.the_following_detail_keybox);
		} else if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK)
				|| mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK_LOWER)) {
			tvTheFollowingDetail.setText(R.string.the_following_detail_deadbolt);
		} else {
			tvTheFollowingDetail.setText(R.string.the_following_detail_door_lock);
		}
	}

	private void initTitleBar() {
		TextView titleTv = findViewById(R.id.page_left_title);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(R.string.failed_to_add);
		findViewById(R.id.page_title).setVisibility(View.GONE);
		findViewById(R.id.page_action).setVisibility(View.GONE);
	}

	private void setListener() {
		mTvTryPairingAgain.setOnClickListener(this);
		mTvBackToSmart.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.tv_try_pairing_again:
				if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_GATEWAY)) {
					GatewayAddGuideActivity.actionStartByTaskTop(this, mLockType);
				} else {
					LockAddGuideActivity.actionStartByTaskTop(this, mLockType);
				}
				break;
			case R.id.tv_back_to_smart:
				goToNewActivity(MainActivity.class);
				break;
		}

	}

	@Override
	public void finishCurrentActivity(View view) {
		goToNewActivity(MainActivity.class);
	}
}
