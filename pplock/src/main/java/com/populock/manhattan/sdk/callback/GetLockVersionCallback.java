package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetLockVersionCallback extends LockCallback {
	void onSuccess(String lockVersion);
}
