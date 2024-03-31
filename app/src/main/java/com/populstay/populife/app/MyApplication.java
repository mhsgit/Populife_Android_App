package com.populstay.populife.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.populock.manhattan.sdk.BleDevice;
import com.populock.manhattan.sdk.api.PPLock;
import com.populock.manhattan.sdk.callback.PPLockCallback;
import com.populock.manhattan.sdk.constant.LockOperation;
import com.populock.manhattan.sdk.entity.LockError;
import com.populstay.populife.base.BaseApplication;
import com.populstay.populife.constant.BleConstant;
import com.populstay.populife.entity.BleSession;
import com.populstay.populife.entity.Key;
import com.populstay.populife.entity.PPLBleSession;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.eventbus.Event;
import com.populstay.populife.ui.loader.PeachLoader;
import com.populstay.populife.util.date.DateUtil;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;
import com.populstay.populife.util.string.StringUtil;
import com.ttlock.bl.sdk.api.TTLockAPI;
import com.ttlock.bl.sdk.callback.TTLockCallback;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.Error;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.scanner.ExtendedBluetoothDevice;

import org.greenrobot.eventbus.EventBus;

import java.util.TimeZone;

import androidx.multidex.MultiDex;
import cn.ittiger.player.Config;
import cn.ittiger.player.PlayerManager;
import cn.ittiger.player.factory.MediaPlayerFactory;

/**
 * Created by Jerry
 */

public class MyApplication extends BaseApplication {

	public static final String TAG = "MyApplication";

