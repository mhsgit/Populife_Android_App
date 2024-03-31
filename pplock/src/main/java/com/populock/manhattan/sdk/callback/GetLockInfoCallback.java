package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetLockInfoCallback extends  LockCallback {
	void onSuccess(String lockInfo);
}
