package com.populock.manhattan.sdk.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
//import android.support.annotation.RequiresPermission;
import androidx.annotation.RequiresPermission;

import com.populock.manhattan.sdk.BleDevice;

import java.util.UUID;

/**
 * Created by Jerry
 */
public class ScannerImplJB extends ScannerCompat implements LeScanCallback {

	private final String TAG = "ScannerImplJB";

	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}
	)
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		this.mIScanCallback.onScan(new BleDevice(device, rssi, scanRecord));
	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}
	)
	public void startScanInternal(UUID[] serviceUuids) {
		mBluetoothAdapter.startLeScan(serviceUuids, this);

	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}
	)
	public void stopScan() {
		try {
			if (mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.stopLeScan(this);
			}
		} catch (Exception var2) {
			var2.printStackTrace();
		}

	}
}
