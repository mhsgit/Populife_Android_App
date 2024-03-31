package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetLockTimeCallback extends LockCallback {
	void onSuccess(long lockTime);
}
