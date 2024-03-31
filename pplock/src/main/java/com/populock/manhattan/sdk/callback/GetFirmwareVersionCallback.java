package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetFirmwareVersionCallback extends  LockCallback {
	void onSuccess(String firmwareVersion);
}