	/**
	 * bluetooth operation
	 */
	public static BleSession bleSession = BleSession.getInstance(Operation.UNLOCK, null);
	/**
	 * TTLockAPI
	 */
	@SuppressLint("StaticFieldLeak")
	public static TTLockAPI mTTLockAPI;
	/**
	 * current used key
	 */
	public static Key CURRENT_KEY = new Key(); // 全局当前正在使用的 key
	/**
	 * bluetooth operation
	 */
	public static PPLBleSession pplBleSession = PPLBleSession.getInstance(LockOperation.GET_BATTERY_LEVEL, null);
	/**
	 * PPLock instance
	 */
	@SuppressLint("StaticFieldLeak")
	public static PPLock sPPLOCK;
	private Activity curActivity;
	/**
	 * Instantiate TTLockCallback Object
	 */
	private TTLockCallback mTTLockCallback = new TTLockCallback() {
		@Override
		public void onFoundDevice(ExtendedBluetoothDevice extendedBluetoothDevice) {
			//found device and broadcast
			broadcastUpdate(BleConstant.ACTION_BLE_DEVICE, BleConstant.DEVICE, extendedBluetoothDevice);
			//todo 读取本地数据
//			Key localKey = DbService.getKeyByLockmac(extendedBluetoothDevice.getAddress());
//			if (extendedBluetoothDevice.getAddress().equals(bleSession.getLockmac())) {
//				mTTLockAPI.connect(extendedBluetoothDevice);
//			}
		}

		@Override
		public void onDeviceConnected(ExtendedBluetoothDevice extendedBluetoothDevice) {
			//获取本地锁信息

			//uid equal to openid
			int uid = PeachPreference.getOpenid();
			switch (bleSession.getOperation()) {
				case ADD_ADMIN:
					//todo 判断要添加的锁是否已存在?
					PeachLogger.d(TAG + " 添加锁头=" + extendedBluetoothDevice.getName());
					mTTLockAPI.lockInitialize(extendedBluetoothDevice);
					break;

				case UNLOCK:
				case CLICK_UNLOCK:
					if (CURRENT_KEY != null) {
						if (bleSession.isAdmin())
							mTTLockAPI.unlockByAdministrator(extendedBluetoothDevice, uid, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), DateUtil.getCurTimeMillis(), CURRENT_KEY.getAesKeyStr(), CURRENT_KEY.getTimezoneRawOffset());
						else
							mTTLockAPI.unlockByUser(extendedBluetoothDevice, uid, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getStartDate(), CURRENT_KEY.getEndDate(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr(), CURRENT_KEY.getTimezoneRawOffset());
					}
					break;

				case LOCK:
					if (CURRENT_KEY != null) {
						mTTLockAPI.lock(extendedBluetoothDevice, uid, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getStartDate(), CURRENT_KEY.getEndDate(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), DateUtil.getCurTimeMillis(), CURRENT_KEY.getAesKeyStr(), CURRENT_KEY.getTimezoneRawOffset());
					}
					break;

				case SET_ADMIN_KEYBOARD_PASSWORD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.setAdminKeyboardPassword(extendedBluetoothDevice, uid, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr(), bleSession.getPassword());
					}
					break;

				case GET_LOCK_TIME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.getLockTime(extendedBluetoothDevice, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAesKeyStr(), CURRENT_KEY.getTimezoneRawOffset());
					}
					break;

				case SET_LOCK_TIME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.setLockTime(extendedBluetoothDevice, PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(), CURRENT_KEY.getLockKey(), DateUtil.getCurTimeMillis(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr(), CURRENT_KEY.getTimezoneRawOffset());
					}
					break;

				case SEARCH_AUTO_LOCK_TIME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.searchAutoLockTime(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case MODIFY_AUTO_LOCK_TIME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.modifyAutoLockTime(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getAutoLockTime(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case RESET_LOCK:
					PeachLogger.d(TAG + " onDeviceConnected 重置锁 RESET_LOCK");
					if (CURRENT_KEY != null) {
						mTTLockAPI.resetLock(extendedBluetoothDevice, uid, CURRENT_KEY.getLockVersion(),
								CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;
				case RESET_LOCK_FOR_INIT_LOCK_FAIL:
					PeachLogger.d(TAG + " onDeviceConnected 重置锁 RESET_LOCK_FOR_INIT_LOCK_FAIL");
					EventBus.getDefault().post(new Event(Event.EventType.INIT_LOCK_FAIL_RESET_WHEN_CONNECT));
					break;

				case RESET_KEYBOARD_PASSWORD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.resetKeyboardPassword(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case RESET_EKEY:
					if (CURRENT_KEY != null) {
						mTTLockAPI.resetEKey(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case ADD_PASSCODE:
					if (CURRENT_KEY != null) {
						mTTLockAPI.addPeriodKeyboardPassword(extendedBluetoothDevice,
								PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(),
								CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), bleSession.getPassword(),
								bleSession.getStartDate(), bleSession.getEndDate(), CURRENT_KEY.getAesKeyStr(),
								(long) TimeZone.getDefault().getOffset(DateUtil.getCurTimeMillis()));
					}
					break;

				case MODIFY_KEYBOARD_PASSWORD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.modifyKeyboardPassword(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getKeyboardPwdType(),
								bleSession.getKeyboardPwdOriginal(), bleSession.getKeyboardPwdNew(),
								bleSession.getStartDate(), bleSession.getEndDate(),
								CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				case DELETE_ONE_KEYBOARDPASSWORD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.deleteOneKeyboardPassword(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getKeyboardPwdType(),
								bleSession.getKeyboardPwdOriginal(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case REMOTE_UNLOCK_SWITCH:
					if (CURRENT_KEY != null) {
						mTTLockAPI.operateRemoteUnlockSwitch(extendedBluetoothDevice, 2, bleSession.getRemoteUnlockState(),
								PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(),
								CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case ADD_IC_CARD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.addICCard(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(),
								CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(),
								CURRENT_KEY.getAesKeyStr());
					}
					break;

				case MODIFY_IC_CARD_PERIOD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.modifyICPeriod(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getIcCardNumber(), bleSession.getStartDate(),
								bleSession.getEndDate(), CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				case DELETE_IC_CARD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.deleteICCard(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getIcCardNumber(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case CLEAR_IC_CARDS:
					if (CURRENT_KEY != null) {
						mTTLockAPI.clearICCard(extendedBluetoothDevice, PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(),
								CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(),
								CURRENT_KEY.getAesKeyStr());
					}
					break;

				case SEARCH_IC_CARDS:
					if (CURRENT_KEY != null) {
						mTTLockAPI.searchICCard(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				case GET_LOCK_BATTERY:
					if (CURRENT_KEY != null) {
						mTTLockAPI.getElectricQuantity(null, CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case QUERY_KEYPAD_VOLUME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.operateAudioSwitch(extendedBluetoothDevice, 1, 0,
								PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(),
								CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case MODIFY_KEYPAD_VOLUME:
					if (CURRENT_KEY != null) {
						mTTLockAPI.operateAudioSwitch(extendedBluetoothDevice, 2, bleSession.getKeypadVolumeState(),
								PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(),
								CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case GET_OPERATE_LOG:
					if (CURRENT_KEY != null) {
						mTTLockAPI.getOperateLog(extendedBluetoothDevice, CURRENT_KEY.getLockVersion(),
								CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				case FINGERPRINT_ADD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.addFingerPrint(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(),
								CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(),
								CURRENT_KEY.getAesKeyStr());
					}
					break;

				case FINGERPRINT_DELETE:
					if (CURRENT_KEY != null) {
						mTTLockAPI.deleteFingerPrint(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getFingerprintNumber(), CURRENT_KEY.getAesKeyStr());
					}
					break;

				case FINGERPRINT_MODIFY_PERIOD:
					if (CURRENT_KEY != null) {
						mTTLockAPI.modifyFingerPrintPeriod(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), bleSession.getFingerprintNumber(), bleSession.getStartDate(),
								bleSession.getEndDate(), CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				case FINGERPRINT_CLEAR:
					if (CURRENT_KEY != null) {
						mTTLockAPI.clearFingerPrint(extendedBluetoothDevice, PeachPreference.getOpenid(), CURRENT_KEY.getLockVersion(),
								CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(), CURRENT_KEY.getLockFlagPos(),
								CURRENT_KEY.getAesKeyStr());
					}
					break;

				case SEARCH_FINGERPRINTS:
					if (CURRENT_KEY != null) {
						mTTLockAPI.searchFingerPrint(extendedBluetoothDevice, PeachPreference.getOpenid(),
								CURRENT_KEY.getLockVersion(), CURRENT_KEY.getAdminPwd(), CURRENT_KEY.getLockKey(),
								CURRENT_KEY.getLockFlagPos(), CURRENT_KEY.getAesKeyStr(), DateUtil.getTimeZoneOffset());
					}
					break;

				default:
					break;
			}
		}

		@Override
		public void onDeviceDisconnected(ExtendedBluetoothDevice extendedBluetoothDevice) {
			PeachLoader.stopLoading();
			switch (bleSession.getOperation()) {
				case UNLOCK:
				case CLICK_UNLOCK:
					bleSession.getILockUnlock().onUnlockFinish();
					break;

				case LOCK:
					bleSession.getILockLock().onLockFinish();
					break;

				case RESET_LOCK:
					PeachLogger.d(TAG + " onDeviceDisconnected 重置锁 RESET_LOCK");
					bleSession.getILockResetLock().onFinish();
					break;

				case RESET_LOCK_FOR_INIT_LOCK_FAIL:
					PeachLogger.d(TAG + " onDeviceDisconnected 重置锁 RESET_LOCK_FOR_INIT_LOCK_FAIL");
					bleSession.getILockResetLock().onFinish();
					break;

				case ADD_PASSCODE:
					bleSession.getILockAddPasscode().onTimeOut();
					break;

				case DELETE_ONE_KEYBOARDPASSWORD:
					bleSession.getILockDeletePasscode().onTimeOut();
					break;

				case MODIFY_KEYBOARD_PASSWORD:
					bleSession.getILockModifyPasscode().onTimeOut();
					break;

				case MODIFY_IC_CARD_PERIOD:
					bleSession.getILockIcCardModifyPeriod().onTimeOut();
					break;

				case DELETE_IC_CARD:
					bleSession.getILockIcCardDelete().onTimeOut();
					break;

				case FINGERPRINT_ADD:
					bleSession.getILockFingerprintAdd().onDeviceDisconnected();
					break;

				case FINGERPRINT_MODIFY_PERIOD:
					bleSession.getILockFingerprintModifyPeriod().onTimeOut();
					break;

				case FINGERPRINT_DELETE:
					bleSession.getILockFingerprintDelete().onTimeOut();
					break;

				case GET_LOCK_TIME:
					bleSession.getILockGetTime().onTimeOut();
					break;

				case SET_LOCK_TIME:
					bleSession.getILockSetTime().onTimeOut();
					break;

				default:
					break;
			}
		}

		@Override
		public void onGetLockVersion(ExtendedBluetoothDevice extendedBluetoothDevice, int protocolType, int protocolVersion, int scene, int groupId, int orgId, Error error) {

		}

		@Override
		public void onLockInitialize(ExtendedBluetoothDevice extendedBluetoothDevice, LockData lockData, Error error) {

			PeachLogger.d(TAG + " onLockInitialize error = " + error.getErrorMsg() + ",extendedBluetoothDevice=" + ",extendedBluetoothDevice=" + extendedBluetoothDevice.toString() + ";\r\n onLockInitialize lockData=" + (lockData != null ? lockData.toString() : "null"));

			PeachLoader.stopLoading();
			if (error == Error.INVALID_VENDOR) {
				//myToast(R.string.lock_not_supported);
				EventBus.getDefault().post(new Event(Event.EventType.LOCK_LOCAL_INITIALIZE_FAIL));
			} else if (error == Error.SUCCESS) {
				mTTLockAPI.stopBTDeviceScan();
				String lockDataJson = lockData.toJson();
				/*Intent intent = new Intent(getApplication(), LockNameAddActivity.class);
				intent.putExtra(LockNameAddActivity.KEY_LOCK_INIT_DATA, lockDataJson);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);*/
				//AddDeviceSuccessActivity.actionStart(getApplication(), HomeDeviceInfo.IDeviceModel.MODEL_LOCK_DEADBOLT);
				EventBus.getDefault().post(new Event(Event.EventType.LOCK_LOCAL_INITIALIZE_SUCCEED, lockDataJson));


			} else {//failure
				EventBus.getDefault().post(new Event(Event.EventType.LOCK_LOCAL_INITIALIZE_FAIL));
				//myToast(R.string.note_lock_init_fail);
			}
		}

		@Override
		public void onResetEKey(ExtendedBluetoothDevice extendedBluetoothDevice, int lockFlagPos, Error error) {
			PeachLogger.d(TAG + " onResetEKey error=" + error.getErrorMsg());
			if (error == Error.SUCCESS) {
				bleSession.getILockResetEkey().onSuccess();
			} else {
				bleSession.getILockResetEkey().onFail();
			}
		}

		@Override
		public void onSetLockName(ExtendedBluetoothDevice extendedBluetoothDevice, String lockname, Error error) {

		}

		@Override
		public void onSetAdminKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, String adminCode, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockSetAdminKeyboardPwd().onSetPwdSuccess();
			} else {
				bleSession.getILockSetAdminKeyboardPwd().onSetPwdFail();
			}
		}

		@Override
		public void onSetDeletePassword(ExtendedBluetoothDevice extendedBluetoothDevice, String deleteCode, Error error) {

		}

		@Override
		public void onUnlock(ExtendedBluetoothDevice extendedBluetoothDevice, int uid, int uniqueid, long lockTime, Error error) {
			if (error == Error.SUCCESS) {
				//开锁成功
				int batteryCapacity = extendedBluetoothDevice.getBatteryCapacity();
				bleSession.getILockUnlock().onUnlockSuccess(batteryCapacity);
			} else {
				bleSession.getILockUnlock().onUnlockFail();
			}
		}

		@Override
		public void onSetLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockSetTime().onSuccess();
			} else {
				bleSession.getILockSetTime().onFail();
			}
		}

		@Override
		public void onGetLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, long lockTime, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockGetTime().onSuccess(lockTime);
			} else {
				bleSession.getILockGetTime().onFail();
			}
		}

		@Override
		public void onResetKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, String pwdInfo, long timestamp, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockResetKeyboardPwd().onSuccess(pwdInfo, timestamp);
			} else {
				bleSession.getILockResetKeyboardPwd().onFail();
			}
		}

		@Override
		public void onSetMaxNumberOfKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int validPwdNum, Error error) {

		}

		@Override
		public void onResetKeyboardPasswordProgress(ExtendedBluetoothDevice extendedBluetoothDevice, int progress, Error error) {

		}

		@Override
		public void onResetLock(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {

			PeachLogger.d(TAG + " onResetLock error= " + error.getErrorMsg());

			if (error == Error.SUCCESS) {
				bleSession.getILockResetLock().onSuccess();
			} else {
				bleSession.getILockResetLock().onFail();
			}
		}

		@Override
		public void onAddKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int keyboardPwdType, String password, long startDate, long endDate, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockAddPasscode().onSuccess();
			} else {
				bleSession.getILockAddPasscode().onFail();
			}
		}

		@Override
		public void onModifyKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int keyboardPwdType, String originPwd, String newPwd, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockModifyPasscode().onSuccess();
			} else {
				bleSession.getILockModifyPasscode().onFail(error);
			}
		}

		@Override
		public void onDeleteOneKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int keyboardPwdType, String deletedPwd, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockDeletePasscode().onSuccess();
			} else {
				bleSession.getILockDeletePasscode().onFail();
			}
		}

		@Override
		public void onDeleteAllKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {

		}

		@Override
		public void onGetOperateLog(ExtendedBluetoothDevice extendedBluetoothDevice, String records, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockGetOperateLog().onSuccess(records);
			} else {
				bleSession.getILockGetOperateLog().onFail();
			}
		}

		@Override
		public void onSearchDeviceFeature(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int specialValue, Error error) {

		}

		@Override
		public void onAddICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int battery, long cardNumber, Error error) {
			if (error == Error.SUCCESS) {
				if (status == 1) {
					bleSession.getILockIcCardAdd().onEnterAddMode();
				} else if (status == 2)
					if (cardNumber != 0) {
						bleSession.getILockIcCardAdd().onSuccess(cardNumber);
					} else {
						bleSession.getILockIcCardAdd().onFail();
					}
			} else {
				bleSession.getILockIcCardAdd().onFail();
			}
		}

		@Override
		public void onModifyICCardPeriod(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long cardNumber, long startDate, long endDate, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockIcCardModifyPeriod().onSuccess();
			} else {
				bleSession.getILockIcCardModifyPeriod().onFail();
			}
		}

		@Override
		public void onDeleteICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long cardNumber, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockIcCardDelete().onSuccess();
			} else {
				bleSession.getILockIcCardDelete().onFail(error);
			}
		}

		@Override
		public void onClearICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockIcCardClear().onSuccess();
			} else {
				bleSession.getILockIcCardClear().onFail(error);
			}
		}

		@Override
		public void onSetWristbandKeyToLock(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {

		}

		@Override
		public void onSetWristbandKeyToDev(Error error) {

		}

		@Override
		public void onSetWristbandKeyRssi(Error error) {

		}

		@Override
		public void onAddFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int battery, long fingerPrintNo, Error error) {

		}

		/**
		 * the callback of addFingerPrint
		 * @param extendedBluetoothDevice device object
		 * @param status 1: entry into add mode; 2: add success
		 * @param battery device battery
		 * @param fingerPrintNo fingerrpint number
		 * @param totalCount the total count(-1: unknown，旧版锁)
		 * @param error errorCode
		 */
		@Override
		public void onAddFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int status, int battery, long fingerPrintNo, int totalCount, Error error) {
			if (error == Error.SUCCESS) {
				if (status == 1) {
					bleSession.getILockFingerprintAdd().onEnterAddMode(totalCount);
				} else if (status == 2) {
					if (fingerPrintNo != 0) {
						bleSession.getILockFingerprintAdd().onAddSuccess(fingerPrintNo, totalCount);
					} else {
						bleSession.getILockFingerprintAdd().onFail();
					}
				}
			} else {
				bleSession.getILockFingerprintAdd().onFail();
			}
		}

