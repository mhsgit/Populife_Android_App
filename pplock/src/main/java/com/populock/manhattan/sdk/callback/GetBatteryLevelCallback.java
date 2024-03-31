package com.populock.manhattan.sdk.callback;

/**
 * Created by Jerry
 */
public interface GetBatteryLevelCallback extends LockCallback{
	void onSuccess(int batteryLevel);
}
