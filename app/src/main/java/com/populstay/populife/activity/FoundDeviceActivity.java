package com.populstay.populife.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meiqia.core.MQManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.populock.manhattan.sdk.BleDevice;
import com.populock.manhattan.sdk.constant.LockOperation;
import com.populstay.populife.R;
import com.populstay.populife.adapter.FoundDeviceAdapter;
import com.populstay.populife.app.MyApplication;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.base.BaseApplication;
import com.populstay.populife.common.Urls;
import com.populstay.populife.constant.BleConstant;
import com.populstay.populife.db.PopulifeDBUtil;
import com.populstay.populife.entity.Key;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.eventbus.Event;
import com.populstay.populife.home.entity.HomeDevice;
import com.populstay.populife.home.entity.HomeDeviceInfo;
import com.populstay.populife.net.RestClient;
import com.populstay.populife.net.callback.IError;
import com.populstay.populife.net.callback.IFailure;
import com.populstay.populife.net.callback.ISuccess;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;
import com.populstay.populife.util.string.StringUtil;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.entity.LockError;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;

import static com.populstay.populife.app.MyApplication.mTTLockAPI;
import static com.populstay.populife.app.MyApplication.sPPLOCK;

public class FoundDeviceActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

	public static final String TAG = FoundDeviceActivity.class.getSimpleName();
	public static final int SCAN_TIME_OUT_SECONDS = 10 * 1000;
	private static final String KEY_LOCK_TYPE = "KEY_LOCK_TYPE";
	TextView tvPageTitle;
	private List<ExtendedBluetoothDevice> mKJXLockList = new ArrayList<>();
	private List<BleDevice> mMHTLockList = new ArrayList<>();
	private List<Object> mAllLockList = new ArrayList<>();
	private ListView mListView;
	private LinearLayout ll_found_device;
	private ImageView mIvNewMsg;
	private LinearLayout mLlFoundDeviceView;
	private SeekBar mSeekbarScanDevice;
	private TextView mTvScanDevice, mTv_tap_help;
	private TextView mKeepTheKeypadLight;
	private FoundDeviceAdapter mAdapter;
	private AlertDialog DIALOG;
	private int mCurrentScanProgress = 0;
	private Key mKey;
	private String mLockType;
	private boolean isMHTLock = false;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			Log.e("mhs", "mRunnable--mCurrentScanProgress=" + mCurrentScanProgress);
			if (mCurrentScanProgress >= SCAN_TIME_OUT_SECONDS) {
				showNoResultDialog();
			} else {
				upDateSeekbarScanDevice();
			}
		}
	};
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BleConstant.ACTION_BLE_DEVICE.equals(action)) {
				setTitleText(false);
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object obj = bundle.getParcelable(BleConstant.DEVICE);
					if (obj instanceof BleDevice) {
						//mAdapter = new FoundDeviceAdapter(FoundDeviceActivity.this,mMHLockList,mLockList,true);
						BleDevice device = (BleDevice) obj;
						PeachLogger.d("device=" + device.toString());
						mAdapter.setMHTState(true);
						mAdapter.UpdateMHTDevice(device);
					} else {
                        return;
                    }

					mListView.setAdapter(mAdapter);
//					if (isMHLock) {
//						BleDevice device = bundle.getParcelable(BleConstant.DEVICE);
//						PeachLogger.d("device=" + device.toString());
//						mAdapter.mhUpdateDevice(device);
//					}else {
//						ExtendedBluetoothDevice device = bundle.getParcelable(BleConstant.DEVICE);
//						PeachLogger.d("device=" + device.toString());
//						mAdapter.updateDevice(device);
//					}
					BaseApplication.getHandler().removeCallbacks(mRunnable);
					ll_found_device.setVisibility(View.VISIBLE);
					mLlFoundDeviceView.setVisibility(View.GONE);
					if (DIALOG != null) {
						DIALOG.cancel();
					}
				}
			}
		}
	};
	private int mLockId, mKeyId, mBattery;
	private String mLockName;
	private HomeDevice mHomeDevice = new HomeDevice();

	public static void actionStart(Context context, String lockType) {
		Intent intent = new Intent(context, FoundDeviceActivity.class);
		intent.putExtra(KEY_LOCK_TYPE, lockType);
		context.startActivity(intent);

	}

	private void showNoResultDialog() {
		if (isFinishing()) {
			return;
		}
		DIALOG = new AlertDialog.Builder(this).create();
		DIALOG.setCanceledOnTouchOutside(false);
		DIALOG.show();
		final Window window = DIALOG.getWindow();
		if (window != null) {
			window.setContentView(R.layout.dialog_input);
			window.setGravity(Gravity.CENTER);
			window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//设置属性
			final WindowManager.LayoutParams params = window.getAttributes();
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			params.dimAmount = 0.5f;
			window.setAttributes(params);

			((TextView) window.findViewById(R.id.tv_dialog_input_title)).setText(R.string.search_time_out);
			window.findViewById(R.id.et_dialog_input_content).setVisibility(View.GONE);
			TextView tvDialogContent = window.findViewById(R.id.tv_dialog_content);
			tvDialogContent.setVisibility(View.VISIBLE);
			if (HomeDeviceInfo.isDeadboltLack(mLockType)) {
				tvDialogContent.setText(R.string.deadbolt_not_found_try_again);
			} else if (HomeDeviceInfo.isKeyBox(mLockType)) {
				tvDialogContent.setText(R.string.keybox_not_found_try_again);
			} else if (HomeDeviceInfo.isKeyBoxK4(mLockType)) {
				tvDialogContent.setText(R.string.keybox_not_found_try_again_k4);
			} else if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK)
					|| mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK_LOWER)) {
				tvDialogContent.setText(R.string.moonlock_not_found_try_again);
			} else {
				tvDialogContent.setText(R.string.door_lock_not_found_try_again);
			}
			window.findViewById(R.id.btn_dialog_input_cancel).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					LockAddGuideActivity.actionStartByTaskTop(FoundDeviceActivity.this, mLockType);
				}
			});
			TextView okBtn = window.findViewById(R.id.btn_dialog_input_ok);
			okBtn.setText(R.string.scan_again);
			okBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DIALOG.cancel();
					initSeekbarScanDevice();
				}
			});
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found_device);
		getIntentData();
		initView();
	}

	private void initView() {
		ll_found_device = findViewById(R.id.ll_found_device);
		mLlFoundDeviceView = findViewById(R.id.ll_found_device_view);
		mListView = findViewById(R.id.lv_found_device);
		mSeekbarScanDevice = findViewById(R.id.seekbar_scan_device);
		mTvScanDevice = findViewById(R.id.tv_scan_device);
		mTv_tap_help = findViewById(R.id.tv_tap_help);
		mTv_tap_help.setOnClickListener(this);
		mKeepTheKeypadLight = findViewById(R.id.keep_the_keypad_light);
		if (HomeDeviceInfo.isDeadboltLack(mLockType)) {
			mTvScanDevice.setText(getResources().getString(R.string.scanning_nearby_deadbolt_please_wait));
		} else if (HomeDeviceInfo.isKeyBox(mLockType)) {
			mTvScanDevice.setText(getResources().getString(R.string.scanning_nearby_keybox_please_wait));
		} else if (HomeDeviceInfo.isKeyBoxK4(mLockType)) {
			mTvScanDevice.setText(getResources().getString(R.string.scanning_nearby_keybox_please_wait_k4));
		} else if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK)
				|| mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK_LOWER)) {
			mTvScanDevice.setText(getResources().getString(R.string.scanning_nearby_moonlock_please_wait));
		} else {
			mTvScanDevice.setText(getResources().getString(R.string.scanning_nearby_door_lock_please_wait));
		}
		mSeekbarScanDevice.setOnTouchListener((v, event) -> {
			// 返回true，禁止手动拖动进度值
			return true;
		});
