package com.populock.manhattan.sdk.callback;

import com.populock.manhattan.sdk.BleDevice;
import com.populock.manhattan.sdk.entity.LockData;
import com.populock.manhattan.sdk.entity.LockError;

/**
 * All the callbacks of lock operations
 * <p>
 * Created by Jerry
 */
public interface PPLockCallback {

	void onFoundDevice(BleDevice device);

	void onDeviceConnected(BleDevice device);

	void onDeviceDisconnected(BleDevice device);

	void onInitLock(BleDevice device, LockData lockData, LockError error);

	void onDeleteLock(BleDevice device, LockError error);

	void onUnlock(BleDevice device, int batteryLevel, LockError error);

	void onLock(BleDevice device,int batteryLevel, LockError error);

	void onGetBatteryLevel(BleDevice device, int batteryLevel, LockError error);

	void onGetLockTime(BleDevice device, long lockTime, LockError error);

	void onGetAutoLockTime(BleDevice device, int seconds, LockError error);

	void onGetLockStatus(BleDevice device, boolean isLocked, LockError error);

	void onSetLockTime(BleDevice device, LockError error);

	void onGetLockOperateLog(BleDevice device, LockError error);

	void onSetAutoLockTime(BleDevice device, LockError error);

	void onSetAdminKeyboardPwd(BleDevice device, LockError error);

	void onDeleteKeyboardPwd(BleDevice device, LockError error);

	void onResetEKey(BleDevice device, LockError error);

	void onResetKeyboardPwd(BleDevice device, LockError error);

	void onAddKeyboardPwd(BleDevice device, LockError error);

	void onFindMyDevice(BleDevice device, LockError error);

	void onEnterAddFingerprint(BleDevice device, LockError error);

	void onAddFingerprint(int step,String fingerId, LockError error);

	void onAddCard(String cardId, LockError error);

	void onDeleteFingerprint(BleDevice device, LockError error);

	void onDeleteCard(BleDevice device, LockError error);

	void onClearFingers(BleDevice device, LockError error);

	void onClearCards(BleDevice device, LockError error);

	void onModifyKeyboardPwd(BleDevice device, LockError error);

	void onModifyKeyboardPwdPeriod(BleDevice device, LockError error);

	void onModifyFingerprintPeriod(BleDevice device, LockError error);

	void onModifyCardPeriod(BleDevice device, LockError error);
}
