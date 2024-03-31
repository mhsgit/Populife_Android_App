package com.populock.manhattan.sdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
//import android.support.annotation.RequiresPermission;
import androidx.annotation.RequiresPermission;

import com.populock.manhattan.sdk.adrecord.AdRecord;
import com.populock.manhattan.sdk.adrecord.AdRecordStore;
import com.populock.manhattan.sdk.util.AdRecordUtil;
import com.populock.manhattan.sdk.util.HexUtil;

import java.util.Collection;

/**
 * Bluetooth device entity
 * <p>
 * Created by Jerry
 */
public class BleDevice implements Parcelable {

	public static final Creator<BleDevice> CREATOR = new Creator<BleDevice>() {
		@Override
		public BleDevice createFromParcel(Parcel in) {
			return new BleDevice(in);
		}

		@Override
		public BleDevice[] newArray(int size) {
			return new BleDevice[size];
		}
	};
	public int disconnectStatus;
	private BluetoothDevice device;
	private int rssi;
	private byte[] scanRecord;
	private AdRecordStore adRecordStore;
	private String name;
	private String address;
	private boolean isSettingMode;
	private long date;
	/**
	 * 电池电量
	 * -1 表示未获取到或者不支持
	 */
	private int batteryLevel = -1;

	public BleDevice() {
		this.isSettingMode = true;
		this.date = System.currentTimeMillis();
//		this.isTouch = true;
		batteryLevel = -1;
	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH}
	)
	public BleDevice(BluetoothDevice device) {
		this(device, 0, null);
	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH}
	)
	public BleDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
		this.device = device;
		this.rssi = rssi;
		this.scanRecord = scanRecord;
		this.name = device.getName();
		this.address = device.getAddress();
		this.isSettingMode = true;
		this.date = System.currentTimeMillis();
		if (scanRecord != null) {
			initial();
		}
	}

	@RequiresPermission(
			allOf = {Manifest.permission.BLUETOOTH}
	)
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public BleDevice(ScanResult scanResult) {
//		this.isTouch = true;
		this.batteryLevel = -1;
		this.device = scanResult.getDevice();
		this.rssi = scanResult.getRssi();
		this.scanRecord = scanResult.getScanRecord().getBytes();
		this.name = device.getName();
		this.address = device.getAddress();
		this.isSettingMode = true;
		this.date = System.currentTimeMillis();
		initial();
	}

	protected BleDevice(Parcel in) {
		isSettingMode = true;
		date = System.currentTimeMillis();
		device = in.readParcelable(BleDevice.class.getClassLoader());
		rssi = in.readInt();
		scanRecord = in.createByteArray();
		adRecordStore = in.readParcelable(AdRecordStore.class.getClassLoader());
		name = in.readString();
		address = in.readString();
		isSettingMode = in.readByte() == 1;
		date = in.readLong();
		disconnectStatus = in.readInt();
		batteryLevel = in.readInt();
	}

	private void initial() {
		adRecordStore = new AdRecordStore(AdRecordUtil.parseScanRecordAsSparseArray(scanRecord));
		final Collection<AdRecord> adRecords = getAdRecordStore().getRecordsAsCollection();
		if (adRecords.size() > 0) {
			for (final AdRecord record : adRecords) {
				if (record.getType() == 255) {
					String manufacturerInfo = HexUtil.encodeHexStr(record.getData());
					if ("00".equals(manufacturerInfo.substring(manufacturerInfo.length() - 2))) {
						isSettingMode = true;
					}
				} else {
					isSettingMode = false;
				}
			}
		}
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public AdRecordStore getAdRecordStore() {
		return adRecordStore;
	}

	public void setAdRecordStore(AdRecordStore adRecordStore) {
		this.adRecordStore = adRecordStore;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public byte[] getScanRecord() {
		return scanRecord;
	}

	public void setScanRecord(byte[] scanRecord) {
		this.scanRecord = scanRecord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isSettingMode() {
		return isSettingMode;
	}

	public void setSettingMode(boolean settingMode) {
		isSettingMode = settingMode;
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	public boolean equals(Object o) {
		return o instanceof BleDevice && this.address.equals(((BleDevice) o).getAddress());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(device, flags);
		dest.writeInt(rssi);
		dest.writeByteArray(scanRecord);
		dest.writeParcelable(adRecordStore, flags);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeByte((byte) (isSettingMode ? 1 : 0));
		dest.writeLong(date);
		dest.writeInt(disconnectStatus);
		dest.writeInt(batteryLevel);
	}
}
