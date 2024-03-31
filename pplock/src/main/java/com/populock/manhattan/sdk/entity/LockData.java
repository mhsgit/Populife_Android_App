package com.populock.manhattan.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.populock.manhattan.sdk.util.GsonUtil;

import java.util.TimeZone;

/**
 * Created by Jerry
 */
public class LockData implements Parcelable {

	public static final Creator<LockData> CREATOR = new Creator<LockData>() {
		@Override
		public LockData createFromParcel(Parcel in) {
			return new LockData(in);
		}

		@Override
		public LockData[] newArray(int size) {
			return new LockData[size];
		}
	};

	/**
	 * 锁的蓝牙名称
	 */
	private String userId;

	/**
	 * 锁的蓝牙名称
	 */
	private String lockName;

	/**
	 * 锁的别名
	 */
	private String alias;

	/**
	 * 锁 lockMac 地址
	 */
	private String lockMac;
	/**
	 * Aes 加解密 key
	 */
	private String aesKey;
	/**
	 * 锁电量
	 */
	private int batteryLevel;

	/**
	 * 锁电量
	 */
	private int electricQuantity;

	/**
	 * 锁所在时区和 UTC 时区时间的差数，单位 milliseconds
	 */
	private long timezoneRawOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
	/**
	 * 协议类型
	 */
	private String protocolType;
	/**
	 * 协议版本
	 */
	private String protocolVersion;
	/**
	 * 场景
	 */
	private String scene;
	/**
	 * 公司代号
	 */
	private String group;
	/**
	 * 供应商代号
	 */
	private String vendor;
	/**
	 * 产品型号
	 */
	private String modelNum;
	/**
	 * 硬件版本号
	 */
	private String hardwareVersion;
	/**
	 * 固件版本号
	 */
	private String firmwareVersion;
	/**
	 * 时间戳
	 */
	private long timestamp = System.currentTimeMillis();

	public String toJson() {
		return GsonUtil.toJson(this);
	}

	public LockData() {

	}

	protected LockData(Parcel in) {
		userId = in.readString();
		electricQuantity = in.readInt();
		alias = in.readString();
		lockName = in.readString();
		lockMac = in.readString();
		aesKey = in.readString();
		batteryLevel = in.readInt();
		timezoneRawOffset = in.readLong();
		protocolType = in.readString();
		protocolVersion = in.readString();
		scene = in.readString();
		group = in.readString();
		vendor = in.readString();
		modelNum = in.readString();
		hardwareVersion = in.readString();
		firmwareVersion = in.readString();
		timestamp = in.readLong();
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public String getLockMac() {
		return lockMac;
	}

	public void setLockMac(String lockMac) {
		this.lockMac = lockMac;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public long getTimezoneRawOffset() {
		return timezoneRawOffset;
	}

	public void setTimezoneRawOffset(long timezoneRawOffset) {
		this.timezoneRawOffset = timezoneRawOffset;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModelNum() {
		return modelNum;
	}

	public void setModelNum(String modelNum) {
		this.modelNum = modelNum;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "LockData{" +
				"lockName='" + lockName + '\'' +
				", lockMac='" + lockMac + '\'' +
				", aesKey='" + aesKey + '\'' +
				", batteryLevel=" + batteryLevel +
				", timezoneRawOffset=" + timezoneRawOffset +
				", protocolType='" + protocolType + '\'' +
				", protocolVersion='" + protocolVersion + '\'' +
				", scene='" + scene + '\'' +
				", group='" + group + '\'' +
				", vendor='" + vendor + '\'' +
				", modelNum='" + modelNum + '\'' +
				", hardwareVersion='" + hardwareVersion + '\'' +
				", firmwareVersion='" + firmwareVersion + '\'' +
				", timestamp=" + timestamp +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(lockName);
		dest.writeString(lockMac);
		dest.writeString(aesKey);
		dest.writeInt(batteryLevel);
		dest.writeLong(timezoneRawOffset);
		dest.writeString(protocolType);
		dest.writeString(protocolVersion);
		dest.writeString(scene);
		dest.writeString(group);
		dest.writeString(vendor);
		dest.writeString(modelNum);
		dest.writeString(hardwareVersion);
		dest.writeString(firmwareVersion);
		dest.writeLong(timestamp);
	}
//	public String toJson() {
//		if (!TextUtils.isEmpty(lockMac) &&lockMac.length() > 5 && TextUtils.isEmpty(ref)) {
//			String headRef = lockMac.substring(lockMac.length() - 5);
//			ref = DigitUtil.encodeLockData(headRef + factoryDate);
//		}
//		return GsonUtil.toJson(this);
//	}
}
