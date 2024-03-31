package com.populock.manhattan.sdk.callback;

import com.populock.manhattan.sdk.BleDevice;

/**
 * Created by Jerry
 */
public interface DeleteLockCallback extends LockCallback {
	void onSuccess(BleDevice device);
}
