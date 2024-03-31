package com.populock.manhattan.sdk.scanner;

import android.os.Build;

import com.populock.manhattan.sdk.callback.OnScanFailedListener;
import com.populock.manhattan.sdk.service.ThreadPool;
import com.populock.manhattan.sdk.util.LogUtil;

import java.util.UUID;

/**
 * Created by Jerry
 */
public abstract class ScannerCompat {

	protected static String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
	protected static UUID[] serviceUuids;
	private static ScannerCompat sScannerCompat;

	static {
		serviceUuids = new UUID[]{UUID.fromString(UUID_SERVICE)};
	}

	private final String TAG = "ScannerCompat";
	protected OnScanFailedListener onScanFailedListener;
	protected IScanCallback mIScanCallback;

	public static ScannerCompat getScanner() {
		if (sScannerCompat == null) {
			sScannerCompat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
					new ScannerLollipop() : new ScannerImplJB();
		}
		return sScannerCompat;
	}

	public void setOnScanFailedListener(OnScanFailedListener onScanFailedListener) {
		this.onScanFailedListener = onScanFailedListener;
	}

	public void startScan(final IScanCallback scanCallback) {
		LogUtil.d("scanCallback:" + scanCallback);
		LogUtil.d(Thread.currentThread().toString());
		ThreadPool.getThreadPool().execute(new Runnable() {
			public void run() {
				mIScanCallback = scanCallback;
				startScanInternal(ScannerCompat.serviceUuids);
			}
		});
	}

	public abstract void startScanInternal(UUID[] uuids);

	public abstract void stopScan();

}
