package com.populstay.populife.activity;

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
import com.populstay.populife.entity.FirmwareInfo;
import com.populstay.populife.entity.Key;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.lock.ILockGetFirmware;
import com.populstay.populife.manhattanlock.MHILockFindMyDevice;
import com.populstay.populife.net.RestClient;
import com.populstay.populife.net.callback.ISuccess;
import com.populstay.populife.ui.loader.PeachLoader;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;
import com.ttlock.bl.sdk.api.DeviceFirmwareUpdateApi;
import com.ttlock.bl.sdk.service.DfuService;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static com.populstay.populife.app.MyApplication.mTTLockAPI;
import static com.populstay.populife.app.MyApplication.sPPLOCK;

public class LockUpdateActivity extends BaseActivity {

	private TextView mTvState, mTvVersion, mTvUpdate;

	private DeviceFirmwareUpdateApi mDeviceFirmwareUpdateApi;
	private FirmwareInfo firmwareInfo;
	private Key mKey = MyApplication.CURRENT_KEY;

	private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
		@Override
		public void onDeviceConnecting(String deviceAddress) {
			PeachLogger.i("dfu", "onDeviceConnecting");
		}

		@Override
		public void onDeviceConnected(String deviceAddress) {
			PeachLogger.i("dfu", "onDeviceConnected");
		}

		@Override
		public void onDfuProcessStarting(String deviceAddress) {
			PeachLogger.i("dfu", "onDfuProcessStarting");
		}

		@Override
		public void onDfuProcessStarted(String deviceAddress) {
			PeachLogger.i("dfu", "onDfuProcessStarted");
		}

		@Override
		public void onEnablingDfuMode(String deviceAddress) {
			PeachLogger.i("dfu", "onEnablingDfuMode");
		}

		@Override
		public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
			PeachLogger.i("dfu", "onProgressChanged");
			PeachLogger.i("dfu", "onProgressChanged" + percent);
			//dfuDialogFragment.setProgress(percent);
		}

		@Override
		public void onFirmwareValidating(String deviceAddress) {
			PeachLogger.i("dfu", "onFirmwareValidating");
		}

		@Override
		public void onDeviceDisconnecting(String deviceAddress) {

			PeachLogger.i("dfu", "onDeviceDisconnecting");
		}

		@Override
		public void onDeviceDisconnected(String deviceAddress) {
			PeachLogger.i("dfu", "onDeviceDisconnected");

		}

		@Override
		public void onDfuCompleted(String deviceAddress) {
			PeachLogger.i("dfu", "onDfuCompleted");
     //		stopDfu();
//			dfuDialogFragment.getProgressBar().setIndeterminate(true);
			//升级成功，重新连接设备
			toast(R.string.update_success_tips);
		}

		@Override
		public void onDfuAborted(String deviceAddress) {
			PeachLogger.i("dfu", "onDfuAborted");
			//升级流产，失败
			toast(R.string.update_fail_tips);
		}

		@Override
		public void onError(String deviceAddress, int error, int errorType, String message) {
			PeachLogger.i("dfu", "onError");
//			stopDfu();
//			dfuDialogFragment.dismiss();
			//toast("升级失败，请重试");
			toast(R.string.update_fail_tips);
		}
	};

