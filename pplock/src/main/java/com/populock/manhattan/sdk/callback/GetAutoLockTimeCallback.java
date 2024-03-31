package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetAutoLockTimeCallback extends LockCallback {
	void onSuccess(int seconds);
}
