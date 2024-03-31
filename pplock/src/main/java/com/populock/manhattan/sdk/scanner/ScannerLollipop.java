package com.populock.manhattan.sdk.scanner;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.ParcelUuid;
//import android.support.annotation.RequiresPermission;
import androidx.annotation.RequiresPermission;
import com.populock.manhattan.sdk.BleDevice;
import com.populock.manhattan.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jerry
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScannerLollipop extends ScannerCompat {

	private final String TAG = "ScannerLollipop";

	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private ScannerLollipop.ScanCallbackImpl scanCallback = new ScannerLollipop.ScanCallbackImpl();
	private BluetoothLeScanner mScanner;

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}
	)
	public void startScanInternal(UUID[] serviceUuids) {
		mScanner = mBluetoothAdapter.getBluetoothLeScanner();
		if (mScanner == null) {
			LogUtil.w("BluetoothLeScanner unavailable");
		} else {
			ScanSettings settings = (new ScanSettings.Builder()).setScanMode(2).build();
			List<ScanFilter> filters = new ArrayList();
			filters.add((new android.bluetooth.le.ScanFilter.Builder())
					.setServiceUuid(ParcelUuid.fromString(UUID_SERVICE))
					.build());
			try {
				mScanner.startScan(filters, settings, scanCallback);
			} catch (Exception var5) {
				var5.printStackTrace();
			}
		}
	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}
	)
	public void stopScan() {
		if (mScanner != null && scanCallback != null && mBluetoothAdapter.isEnabled()) {
			mScanner.stopScan(this.scanCallback);
			LogUtil.d("scanCallback:" + this.scanCallback);
		}
	}

	private class ScanCallbackImpl extends ScanCallback {
		private ScanCallbackImpl() {
		}

		@RequiresPermission(
				allOf = {"android.permission.BLUETOOTH"}
		)
		public void onScanResult(int callbackType, ScanResult result) {
			super.onScanResult(callbackType, result);
			mIScanCallback.onScan(new BleDevice(result));
		}

		@RequiresPermission("android.permission.BLUETOOTH_ADMIN")
		public void onScanFailed(int errorCode) {
			super.onScanFailed(errorCode);
			if (onScanFailedListener != null) {
				onScanFailedListener.onScanFailed(errorCode);
			}

			LogUtil.w("errorCode=" + errorCode);
		}
	}
}
