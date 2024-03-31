package com.populstay.populife.lock;

/**
 * Created by Jerry
 */
public interface ILockFingerprintAdd {

	void onEnterAddMode(int totalCount);

	void onCollectSuccess(int currentCount, int totalCount);

	void onAddSuccess(long fingerprintNumber, int totalCount);

	void onFail();

	void onDeviceDisconnected();
}