//		if (isMHLock) {
//			mAdapter = new FoundDeviceAdapter(this,mMHLockList,mLockList,true);
//		}else {
//			mAdapter = new FoundDeviceAdapter(this,mMHLockList, mLockList,false);
//		}

		mAdapter = new FoundDeviceAdapter(FoundDeviceActivity.this, mMHTLockList, mKJXLockList);
		mListView.setOnItemClickListener(this);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU){
			registerReceiver(mReceiver, getIntentFilter(),RECEIVER_EXPORTED);
		}else {
			registerReceiver(mReceiver, getIntentFilter());
		}
		//It needs location permission to start bluetooth scan,or it can not scan device
		requestRuntimePermissions(isAndroid12() ? PERMISSION_BLE_SCAN_CONNECT
						: new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
				new PermissionListener() {
					@Override
					public void onGranted() {
//						if (isMHLock) {
//							PeachPreference.putStr(PeachPreference.KEY_LOCK_ACTION_TYPE,
//									PeachPreference.VAL_LOCK_ACTION_TYPE_INIT);
//							pplStartScan();
//						}else {
//							//开启蓝牙扫描
//							startScan();
//						}

						PeachPreference.putStr(PeachPreference.KEY_LOCK_ACTION_TYPE,
								PeachPreference.VAL_LOCK_ACTION_TYPE_INIT);
						pplStartScan();

						//开启蓝牙扫描
						startScan();
					}

					@Override
					public void onDenied(List<String> deniedPermissions) {
						toast(isAndroid12() ? R.string.note_permission_ble_scan_connect : R.string.note_permission_lbs);
					}
				});
		tvPageTitle = findViewById(R.id.page_left_title);
		tvPageTitle.setVisibility(View.VISIBLE);
		findViewById(R.id.page_title).setVisibility(View.GONE);
		setTitleText(true);
		initProgressStatus();
		initTitleBarRightBtn();
	}

	private void setTitleText(boolean isInit) {
		if (isInit) {
			mKeepTheKeypadLight.setVisibility(View.GONE);
			if (HomeDeviceInfo.isDeadboltLack(mLockType)) {
				tvPageTitle.setText(getResources().getString(R.string.scanning_nearby_deadbolt));
			} else if (HomeDeviceInfo.isKeyBox(mLockType)) {
				tvPageTitle.setText(getResources().getString(R.string.scanning_nearby_keybox));
			} else if (HomeDeviceInfo.isKeyBoxK4(mLockType)) {
				tvPageTitle.setText(getResources().getString(R.string.scanning_nearby_keybox_k4));
			} else if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK)
					|| mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK_LOWER)) {
				tvPageTitle.setText(getResources().getString(R.string.scanning_nearby_moon_lock));
			} else {
				tvPageTitle.setText(getResources().getString(R.string.scanning_nearby_door_lock));
			}
		} else {
			mKeepTheKeypadLight.setVisibility(View.VISIBLE);
			tvPageTitle.setText(getResources().getString(R.string.select_a_device));
		}
	}

	private void initProgressStatus() {
		ImageView ivStatus1 = findViewById(R.id.iv_status_1);
		ImageView ivStatus2 = findViewById(R.id.iv_status_2);
		ImageView ivStatus3 = findViewById(R.id.iv_status_3);
		View line1 = findViewById(R.id.line_1);
		View line2 = findViewById(R.id.line_2);

		ivStatus1.setImageResource(R.drawable.status_pre_check_red);
		ivStatus2.setImageResource(R.drawable.status_pre_check_red);
		ivStatus3.setImageResource(R.drawable.status_pair_red);
		line1.setBackgroundColor(getResources().getColor(R.color.edit_focus_line));
		line2.setBackgroundColor(getResources().getColor(R.color.edit_focus_line));
	}

	private void initTitleBarRightBtn() {
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mIvNewMsg = findViewById(R.id.iv_main_lock_msg_new);
		View tvSupport = findViewById(R.id.rl_main_lock_online_service);
		tvSupport.setVisibility(View.VISIBLE);
		tvSupport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startImServiceActivity(FoundDeviceActivity.this);
			}
		});
	}

	/**
	 * 获取美洽未读消息
	 */
	private void getMeiQiaUnreadMsg() {
		MQManager.getInstance(this).getUnreadMessages(new OnGetMessageListCallback() {
			@Override
			public void onSuccess(List<MQMessage> messageList) {
				PeachLogger.d(messageList);
				if (messageList != null && !messageList.isEmpty())
					mIvNewMsg.setVisibility(View.VISIBLE);
				else
					mIvNewMsg.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onFailure(int code, String message) {
			}
		});
	}

	private void upDateSeekbarScanDevice() {
		mCurrentScanProgress += 500;
		mSeekbarScanDevice.setProgress(mCurrentScanProgress);
		BaseApplication.getHandler().postDelayed(mRunnable, 500);
	}

	private void initSeekbarScanDevice() {
		mSeekbarScanDevice.setMax(SCAN_TIME_OUT_SECONDS);
		mCurrentScanProgress = 0;
		upDateSeekbarScanDevice();
	}

	private IntentFilter getIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BleConstant.ACTION_BLE_DEVICE);
		intentFilter.addAction(BleConstant.ACTION_BLE_DISCONNECTED);
		return intentFilter;
	}

	@Override
	@RequiresPermission(Manifest.permission.BLUETOOTH)
	public void onResume() {
		super.onResume();
		getMeiQiaUnreadMsg();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (isBleNetEnableWithToast()) {
			final Object obj = mAdapter.getItem(position);
			PeachLogger.d(TAG + " 点击锁头，开始初始化");
			if (isAndroid12()) {
				requestRuntimePermissions(PERMISSION_BLE_SCAN_CONNECT, new PermissionListener() {
					@Override
					public void onGranted() {
						startConnect(obj);
					}

					@Override
					public void onDenied(List<String> deniedPermissions) {
						toast(R.string.note_permission_ble_scan_connect);
					}
				});
			} else {
				startConnect(obj);
			}
			showLoading();
		}
	}

	private void startConnect(Object obj) {
		if (obj instanceof BleDevice) {//曼哈顿锁走曼哈顿SDK连接方法
			MyApplication.pplBleSession.setOperation(LockOperation.ADD_ADMIN);
			sPPLOCK.connect((BleDevice) obj);
			isMHTLock = true;
			// 添加锁时，在 LockAddSelectTypeActivity 引导页里，锁盒类型统一被初始化为 NAME_LOCK_KEY_BOX（无法区分 KJX or MHT）
			if (mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_KEY_BOX)) {
				// 此时可以判断，该锁盒为 MHT 锁盒，将锁类型赋值为 MHT 锁盒
				mLockType = HomeDeviceInfo.IDeviceName.NAME_LOCK_KEY_BOX_3;
			}
		} else {
            isMHTLock = false;
			mTTLockAPI.initLock((ExtendedBluetoothDevice) obj, new InitLockCallback() {
                @Override
                public void onInitLockSuccess(String s) {

                }

                @Override
                public void onFail(LockError lockError) {

                }
            });
		}
