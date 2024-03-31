package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface BleWriteCallback {

	void onWriteSuccess();

	void onFail();

	void onResponseSuccess(String response);
}
