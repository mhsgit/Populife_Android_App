package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface InitLockCallback extends LockCallback {
	void onSuccess(String lockData);
}
