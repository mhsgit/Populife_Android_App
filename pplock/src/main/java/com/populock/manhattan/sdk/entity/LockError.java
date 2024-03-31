package com.populock.manhattan.sdk.entity;

/**
 * Created by Jerry
 */
public enum LockError {

	SUCCESS(0, "Operation success."),
	FAIL(1,"Operation fail."),
	LOCK_NO_PERMISSION(2, "Not verified, have no permission."),
	LOCK_ADMIN_CHECK_ERROR(3, "Wrong administrator password."),
	LOCK_IN_SETTING_MODE(4, "Lock is in setting mode."),
	LOCK_NOT_IN_SETTING_MODE(5, "Lock is not in setting mode."),
	LOCK_NOT_INIT(6,"Lock has not been initiated.");

	private String lockMac;
	private int errorCode;
	private String errorMsg;

	private LockError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getLockMac() {
		return lockMac;
	}

	public void setLockMac(String lockMac) {
		this.lockMac = lockMac;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