//				if (isMHLock) {
//					PeachLogger.d(TAG + " 点击锁头，开始初始化");
//					MyApplication.pplBleSession.setOperation(LockOperation.ADD_ADMIN);
//					sPPLOCK.connect((BleDevice) mAdapter.getItem(position));
//				}else {
//
//					PeachLogger.d(TAG + " 点击锁头，开始初始化");
//					MyApplication.bleSession.setOperation(Operation.ADD_ADMIN);
//					mTTLockAPI.connect((ExtendedBluetoothDevice) mAdapter.getItem(position));
//				}
	}

	private void startScan() {
		mTTLockAPI.startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice extendedBluetoothDevice) {

                ExtendedBluetoothDevice device = extendedBluetoothDevice;
                PeachLogger.d("device=" + device.toString());
                mAdapter.setMHTState(false);
                mAdapter.updateKJXDevice(device);
                mListView.setAdapter(mAdapter);
                BaseApplication.getHandler().removeCallbacks(mRunnable);
                ll_found_device.setVisibility(View.VISIBLE);
                mLlFoundDeviceView.setVisibility(View.GONE);
                if (DIALOG != null) {
                    DIALOG.cancel();
                }
            }

            @Override
            public void onFail(LockError lockError) {

            }
        });
		initSeekbarScanDevice();
	}

	private void stopScan() {
		mTTLockAPI.stopScanLock();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (isMHLock) {
//			pplStopScan();
//		}else {
//			stopScan();
//		}
		pplStopScan();
		stopScan();
		unregisterReceiver(mReceiver);
		if (null != DIALOG) {
			DIALOG.dismiss();
		}
	}

	@Override
	public void onEventSub(Event event) {
		super.onEventSub(event);
		switch (event.type) {
			case Event.EventType.LOCK_LOCAL_INITIALIZE_SUCCEED:
				PeachLogger.d(TAG + " onEventSub LOCK_LOCAL_INITIALIZE_SUCCEED event.obj = " + event.obj);
				if (null == event.obj) {
					//toast(R.string.note_lock_init_fail);
					AddDeviceFailActivity.actionStart(this, mLockType);
					return;
				}
				// 模拟初始化设备失败
				/*if (PeachPreference.getTestDeviceInitFail()){
					// 模拟初始化设备失败
					AddDeviceFailActivity.actionStart(this, mLockType);
					return;
				}*/
				if (isMHTLock) {
					initializeMHTLock((String) event.obj);
				} else {
					initializeKJXLock((String) event.obj);
				}
				break;
			case Event.EventType.LOCK_LOCAL_INITIALIZE_FAIL:
				PeachLogger.d(TAG + " onEventSub LOCK_LOCAL_INITIALIZE_FAIL");
				AddDeviceFailActivity.actionStart(this, mLockType);
				break;
			case Event.EventType.INIT_LOCK_FAIL_RESET_WHEN_CONNECT:
				if (isMHTLock) {
					sPPLOCK.deleteLock(mKey.getUserId(), String.valueOf(mKey.getLockId()), String.valueOf(mKey.getKeyId()), mKey.getK1());
				} else {

					mTTLockAPI.resetLock(mKey.getLockData(), mKey.getLockMac(),new ResetLockCallback() {
                        @Override
                        public void onResetLockSuccess() {
                        }

                        @Override
                        public void onFail(LockError error) {
                        }
                    });
				}
				break;
		}

	}

	/**
	 * 请求服务器，初始化锁
	 */
	private void initializeKJXLock(final String lockDataJson) {
		final WeakHashMap<String, Object> requestParams = parseLockData(lockDataJson);
		PeachLogger.d(TAG + " initializeLock 开始提交服务器 " + requestParams.toString());
		/*final CustomProgress customProgress = CustomProgress.show(this,
				getString(R.string.note_lock_init_ing), false, null);*/
		showLoading();
		RestClient.builder()
				.url(Urls.LOCK_INIT)
				.params(requestParams)
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						//customProgress.cancel();
						stopLoading();
						PeachLogger.d(TAG + " initializeLock response=" + response);

						JSONObject result = JSON.parseObject(response);
						int code = result.getInteger("code");
						if (code == 200) {
							PopulifeDBUtil.getInstance(BaseApplication.getApplication()).deleteByUserAndMac(mKey.getUserId(), mKey.getLockMac());
							PeachLogger.d(TAG + " initializeLock 提交成功 ");
							JSONObject data = result.getJSONObject("data");
							mLockId = data.getInteger("lockId");
							String lockVersion = mKey.getLockVersion();
							JSONObject lockInfo = JSON.parseObject(lockVersion);
							if (null != lockInfo) {
								lockInfo.put("lockId", mLockId);
								mKey.setLockVersion(lockInfo.toJSONString());
							}

							mKey.setLockId(mLockId);
							mBattery = (int) requestParams.get("electricQuantity");
							mHomeDevice.setDeviceId(String.valueOf(mLockId));
							mHomeDevice.setName(mLockName);
							PeachPreference.setBoolean(PeachPreference.HAVE_NEW_MESSAGE, true);
							toast(R.string.note_lock_init_success);
							EventBus.getDefault().post(new Event(Event.EventType.ADD_DEVICE_SUCCESS));
							AddDeviceSuccessActivity.actionStart(FoundDeviceActivity.this, HomeDeviceInfo.IDeviceName.NAME_LOCK_DEADBOLT, mHomeDevice, mKey);
						} else {
							PeachLogger.d(TAG + " initializeLock 提交失败 ");
							resetLock();
							toast(R.string.note_lock_init_fail);
						}
					}
				})
				.failure(new IFailure() {
					@Override
					public void onFailure() {
						PeachLogger.d(TAG + " initializeLock 提交失败 onFailure");
						resetLock();
						stopLoading();
						//customProgress.cancel();
						toast(R.string.note_lock_init_fail);
					}
				})
				.error(new IError() {
					@Override
					public void onError(int code, String msg) {
						PeachLogger.d(TAG + " initializeLock 提交失败 onError");
						resetLock();
						stopLoading();
						//customProgress.cancel();
						toast(R.string.note_lock_init_fail);
					}
				})
				.build()
				.post();
	}

	/**
	 * 曼哈顿请求服务器，初始化锁
	 */
	private void initializeMHTLock(final String lockDataJson) {
		final WeakHashMap<String, Object> requestParams = mhParseLockData(lockDataJson);
		PeachLogger.d(TAG + " initializeLock 开始提交服务器 " + requestParams.toString());
		showLoading();
		RestClient.builder()
				.url(Urls.MH_LOCK_INIT)
				.params(requestParams)
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						//customProgress.cancel();
						stopLoading();
						PeachLogger.d(TAG + " initializeLock response=" + response);
						JSONObject result = JSON.parseObject(response);
						int code = result.getInteger("code");
						if (code == 200) {
							PopulifeDBUtil.getInstance(BaseApplication.getApplication()).deleteByUserAndMac(mKey.getUserId(), mKey.getLockMac());
							PeachLogger.d(TAG + " initializeLock 提交成功 ");
							JSONObject data = result.getJSONObject("data");
							mLockId = data.getInteger("lockId");
							mKeyId = data.getInteger("keyId");
							String noKeyPwd = data.getString("noKeyPwd");
							String k1 = data.getString("k1");
							String k2 = data.getString("k2");

//							String lockVersion = mKey.getLockVersion();
//							JSONObject lockInfo = JSON.parseObject(lockVersion);
//							if (null != lockInfo) {
//								lockInfo.put("lockId", mLockId);
//								mKey.setLockVersion(lockInfo.toJSONString());
//							}

							mKey.setLockVersion("11111");
							mKey.setLockId(mLockId);
							mKey.setKeyId(mKeyId);
							mKey.setNoKeyPwd(noKeyPwd);
							mKey.setK1(k1);
							mKey.setK2(k2);
							mBattery = (int) requestParams.get("electricQuantity");
							mHomeDevice.setDeviceId(String.valueOf(mLockId));
							mHomeDevice.setName(mLockName);
							PeachPreference.setBoolean(PeachPreference.HAVE_NEW_MESSAGE, true);
							toast(R.string.note_lock_init_success);
							MyApplication.pplBleSession.setOperation(LockOperation.GET_BATTERY_LEVEL);
							EventBus.getDefault().post(new Event(Event.EventType.ADD_DEVICE_SUCCESS));
							AddDeviceSuccessActivity.actionStart(FoundDeviceActivity.this, HomeDeviceInfo.IDeviceName.NAME_LOCK_KEY_BOX_3, mHomeDevice, mKey);
						} else {
							PeachLogger.d(TAG + " initializeLock 提交失败 ");
							resetLock();
							toast(R.string.note_lock_init_fail);
						}
					}
				})
				.failure(new IFailure() {
					@Override
					public void onFailure() {
						PeachLogger.d(TAG + " initializeLock 提交失败 onFailure");
						resetLock();
						stopLoading();
						//customProgress.cancel();
						toast(R.string.note_lock_init_fail);
					}
				})
				.error(new IError() {
					@Override
					public void onError(int code, String msg) {
						PeachLogger.d(TAG + " initializeLock 提交失败 onError");
						resetLock();
						stopLoading();
						//customProgress.cancel();
						toast(R.string.note_lock_init_fail);
					}
				})
				.build()
				.post();
	}

	/**
	 * 上传失败，重置锁，数据回滚
	 */
	private void resetLock() {
		PeachLogger.d(TAG + " reset 上传失败，重置锁，数据回滚");
		MyApplication.CURRENT_KEY = mKey;
		if (isMHTLock) {
			if (sPPLOCK.isConnected(mKey.getLockMac())) {
				sPPLOCK.deleteLock(mKey.getUserId(), String.valueOf(mKey.getLockId()), String.valueOf(mKey.getKeyId()), mKey.getK1());
			} else {
				MyApplication.pplBleSession.setOperation(LockOperation.INIT_LOCK_DELETE);
				//sPPLOCK.connect(mKey.getLockMac());
				startLockActionScan();
			}
		} else {
            mTTLockAPI.resetLock(mKey.getLockData(), mKey.getLockMac(),new ResetLockCallback() {
                @Override
                public void onResetLockSuccess() {
//                    makeToast("-lock is reset and now upload to  server -");
//                    uploadResetLock2Server();
                }

                @Override
                public void onFail(LockError error) {
                    kjxRequestBleConnectPermissionStartConnect(mKey.getLockMac());
                }
            });
		}

		AddDeviceFailActivity.actionStart(this, mLockType);
	}

	/**
	 * 解析锁的数据
	 */
	private WeakHashMap<String, Object> parseLockData(String lockDataJson) {

		JSONObject lockInfo = JSON.parseObject(lockDataJson);
		final WeakHashMap<String, Object> params = new WeakHashMap<>();
		params.put("userId", PeachPreference.readUserId());
		String lockName = lockInfo.getString("lockName");

		//todo
		params.put("name", lockName);
		params.put("lockName", lockName);
		mLockName = lockName;
		params.put("mac", lockInfo.getString("lockMac"));
		params.put("key", lockInfo.getString("lockKey"));
		params.put("flagPos", lockInfo.getInteger("lockFlagPos"));
		params.put("aesKey", lockInfo.getString("aesKeyStr"));
		params.put("adminPwd", lockInfo.getString("adminPwd"));
		params.put("noKeyPwd", lockInfo.getString("noKeyPwd"));
		String deletePwd = lockInfo.getString("deletePwd");
		params.put("deletePwd", StringUtil.isBlank(deletePwd) ? "" : deletePwd);
		params.put("pwdInfo", lockInfo.getString("pwdInfo"));
		params.put("timestamp", lockInfo.getString("timestamp"));
		params.put("specialValue", lockInfo.getInteger("specialValue"));
		params.put("electricQuantity", lockInfo.getInteger("electricQuantity"));
		params.put("timezoneRawOffSet", String.valueOf(lockInfo.getInteger("timezoneRawOffset")));
		params.put("modelNum", lockInfo.getString("modelNum"));
		params.put("hardwareRevision", lockInfo.getString("hardwareRevision"));
		params.put("firmwareRevision", lockInfo.getString("firmwareRevision"));
		JSONObject lockVersion = lockInfo.getJSONObject("lockVersion");
		params.put("protocolType", lockVersion.getInteger("protocolType"));
		params.put("protocolVersion", lockVersion.getInteger("protocolVersion"));
		params.put("scene", lockVersion.getInteger("scene"));
		params.put("groupId", lockVersion.getInteger("groupId"));
		params.put("orgId", lockVersion.getInteger("orgId"));


		// 这些数据用于重置锁
		mKey = new Key();
		lockVersion.put("lockId", lockInfo.getInteger("lockId"));
		lockVersion.put("showAdminKbpwdFlag", null);
		lockVersion.put("showAdminKbpwdFlag", null);
		mKey.setLockVersion(lockVersion.toJSONString());
		mKey.setAdminPwd(lockInfo.getString("adminPwd"));
		if (lockInfo.containsKey("noKeyPwd")) {
			mKey.setNoKeyPwd(lockInfo.getString("noKeyPwd"));
		}
		mKey.setLockKey(lockInfo.getString("lockKey"));
		mKey.setLockFlagPos(lockInfo.getInteger("lockFlagPos"));
		mKey.setAesKeyStr(lockInfo.getString("aesKeyStr"));
        mKey.setLockMac(lockInfo.getString("lockMac"));
        mKey.setLockData(lockInfo.getString("lockData"));
		mKey.setUserId(PeachPreference.readUserId());
		if (lockInfo.containsKey("keyId")) {
			mKey.setKeyId(lockInfo.getInteger("keyId"));
		}
		if (lockInfo.containsKey("userKeyId")) {
			mKey.setUserKeyId(lockInfo.getInteger("userKeyId"));
		}

		return params;
	}

	/**
	 * 解析锁的数据
	 */
	private WeakHashMap<String, Object> mhParseLockData(String lockDataJson) {

		JSONObject lockInfo = JSON.parseObject(lockDataJson);
		final WeakHashMap<String, Object> params = new WeakHashMap<>();
		params.put("userId", PeachPreference.readUserId());
		String lockName = lockInfo.getString("lockName");

		//todo
		params.put("name", lockName);
		params.put("lockName", lockName);
		mLockName = lockName;
		params.put("alias", lockName);
		params.put("mac", lockInfo.getString("lockMac"));
		//params.put("key", lockInfo.getString("lockKey"));
		//params.put("flagPos", lockInfo.getInteger("lockFlagPos"));
		params.put("aesKey", lockInfo.getString("aesKey"));
		params.put("timestamp", lockInfo.getString("timestamp"));
		params.put("specialValue", 1230);
		params.put("electricQuantity", lockInfo.getInteger("batteryLevel"));//batteryLevel,electricQuantity
		params.put("timezoneRawOffSet", String.valueOf(lockInfo.getInteger("timezoneRawOffset")));
		params.put("modelNum", lockInfo.getString("modelNum"));
		mHomeDevice.setAlias(lockName);
		mHomeDevice.setModelNum(lockInfo.getString("modelNum"));
		params.put("hardwareRevision", lockInfo.getString("hardwareVersion"));
		params.put("firmwareRevision", lockInfo.getString("firmwareVersion"));//firmwareVersion -> 1.0.0.1108
		params.put("protocolVersion", 1);


		// 这些数据用于重置锁
		mKey = new Key();
//		lockVersion.put("lockId", lockInfo.getInteger("lockId"));
//		lockVersion.put("showAdminKbpwdFlag", null);
//		lockVersion.put("showAdminKbpwdFlag", null);
//		mKey.setLockVersion(lockVersion.toJSONString());
//		mKey.setAdminPwd(lockInfo.getString("adminPwd"));
		mKey.setLockVersion("11111");
		mKey.setAdminPwd("111111");
		if (lockInfo.containsKey("noKeyPwd")) {
			mKey.setNoKeyPwd(lockInfo.getString("noKeyPwd"));
		}
		mKey.setLockKey(lockInfo.getString("aesKey"));
		//mKey.setLockFlagPos(lockInfo.getInteger("lockFlagPos"));
		mKey.setLockFlagPos(1);
		mKey.setAesKeyStr(lockInfo.getString("aesKey"));
		mKey.setLockMac(lockInfo.getString("lockMac"));
        mKey.setLockData(lockInfo.getString("lockData"));
		mKey.setUserId(PeachPreference.readUserId());
		if (lockInfo.containsKey("keyId")) {
			mKey.setKeyId(lockInfo.getInteger("keyId"));
		}
		if (lockInfo.containsKey("userKeyId")) {
			mKey.setUserKeyId(lockInfo.getInteger("userKeyId"));
		}
		mKey.setUserKeyId(2222);
		return params;
	}

	private void getIntentData() {
		mLockType = getIntent().getStringExtra(KEY_LOCK_TYPE);
		if ((mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_MANHATTAN_MOON_LOCK)
				|| mLockType.startsWith(HomeDeviceInfo.IDeviceName.NAME_LOCK_KEY_BOX_3))) {
			isMHTLock = true;
		}
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_tap_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
    }


    /**
	 * 开始扫描
	 */
	private void pplStartScan() {
		sPPLOCK.startScan();
		initSeekbarScanDevice();
	}

	/**
	 * 停止扫描
	 */
	private void pplStopScan() {
		sPPLOCK.stopScan();
	}

}