//	private DeviceFirmwareUpdateCallback deviceFirmwareUpdateCallback = new DeviceFirmwareUpdateCallback() {
//		@Override
//		public void onGetLockFirmware(int specialValue, String module, String hardware, String firmware) {
//			PeachLogger.d("firmwareInfo", firmwareInfo);
//			if (firmwareInfo != null) {
//				firmwareInfo.specialValue = specialValue;
//				firmwareInfo.modelNum = module;
//				firmwareInfo.hardwareRevision = hardware;
//				firmwareInfo.firmwareRevision = firmware;
//				checkAgain();
//			}
//		}
//
//		@Override
//		public void onStatusChanged(final int status) {
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					switch (status) {
//						case DeviceFirmwareUpdateApi.UpgradeOprationPreparing:
//							binding.status.setText(getString(R.string.words_preparing));
//							break;
//						case DeviceFirmwareUpdateApi.UpgradeOprationUpgrading:
//							binding.status.setText(getString(R.string.words_upgrading));
//							progressDialog = new ProgressDialog(DeviceFirmwareUpdateActivity.this);
//							break;
//						case DeviceFirmwareUpdateApi.UpgradeOprationRecovering:
//							binding.status.setText(getString(R.string.words_recovering));
//							break;
//						case DeviceFirmwareUpdateApi.UpgradeOprationSuccess:
//							mDeviceFirmwareUpdateApi.upgradeComplete();
//							cancelProgressDialog();
//							binding.status.setText(getString(R.string.words_upgrade_successed));
//							toast(getString(R.string.words_upgrade_successed));
//							break;
//					}
//				}
//			});
//		}
//
//		@Override
//		public void onDfuAborted(String deviceAddress) {
//			LogUtil.d(deviceAddress, DBG);
//		}
//
//		@Override
//		public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
//			LogUtil.e("percent:" + percent, DBG);
//			cancelProgressDialog();
//			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			progressDialog.setMax(100);
//			progressDialog.setProgress(percent);
//			progressDialog.show();
//		}
//
//		@Override
//		public void onDfuProcessStarting(final String deviceAddress) {
//			LogUtil.d("deviceAddress:" + deviceAddress, DBG);
//		}
//
//		@Override
//		public void onEnablingDfuMode(final String deviceAddress) {
//			LogUtil.d("deviceAddress:" + deviceAddress, DBG);
//		}
//
//		@Override
//		public void onDfuCompleted(final String deviceAddress) {
//			LogUtil.d("deviceAddress:" + deviceAddress, DBG);
//			progressDialog.cancel();
//			showProgressDialog(getString(R.string.words_recovering));
//		}
//
//		@Override
//		public void onError(int errorCode, Error error, String errorContent) {
//			LogUtil.w("errorCode:" + errorCode, DBG);
//			LogUtil.w("error:" + error, DBG);
//			LogUtil.w("errorContent:" + errorContent, DBG);
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					cancelProgressDialog();
//					binding.status.setText(getString(R.string.words_upgrade_failed));
//					showRetryDialog();
//				}
//			});
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_update);

		initView();
		initListener();
		if(mKey.getLockId()>0){
			requestFirmwareInfo();
		}
