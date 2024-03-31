package com.populock.manhattan.sdk.scanner;

import com.populock.manhattan.sdk.BleDevice;

/**
 * Created by Jerry
 */
public interface IScanCallback {
	void onScan(BleDevice bleDevice);
}
