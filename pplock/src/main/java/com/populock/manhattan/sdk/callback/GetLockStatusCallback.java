package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetLockStatusCallback extends LockCallback {
	void onSuccess(boolean isLocked);
}
