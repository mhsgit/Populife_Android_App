package com.populstay.populife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.populock.manhattan.sdk.constant.LockOperation;
import com.populstay.populife.R;
import com.populstay.populife.app.MyApplication;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.common.Urls;
import com.populstay.populife.entity.Key;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.lock.ILockSetTime;
import com.populstay.populife.manhattanlock.MHILockSetTime;
import com.populstay.populife.net.RestClient;
import com.populstay.populife.net.callback.IError;
import com.populstay.populife.net.callback.IFailure;
import com.populstay.populife.net.callback.ISuccess;
import com.populstay.populife.util.date.DateUtil;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;

import static com.populstay.populife.app.MyApplication.mTTLockAPI;
import static com.populstay.populife.app.MyApplication.sPPLOCK;

public class LockTimeActivity extends BaseActivity {

	private static final String KEY_LOCK_TIME = "key_lock_time";
	private static final String KEY_KEY = "KEY_KEY";

	private TextView mTvTime, mTvCalibrate;

	private Key mKey = MyApplication.CURRENT_KEY;
	private long mLockTime;
	private boolean mIsLockOperationSuccess;

	/**
	 * @param lockTime 锁时间
	 */
	public static void actionStart(Activity context, long lockTime, int requestCode, Key key) {
		Intent intent = new Intent(context, LockTimeActivity.class);
		intent.putExtra(KEY_LOCK_TIME, lockTime);
		intent.putExtra(KEY_KEY, key);
		context.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_time);

		getIntentData();
		initView();
		initListener();
	}

	private void getIntentData() {
		Intent data = getIntent();
		mLockTime = data.getLongExtra(KEY_LOCK_TIME, DateUtil.getCurTimeMillis());
		if (data.hasExtra(KEY_KEY)){
			mKey = data.getParcelableExtra(KEY_KEY);
			MyApplication.CURRENT_KEY = mKey;
		}
	}

	private void initView() {
		((TextView) findViewById(R.id.page_title)).setText(R.string.calibrate_time);
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mTvTime = findViewById(R.id.tv_lock_time);
		mTvCalibrate = findViewById(R.id.tv_lock_time_calibrate);
		mTvTime.setText(DateUtil.getDateToString(mLockTime, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
	}

	private void initListener() {
		mTvCalibrate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isBleEnableWithoutToast()) { // 蓝牙开启
					// 和锁通信，校准锁时间
					calibrateLockTime();
				} else if (isNetEnableWithoutToast()) { // 网络开启
					if (mKey.getLockId()>0){
						// 通过网关校准锁时间
						calibrateLockTimeViaGateway();
					}
				} else {
					toastFail();
				}
			}
		});
	}

	private void calibrateLockTime() {
		showLoading();
		mLockTime = DateUtil.getCurTimeMillis();
		setSetTimeCallback();
        if (mKey.getLockId()<0){
			if (sPPLOCK.isConnected(mKey.getLockMac())) {
				sPPLOCK.setLockTime(PeachPreference.readUserId(),String.valueOf(mKey.getLockId()),
						String.valueOf(mKey.getKeyId()),mLockTime,mKey.getK1());
			} else {
				//sPPLOCK.connect(mKey.getLockMac());
				startLockActionScan();
			}
		}else {
			if (mTTLockAPI.isConnected(mKey.getLockMac())) {
				mTTLockAPI.setLockTime(null, PeachPreference.getOpenid(),
						mKey.getLockVersion(), mKey.getLockKey(), mLockTime,
						mKey.getLockFlagPos(), mKey.getAesKeyStr(), mKey.getTimezoneRawOffset());
			} else {
//				mTTLockAPI.connect(mKey.getLockMac());
				kjxRequestBleConnectPermissionStartConnect(mKey.getLockMac());
			}
		}
	}

	private void setSetTimeCallback() {
		if (mKey.getLockId()<0){
			MyApplication.pplBleSession.setOperation(LockOperation.SET_LOCK_TIME);
			MyApplication.pplBleSession.setmILockSetTime(new MHILockSetTime() {
				@Override
				public void onSuccess() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							stopLoading();
							mIsLockOperationSuccess = true;
							toast(R.string.calibrate_time_success);
							mTvTime.setText(DateUtil.getDateToString(mLockTime, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
						}
					});
				}

				@Override
				public void onFail() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							stopLoading();
							mIsLockOperationSuccess = true;
							toastFail();
						}
					});
				}
			});
		}else {
			MyApplication.bleSession.setOperation(Operation.SET_LOCK_TIME);
			MyApplication.bleSession.setILockSetTime(new ILockSetTime() {
				@Override
				public void onSuccess() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							stopLoading();
							mIsLockOperationSuccess = true;
							toast(R.string.calibrate_time_success);
							mTvTime.setText(DateUtil.getDateToString(mLockTime, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
						}
					});
				}

				@Override
				public void onFail() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							stopLoading();
							mIsLockOperationSuccess = true;
							if (isNetEnableWithoutToast()) { // 网络开启
								// 通过网关校准锁时间
								calibrateLockTimeViaGateway();
							} else {
								toastFail();
							}
						}
					});
				}

				@Override
				public void onTimeOut() {
					if (!mIsLockOperationSuccess) {
						// 通过网关校准锁时间
						calibrateLockTimeViaGateway();
					}
				}
			});
		}
	}

	/**
	 * 通过网关校准锁时间
	 */
	private void calibrateLockTimeViaGateway() {
		RestClient.builder()
				.url(Urls.GATEWAY_LOCK_TIME_CALIBRATE)
				.loader(this)
				.params("lockId", mKey.getLockId())
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						PeachLogger.d("GATEWAY_LOCK_TIME_CALIBRATE", response);

						JSONObject result = JSON.parseObject(response);
						int code = result.getInteger("code");
						if (code == 200) {
							toast(R.string.calibrate_time_success);
							mTvTime.setText(DateUtil.getDateToString(mLockTime, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
						} else {
							toastFail();
						}
					}
				})
				.failure(new IFailure() {
					@Override
					public void onFailure() {
						toastFail();
					}
				})
				.error(new IError() {
					@Override
					public void onError(int code, String msg) {
						toastFail();
					}
				})
				.build()
				.post();
	}

	@Override
	public void finishCurrentActivity(View view) {
		setResultToBack();
		super.finishCurrentActivity(view);
	}

	@Override
	public void onBackPressed() {
		setResultToBack();
		finish();
	}

	private void setResultToBack() {
		Intent data = new Intent();
		data.putExtra(LockSettingsActivity.KEY_RESULT_DATA, String.valueOf(mLockTime));
		setResult(RESULT_OK, data);
	}
}