		@Override
		public void onFingerPrintCollection(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {

		}

		/**
		 * fingerprint collection callback
		 * @param extendedBluetoothDevice device object
		 * @param battery device battery
		 * @param currentCount the current count(-1: uknown，旧版锁)
		 * @param totalCount the total count(-1: uknown，旧版锁)
		 * @param error errorCode
		 */
		@Override
		public void onFingerPrintCollection(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int currentCount, int totalCount, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockFingerprintAdd().onCollectSuccess(currentCount, totalCount);
			} else {
				bleSession.getILockFingerprintAdd().onFail();
			}
		}

		/**
		 * the callback of modifyFingrPrintPeriod
		 * @param extendedBluetoothDevice device object
		 * @param battery device battery
		 * @param fingerPrintNo fingerrpint number
		 * @param startDate the time when it becomes valid
		 * @param endDate the time when it is expired
		 * @param error errorCode
		 */
		@Override
		public void onModifyFingerPrintPeriod(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long fingerPrintNo, long startDate, long endDate, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockFingerprintModifyPeriod().onSuccess();
			} else {
				bleSession.getILockFingerprintModifyPeriod().onFail();
			}
		}

		/**
		 * the callback of deleteFingerPrint
		 * @param extendedBluetoothDevice device object
		 * @param battery device battery
		 * @param fingerPrintNo fingerrpint number
		 * @param error errorCode
		 */
		@Override
		public void onDeleteFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, long fingerPrintNo, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockFingerprintDelete().onSuccess();
			} else {
				bleSession.getILockFingerprintDelete().onFail(error);
			}
		}

		@Override
		public void onClearFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockIcCardClear().onSuccess();
			} else {
				bleSession.getILockIcCardClear().onFail(error);
			}
		}

		/**
		 * @param extendedBluetoothDevice
		 * @param battery
		 * @param currentTime 当前自动闭锁时间（0为“不自动闭锁”）
		 * @param minTime 可设置的最短自动闭锁时间
		 * @param maxTime 可设置的最长自动闭锁时间
		 * @param error
		 */
		@Override
		public void onSearchAutoLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int currentTime, int minTime, int maxTime, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockSearchAutoLockTime().onSearchAutoLockTimeSuccess(currentTime);
			} else {
				bleSession.getILockSearchAutoLockTime().onSearchAutoLockTimeFail();
			}
		}

		@Override
		public void onModifyAutoLockTime(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int autoLockTime, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockModifyAutoLockTime().onModifyAutoLockTimeSuccess();
			} else {
				bleSession.getILockModifyAutoLockTime().onModifyAutoLockTimeFail();
			}
		}

		@Override
		public void onReadDeviceInfo(ExtendedBluetoothDevice extendedBluetoothDevice, DeviceInfo deviceInfo, Error error) {

		}

		@Override
		public void onEnterDFUMode(ExtendedBluetoothDevice extendedBluetoothDevice, Error error) {

		}

		@Override
		public void onGetLockSwitchState(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int status, Error error) {

		}

		@Override
		public void onLock(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int uid, int uniqueid, long lockTime, Error error) {
			if (error == Error.SUCCESS) {
				//闭锁成功
				int batteryCapacity = extendedBluetoothDevice.getBatteryCapacity();
				bleSession.getILockLock().onLockSuccess(batteryCapacity);
			} else {
				//闭锁失败
				bleSession.getILockLock().onLockFail();
			}
		}

		@Override
		public void onScreenPasscodeOperate(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int status, Error error) {

		}

		@Override
		public void onRecoveryData(ExtendedBluetoothDevice extendedBluetoothDevice, int op, Error error) {

		}

		@Override
		public void onSearchICCard(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, String json, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockIcCardSearch().onSuccess(json);
			} else {
				bleSession.getILockIcCardSearch().onFail(error);
			}
		}

		@Override
		public void onSearchFingerPrint(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, String json, Error error) {
			if (error == Error.SUCCESS) {
				bleSession.getILockFingerprintSearch().onSuccess(json);
			} else {
				bleSession.getILockFingerprintSearch().onFail(error);
			}
		}

		@Override
		public void onSearchPasscode(ExtendedBluetoothDevice extendedBluetoothDevice, String json, Error error) {

		}

		@Override
		public void onSearchPasscodeParam(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, String pwdInfo, long timestamp, Error error) {

		}

		/**
		 * @param operateType 操作类型（1 get获取、2 modify修改）
		 * @param state  远程开锁开关状态（1 on 打开、0 off关闭）
		 * @param specialValue 设备特征值
		 */
		@Override
		public void onOperateRemoteUnlockSwitch(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int operateType, int state, int specialValue, Error error) {
			if (error == Error.SUCCESS) {
				//修改远程开锁状态成功
				bleSession.getILockModifyRemoteUnlockState().onSuccess(battery, operateType, state, specialValue);
			} else {
				//修改失败
				bleSession.getILockModifyRemoteUnlockState().onFail(error);
			}
		}

		@Override
		public void onGetElectricQuantity(ExtendedBluetoothDevice extendedBluetoothDevice, int electricQuantity, Error error) {
			if (error == Error.SUCCESS) {
				//读取锁电量成功
				bleSession.getILockGetBattery().onGetBatterySuccess(electricQuantity);
			} else {
				bleSession.getILockGetBattery().onGetBatteryFail();
			}
		}

		@Override
		public void onOperateAudioSwitch(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int operateType, int state, Error error) {
			if (error == Error.SUCCESS) {
				if (operateType == 1) // 查询
					bleSession.getILockQueryKeypadVolume().onSuccess(state);
				else if (operateType == 2) // 修改
					bleSession.getILockModifyKeypadVolume().onSuccess(state);
			} else {
				if (operateType == 1) // 查询
					bleSession.getILockQueryKeypadVolume().onFail();
				else if (operateType == 2) // 修改
					bleSession.getILockModifyKeypadVolume().onFail();
			}
		}

		@Override
		public void onOperateRemoteControl(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int operateType, int keyValue, Error error) {

		}

		@Override
		public void onOperateDoorSensorLocking(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int operationType, int operationValue, Error error) {

		}

		@Override
		public void onGetDoorSensorState(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, int state, Error error) {

		}

		@Override
		public void onSetNBServer(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, Error error) {

		}

		@Override
		public void onGetAdminKeyboardPassword(ExtendedBluetoothDevice extendedBluetoothDevice, int battery, String adminCode, Error error) {

		}
	};
	/**
	 * PPLockCallback
	 */
	private PPLockCallback mPPLockCallback = new PPLockCallback() {
		@Override
		public void onFoundDevice(BleDevice device) {
			Logger.t("PPLock").e("onFoundDevice");
			String actionType = PeachPreference.getStr(PeachPreference.KEY_LOCK_ACTION_TYPE);
			if (PeachPreference.VAL_LOCK_ACTION_TYPE_INIT.equals(actionType)) {
				Logger.t("PPLock").e("onFoundDevice1");
				// 初始化绑定锁：扫描到设备后，广播已扫描到的设备
				broadcastUpdate(BleConstant.ACTION_BLE_DEVICE, BleConstant.DEVICE, device);
			} else { // 常规操作：判断进行连接
				final String mac = CURRENT_KEY.getLockMac();
				Logger.t("PPLock").e("onFoundDevice2 mac = " + mac +",device.getAddress() = " + device.getAddress());
				if (!StringUtil.isBlank(mac) && mac.equalsIgnoreCase(device.getAddress())) {
					Logger.t("PPLock").e("onFoundDevice3");
					sPPLOCK.stopScan();
					getHandler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Logger.t("PPLock").e("onFoundDevice45");
							sPPLOCK.connect(mac);
						}
					}, 500);
				}
			}
		}

		@Override
		public void onDeviceConnected(BleDevice device) {
			Log.e("", "connected");
			switch (pplBleSession.getOperation()) {
				case ADD_ADMIN:
					sPPLOCK.initLock(device, PeachPreference.readUserId());
					PeachLogger.e("aaa", "连接成功");
					break;

				case ADMIN_UNLOCK:
					if (CURRENT_KEY != null)
//						if (bleSession.isAdmin())
						Logger.t(BaseApplication.TAG).e("操作unlock sPPLOCK ADMIN_UNLOCK");
						sPPLOCK.adminUnlock(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
//						else
//							sPPLOCK.userUnlock(sCURRENT_KEY.getUserId(), sCURRENT_KEY.getLockId(), sCURRENT_KEY.getKeyId(),
//									sCURRENT_KEY.getKeyType(), sCURRENT_KEY.getStartDate(), sCURRENT_KEY.getEndDate(),
//									StringUtil.isBlank(sCURRENT_KEY.getStartTime()) ? -1 : Integer.parseInt(sCURRENT_KEY.getStartTime().substring(0, 2)),
//									StringUtil.isBlank(sCURRENT_KEY.getEndTime()) ? -1 : Integer.parseInt(sCURRENT_KEY.getEndTime().substring(0, 2)),
//									sCURRENT_KEY.getSendKeyDate());
					break;

				case ADMIN_LOCK:
					if (CURRENT_KEY != null)
//						if (bleSession.isAdmin())
						sPPLOCK.adminLock(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
//						else
//							sPPLOCK.userLock(sCURRENT_KEY.getUserId(), sCURRENT_KEY.getLockId(), sCURRENT_KEY.getKeyId());
					break;

				case DELETE_LOCK:
				case INIT_LOCK_DELETE:
					if (CURRENT_KEY != null)
						sPPLOCK.deleteLock(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;

				case GET_BATTERY_LEVEL:
					sPPLOCK.getBatteryLevel(0);
					break;

				case GET_LOCK_TIME:
					sPPLOCK.getLockTime();
					break;

				case SET_LOCK_TIME:
					if (CURRENT_KEY != null)
						sPPLOCK.setLockTime(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), DateUtil.getCurTimeMillis(), CURRENT_KEY.getK1());
					break;

				case GET_OPERATE_LOG:
					if (CURRENT_KEY != null)
						sPPLOCK.getOperateLog(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1(), CURRENT_KEY.getLockName());
					break;

				case GET_AUTO_LOCK_TIME:
					sPPLOCK.getAutoLockTime();
					break;

				case GET_LOCK_STATUS:
					sPPLOCK.getLockStatus();
					break;

				case SET_AUTO_LOCK_TIME:
					if (CURRENT_KEY != null)
						sPPLOCK.setAutoLocking(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getAutoLockTime(), CURRENT_KEY.getK1());
					break;
				case MODIFY_KEYBOARD_PWD:
					if (CURRENT_KEY != null)
						sPPLOCK.modifyUserKeyboardPwd(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()),
								pplBleSession.getKeyboardPwdOriginal(),
								pplBleSession.getKeyboardPwdNew(), pplBleSession.getKeyboardPwdType(), pplBleSession.getStartDate(),
								pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;
				case MODIFY_KEYBOARD_PWD_VALID:
					if (CURRENT_KEY != null)
						sPPLOCK.modifyUserKeyboardPwdPeriod(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()),
								pplBleSession.getKeyboardPwdOriginal(), pplBleSession.getKeyboardPwdType(), pplBleSession.getStartDate(),
								pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;

				case SET_ADMIN_KEYBOARD_PWD:
					if (CURRENT_KEY != null)
						sPPLOCK.setAdminKeyboardPwd(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getNoKeyPwd(), pplBleSession.getPassword(), CURRENT_KEY.getK1());
					break;

				case DELETE_KEYBOARD_PWD:
					if (CURRENT_KEY != null)
						sPPLOCK.deleteKeyboardPwd(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getKeyboardPwdType(), pplBleSession.getKeyboardPwdOriginal(), CURRENT_KEY.getK1());
					break;

				case RESET_EKEY:
					if (CURRENT_KEY != null)
						sPPLOCK.resetEKey(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;

				case RESET_KEYBOARD_PWD:
					if (CURRENT_KEY != null)
						sPPLOCK.resetKeyboardPwd(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;

				case ADD_KEYBOARD_PWD:
					if (CURRENT_KEY != null)
						sPPLOCK.addKeyboardPwd(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getPassword(), pplBleSession.getStartDate(), pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;

				case FIND_MY_DEVICE:
					if (CURRENT_KEY != null)
						sPPLOCK.findMyDevice(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;

				case ADD_FINGERPRINT:
					if (CURRENT_KEY != null)
						sPPLOCK.addFingerprint(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getFingerType(), pplBleSession.getFingerPriority(),
								pplBleSession.getStartDate(), pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;
				case DEL_FINGERPRINT:
					if (CURRENT_KEY != null)
						sPPLOCK.delFingerprint(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getFingerId(), CURRENT_KEY.getK1());
					break;
				case ADD_CARD:
					if (CURRENT_KEY != null)
						sPPLOCK.addCard(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()), String.valueOf(CURRENT_KEY.getKeyId()),
								pplBleSession.getCardType(), pplBleSession.getCardPriority(), pplBleSession.getStartDate(),
								pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;
				case DEL_CARD:
					if (CURRENT_KEY != null)
						sPPLOCK.delCard(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getCardId(), CURRENT_KEY.getK1());
					break;
				case CLEAR_FINGERS:
					if (CURRENT_KEY != null)
						sPPLOCK.clearFingers(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;
				case CLEAR_CARDS:
					if (CURRENT_KEY != null)
						sPPLOCK.clearCards(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), CURRENT_KEY.getK1());
					break;
				case MODIFY_FINGERPRINT_VALID:
					if (CURRENT_KEY != null)
						sPPLOCK.modifyFingerprintPeriod(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getFingerId(), pplBleSession.getFingerType(),
								pplBleSession.getStartDate(), pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;
				case MODIFY_CARD_VALID:
					if (CURRENT_KEY != null)
						sPPLOCK.modifyCardPeriod(CURRENT_KEY.getUserId(), String.valueOf(CURRENT_KEY.getLockId()),
								String.valueOf(CURRENT_KEY.getKeyId()), pplBleSession.getCardId(), pplBleSession.getCardType(),
								pplBleSession.getStartDate(), pplBleSession.getEndDate(), CURRENT_KEY.getK1());
					break;

				default:
					break;
			}
		}

		@Override
		public void onDeviceDisconnected(BleDevice device) {
			stopLoading();
			Log.e("", "Disconnected");
			switch (pplBleSession.getOperation()) {
				case ADMIN_UNLOCK:
					pplBleSession.getmILockUnlock().onUnlockFinish();
					break;

				case ADMIN_LOCK:
					pplBleSession.getmILockLock().onLockFinish();
					break;

				case DELETE_LOCK:
					pplBleSession.getmILockDeleteLock().onFinish();
					break;

				default:
					break;
			}
		}

		@Override
		public void onInitLock(BleDevice device, com.populock.manhattan.sdk.entity.LockData lockData, LockError error) {
			stopLoading();
			if (error == LockError.SUCCESS) {
				/*Intent intent = new Intent(getApplication(), LockNameAddActivity.class);
				intent.putExtra(LockNameAddActivity.KEY_LOCK_INIT_DATA, lockDataJson);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);*/
				//AddDeviceSuccessActivity.actionStart(getApplication(), HomeDeviceInfo.IDeviceModel.MODEL_LOCK_DEADBOLT)
				sPPLOCK.stopScan();
				String lockDataJson = lockData.toJson();
				PeachLogger.e("lockData", lockDataJson);
//				Intent intent = new Intent(getApplication(), LockNameAddActivity.class);
//				intent.putExtra(LockNameAddActivity.KEY_LOCK_INIT_DATA, lockDataJson);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);

				EventBus.getDefault().post(new Event(Event.EventType.LOCK_LOCAL_INITIALIZE_SUCCEED, lockDataJson));

			} else {
				EventBus.getDefault().post(new Event(Event.EventType.LOCK_LOCAL_INITIALIZE_FAIL));
			}
		}

		@Override
		public void onDeleteLock(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockDeleteLock().onSuccess();
			} else {
				pplBleSession.getmILockDeleteLock().onFail();
			}
		}

		@Override
		public void onUnlock(BleDevice device, int battery, LockError error) {
			if (error == LockError.SUCCESS) { // 开锁成功
				pplBleSession.getmILockUnlock().onUnlockSuccess(battery);
			} else { // 开锁失败
				pplBleSession.getmILockUnlock().onUnlockFail();
			}
		}

		@Override
		public void onLock(BleDevice device, int battery, LockError error) {
			if (error == LockError.SUCCESS) { // 闭锁成功
				pplBleSession.getmILockLock().onLockSuccess(battery);
			} else { // 闭锁失败
				pplBleSession.getmILockLock().onLockFail();
			}
		}

		@Override
		public void onGetBatteryLevel(BleDevice device, int batteryLevel, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockGetBattery().onSuccess(batteryLevel);
			} else {
				pplBleSession.getmILockGetBattery().onFail();
			}
		}

		@Override
		public void onGetLockTime(BleDevice device, long lockTime, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockGetTime().onSuccess(lockTime);
			} else {
				pplBleSession.getmILockGetTime().onFail();
			}
		}

		@Override
		public void onGetAutoLockTime(BleDevice device, int seconds, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockGetAutoLockTime().onSuccess(seconds);
			} else {
				pplBleSession.getmILockGetAutoLockTime().onFail();
			}
		}

		@Override
		public void onGetLockStatus(BleDevice device, boolean isLocked, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockGetLockStatus().onSuccess(isLocked);
			} else {
				pplBleSession.getmILockGetLockStatus().onFail();
			}
		}

		@Override
		public void onSetLockTime(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockSetTime().onSuccess();
			} else {
				pplBleSession.getmILockSetTime().onFail();
			}
		}

		@Override
		public void onGetLockOperateLog(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockGetOperateLog().onSuccess();
			} else {
				pplBleSession.getmILockGetOperateLog().onFail();
			}
		}

		@Override
		public void onSetAutoLockTime(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockModifyAutoLockTime().onSuccess();
			} else {
				pplBleSession.getmILockModifyAutoLockTime().onFail();
			}
		}

		@Override
		public void onSetAdminKeyboardPwd(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockSetAdminKeyboardPwd().onSuccess();
			} else {
				pplBleSession.getmILockSetAdminKeyboardPwd().onFail();
			}
		}

		@Override
		public void onDeleteKeyboardPwd(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockDeletePasscode().onSuccess();
			} else {
				pplBleSession.getmILockDeletePasscode().onFail();
			}
		}

		@Override
		public void onResetEKey(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockResetEkey().onSuccess();
			} else {
				pplBleSession.getmILockResetEkey().onFail();
			}
		}

		@Override
		public void onResetKeyboardPwd(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockResetKeyboardPwd().onSuccess();
			} else {
				pplBleSession.getmILockResetKeyboardPwd().onFail();
			}
		}

		@Override
		public void onAddKeyboardPwd(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockAddPasscode().onSuccess();
			} else {
				pplBleSession.getmILockAddPasscode().onFail();
			}
		}

		@Override
		public void onFindMyDevice(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getILockFindMyDevice().onSuccess();
			} else {
				pplBleSession.getILockFindMyDevice().onFail();
			}
		}

		@Override
		public void onEnterAddFingerprint(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockEnterAddFingerprint().onSuccess();
			} else {
				pplBleSession.getmILockEnterAddFingerprint().onFail();
			}
		}

		@Override
		public void onAddFingerprint(int step, String fingerId, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockAddFingerprint().onSuccess(step, fingerId);
			} else {
				pplBleSession.getmILockAddFingerprint().onFail();
			}

		}

		@Override
		public void onAddCard(String cardId, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockAddCard().onSuccess(cardId);
			} else {
				pplBleSession.getmILockAddCard().onFail();
			}
		}


		@Override
		public void onDeleteFingerprint(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockDeleteFingerprint().onSuccess();
			} else {
				pplBleSession.getmILockDeleteFingerprint().onFail();
			}

		}

		@Override
		public void onDeleteCard(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockDeleteCard().onSuccess();
			} else {
				pplBleSession.getmILockDeleteCard().onFail();
			}
		}

		@Override
		public void onClearFingers(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockClearFingers().onSuccess();
			} else {
				pplBleSession.getmILockClearFingers().onFail();
			}
		}

		@Override
		public void onClearCards(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockClearCards().onSuccess();
			} else {
				pplBleSession.getmILockClearCards().onFail();
			}

		}

		@Override
		public void onModifyKeyboardPwd(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockModifyPasscode().onSuccess();
			} else {
				pplBleSession.getmILockModifyPasscode().onFail();
			}
		}

		@Override
		public void onModifyKeyboardPwdPeriod(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockModifyPasscodePeriod().onSuccess();
			} else {
				pplBleSession.getmILockModifyPasscodePeriod().onFail();
			}
		}

		@Override
		public void onModifyFingerprintPeriod(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockModifyFingerPeriod().onSuccess();
			} else {
				pplBleSession.getmILockModifyFingerPeriod().onFail();
			}
		}

		@Override
		public void onModifyCardPeriod(BleDevice device, LockError error) {
			if (error == LockError.SUCCESS) {
				pplBleSession.getmILockModifyCardPeriod().onSuccess();
			} else {
				pplBleSession.getmILockModifyCardPeriod().onFail();
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		init();
		initBleLock();
	}

	private void init() {
		initLock();
		initVideoPlayerConfig();
	}

	private void initVideoPlayerConfig() {
		PlayerManager.loadConfig(
				new Config.Builder(this)
						.cache(false)
						.buildPlayerFactory(new MediaPlayerFactory())
						.build()
		);
	}

	/**
	 * Init TTLockAPI Object
	 */
	private void initLock() {
		mTTLockAPI = new TTLockAPI(getApplication(), mTTLockCallback);

	}
//	/**
//	 * 全局当前正在使用的 key（蓝牙钥匙）
//	 */
//	public static KeyBean sCURRENT_KEY = new KeyBean();

	//TODO:
	private <K, V extends Parcelable> void broadcastUpdate(String action, K key, V value) {
		final Intent intent = new Intent(action);
		if (key != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable((String) key, value);
			intent.putExtras(bundle);
		}
		sendBroadcast(intent);
	}

	/**
	 * 初始化蓝牙锁相关功能
	 */
	private void initBleLock() {
		// get PPLock instance of SDK
		sPPLOCK = PPLock.getInstance(getApplication(), mPPLockCallback);
	}

	/**
	 * 请求服务器，初始化锁
	 */
	private void initLock(final com.populock.manhattan.sdk.entity.LockData lockData) {
//		RestClient.builder()
//				.url(Urls.LOCK_BLE_BIND)
//				.params("name", lockData.getLockName())
//				.params("mac", lockData.getLockMac())
//				.params("aesKey", lockData.getAesKey())
//				.params("electricQuantity", String.valueOf(lockData.getBatteryLevel()))
//				.params("timezoneRawOffset", String.valueOf(lockData.getTimezoneRawOffset()))
//				.params("protocolVersion", lockData.getProtocolVersion())
//				.params("modelId", lockData.getModelNum())
//				.params("hardwareVersion", lockData.getHardwareVersion())
//				.params("firwareVersion", lockData.getFirmwareVersion())
//				.params("timestamp", String.valueOf(lockData.getTimestamp()))
//				.params("token", PeachPreference.getAccountToken())
//				.success(new ISuccess() {
//					@Override
//					public void onSuccess(String response) {
//						stopLoading();
//						PeachLogger.d("LOCK_BLE_BIND", response);
//
//						JSONObject result = JSON.parseObject(response);
//						int code = result.getInteger("code");
//						if (code == 200) {
//							JSONObject data = result.getJSONObject("data");
//							String mLockId = data.getString("lockId");
//							String k1 = data.getString("k1");
////							mBattery = (int) requestParams.get("electricQuantity");
//							DeviceAddNameBindHomeActivity.actionStart(ActivityCollector.getTopActivity(),
//									Constant.VAL_LOCK_TYPE_BLUETOOTH, mLockId, lockData.getLockName());
//						}
//					}
//				})
//				.failure(new IFailure() {
//					@Override
//					public void onFailure() {
//						PeachLogger.e("fail", "fail");
//						stopLoading();
//						toast(R.string.note_lock_init_fail);
//					}
//				})
//				.error(new IError() {
//					@Override
//					public void onError(int code, String msg) {
//						PeachLogger.e("error", "error");
//						stopLoading();
//						toast(R.string.note_lock_init_fail);
//					}
//				})
//				.build()
//				.post();
	}

}