//		getLockFirmware();
	}

	private void initView() {
		((TextView) findViewById(R.id.page_title)).setText(R.string.lock_update);
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mTvState = findViewById(R.id.tv_lock_update_state);
		mTvVersion = findViewById(R.id.tv_lock_update_version);
		mTvUpdate = findViewById(R.id.tv_lock_update);
		if (mKey.getLockId()<0){
			mTvVersion.setText(mKey.getFirmwareRevision());
		}

//		mDeviceFirmwareUpdateApi = new DeviceFirmwareUpdateApi(this, MyApplication.mTTLockAPI, deviceFirmwareUpdateCallback);
	}

	private void initListener() {
		mTvUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				requestFirmwareInfo();
//				mTTLockAPI.getVersionInfo();
				if(mKey.getLockId()<0){
					toast(R.string.no_update);
					//updateFirmware();
				}else {
					if (firmwareInfo.getNeedUpgrade() == 0) {
						toast(R.string.no_update);
					}
				}
			}
		});
	}

	private void getLockFirmware() {
		PeachLoader.showLoading(this);
		if (mTTLockAPI.isConnected(mKey.getLockMac())) {
			mDeviceFirmwareUpdateApi.getLockFirmware(mKey.getLockMac(), mKey.getLockVersion(),
					mKey.getAdminPwd(), mKey.getLockKey(), mKey.getLockFlagPos(), mKey.getAesKeyStr());
		} else {//connect the lock
			MyApplication.bleSession.setLockmac(mKey.getLockMac());
			MyApplication.bleSession.setOperation(Operation.GET_LOCK_VERSION_INFO);
//			mTTLockAPI.connect(mKey.getLockMac());
			kjxRequestBleConnectPermissionStartConnect(mKey.getLockMac());
		}
	}

	private void setGetFirmwareCallback() {
		MyApplication.bleSession.setOperation(Operation.GET_LOCK_VERSION_INFO);
		MyApplication.bleSession.setILockGetFirmware(new ILockGetFirmware() {
			@Override
			public void onGetFirmwareSuccess() {
				PeachLoader.stopLoading();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

					}
				});
			}

			@Override
			public void onGetFirmwareFail() {
				PeachLoader.stopLoading();

			}
		});
	}

	private void requestFirmwareInfo() {
		RestClient.builder()
				.url(Urls.LOCK_FIRMWARE_INFO)
				.loader(this)
				.params("lockId", mKey.getLockId())
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						PeachLogger.d("LOCK_FIRMWARE_INFO", response);

						JSONObject result = JSON.parseObject(response);
						int code = result.getInteger("code");
						if (code == 200) {
							JSONObject lockInfo = result.getJSONObject("data");
							firmwareInfo = new FirmwareInfo();
							firmwareInfo.setNeedUpgrade(lockInfo.getInteger("needUpgrade"));
							firmwareInfo.setModelNum(lockInfo.getString("modelNum"));
							firmwareInfo.setHardwareRevision(lockInfo.getString("hardwareRevision"));
							firmwareInfo.setFirmwareRevision(lockInfo.getString("firmwareRevision"));
							if (lockInfo.containsKey("version")) {
								firmwareInfo.setVersion(lockInfo.getString("version"));
							}

							mTvVersion.setText(firmwareInfo.getFirmwareRevision());
						}
					}
				})
				.build()
				.get();
	}


	/**
	 * 固件升级
	 */
	private void startDFU(String mac_address) {
//		sPPLOCK.dfuAuthVerify();
		showLoading();
		DfuServiceInitiator dfu = new DfuServiceInitiator(mac_address);
		dfu.setDeviceName(mKey.getLockName());
		dfu.setDisableNotification(true);
		dfu.setZip(R.raw.dfu_package_pl_hl112);
		dfu.start(this, DfuService.class);
//		new DfuServiceInitiator(mac_address)
//				//.setForeground(false)
//				.setDisableNotification(true)
//				.setZip(R.raw.dfu_package_pl_hl112)
////				.setZip("file:///android_asset/" + "raw/Dfu_package_pl_hl111.zip")
//				.start(this, DfuService.class);

	}

	private void updateFirmware() {
		showLoading();
		setEnterUpdateModeCallback();
		if (sPPLOCK.isConnected(mKey.getLockMac())) {
			sPPLOCK.findMyDevice(PeachPreference.readUserId(),
					String.valueOf(mKey.getLockId()), String.valueOf(mKey.getKeyId()),mKey.getK1());
		} else {
//			sPPLOCK.connect(mKey.getLockMac());
			MyApplication.pplBleSession.setLockMac(mKey.getLockMac());
			startLockActionScan();
		}
	}

	private void setEnterUpdateModeCallback() {
		MyApplication.pplBleSession.setOperation(LockOperation.FIND_MY_DEVICE);
		MyApplication.pplBleSession.setILockFindMyDevice(new MHILockFindMyDevice() {
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						stopLoading();
						toast("进入固件升级模式成功");
						startDFU(mKey.getLockMac());
					}
				});
			}

			@Override
			public void onFail() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						stopLoading();
						toast("进入固件升级模式失败");
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
		super.onResume();
	}

	@Override
	protected void onPause() {
		DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
		super.onPause();
	}
}
