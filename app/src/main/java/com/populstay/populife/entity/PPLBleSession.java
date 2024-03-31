package com.populstay.populife.entity;


import com.populock.manhattan.sdk.constant.LockOperation;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.manhattanlock.MHILockAddCard;
import com.populstay.populife.manhattanlock.MHILockAddFingerprint;
import com.populstay.populife.manhattanlock.MHILockAddPasscode;
import com.populstay.populife.manhattanlock.MHILockClearCards;
import com.populstay.populife.manhattanlock.MHILockClearFingers;
import com.populstay.populife.manhattanlock.MHILockDeleteCard;
import com.populstay.populife.manhattanlock.MHILockDeleteFingerprint;
import com.populstay.populife.manhattanlock.MHILockDeleteLock;
import com.populstay.populife.manhattanlock.MHILockDeletePasscode;
import com.populstay.populife.manhattanlock.MHILockEnterAddFingerprint;
import com.populstay.populife.manhattanlock.MHILockFindMyDevice;
import com.populstay.populife.manhattanlock.MHILockGetAutoLockTime;
import com.populstay.populife.manhattanlock.MHILockGetBattery;
import com.populstay.populife.manhattanlock.MHILockGetFirmware;
import com.populstay.populife.manhattanlock.MHILockGetLockStatus;
import com.populstay.populife.manhattanlock.MHILockGetOperateLog;
import com.populstay.populife.manhattanlock.MHILockGetTime;
import com.populstay.populife.manhattanlock.MHILockLock;
import com.populstay.populife.manhattanlock.MHILockModifyAutoLockTime;
import com.populstay.populife.manhattanlock.MHILockModifyCardPeriod;
import com.populstay.populife.manhattanlock.MHILockModifyFingerprintPeriod;
import com.populstay.populife.manhattanlock.MHILockModifyPasscode;
import com.populstay.populife.manhattanlock.MHILockModifyPasscodePeriod;
import com.populstay.populife.manhattanlock.MHILockResetEkey;
import com.populstay.populife.manhattanlock.MHILockResetKeyboardPwd;
import com.populstay.populife.manhattanlock.MHILockSetAdminKeyboardPwd;
import com.populstay.populife.manhattanlock.MHILockSetTime;
import com.populstay.populife.manhattanlock.MHILockUnlock;

import java.util.concurrent.locks.Lock;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class PPLBleSession {

	/**
	 * operation
	 */
	private LockOperation operation;

	/**
	 * lock mac
	 */
	private String lockMac;

	/**
	 * passcode
	 */
	private String password;

	private long startDate;

	private long endDate;

	private int autoLockTime;

	private boolean isAdmin;
	private int keyboardPwdType;
	/**
	 * 远程开锁状态（1 开启、2 关闭）
	 */
	private int remoteUnlockState;
	private String keyboardPwdOriginal;
	private String keyboardPwdNew;
	private String fingerId;
	private String cardId;
	private String fingerType;
	private String fingerPriority;
	private String cardType;
	private String cardPriority;
	private MHILockAddPasscode mILockAddPasscode;
	private MHILockLock mILockLock;
	private MHILockUnlock mILockUnlock;
	private MHILockResetEkey mILockResetEkey;
	private MHILockResetKeyboardPwd mILockResetKeyboardPwd;
	private MHILockSetAdminKeyboardPwd mILockSetAdminKeyboardPwd;
	private MHILockGetTime mILockGetTime;
	private MHILockSetTime mILockSetTime;
	private MHILockGetFirmware mILockGetFirmware;
	private MHILockGetAutoLockTime mILockGetAutoLockTime;
	private MHILockGetLockStatus mILockGetLockStatus;
	private MHILockModifyAutoLockTime mILockModifyAutoLockTime;
	private MHILockDeleteLock mILockDeleteLock;
	private MHILockModifyPasscode mILockModifyPasscode;
	private MHILockModifyPasscodePeriod mILockModifyPasscodePeriod;
	private MHILockDeletePasscode mILockDeletePasscode;
	private MHILockGetBattery mILockGetBattery;
	private MHILockGetOperateLog mILockGetOperateLog;
	private MHILockFindMyDevice mILockFindMyDevice;
	private MHILockEnterAddFingerprint mILockEnterAddFingerprint;
	private MHILockAddFingerprint mILockAddFingerprint;
	private MHILockDeleteFingerprint mILockDeleteFingerprint;
	private MHILockAddCard mILockAddCard;
	private MHILockDeleteCard mILockDeleteCard;
	private MHILockClearFingers mILockClearFingers;
	private MHILockClearCards mILockClearCards;
	private MHILockModifyFingerprintPeriod mILockModifyFingerPeriod;
	private MHILockModifyCardPeriod mILockModifyCardPeriod;

	public MHILockFindMyDevice getILockFindMyDevice() {
		return mILockFindMyDevice;
	}

	public void setILockFindMyDevice(MHILockFindMyDevice ILockFindMyDevice) {
		mILockFindMyDevice = ILockFindMyDevice;
	}

	public static PPLBleSession getInstance(LockOperation operation, String lockmac) {
		PPLBleSession bleSession = new PPLBleSession();
		bleSession.setOperation(operation);
		bleSession.setLockMac(lockmac);
		return bleSession;
	}

	public MHILockGetOperateLog getmILockGetOperateLog() {
		return mILockGetOperateLog;
	}

	public void setmILockGetOperateLog(MHILockGetOperateLog mILockGetOperateLog) {
		this.mILockGetOperateLog = mILockGetOperateLog;
	}

	public MHILockGetBattery getmILockGetBattery() {
		return mILockGetBattery;
	}

	public void setmILockGetBattery(MHILockGetBattery mILockGetBattery) {
		this.mILockGetBattery = mILockGetBattery;
	}

	public int getRemoteUnlockState() {
		return remoteUnlockState;
	}

	public void setRemoteUnlockState(int remoteUnlockState) {
		this.remoteUnlockState = remoteUnlockState;
	}

	public MHILockDeletePasscode getmILockDeletePasscode() {
		return mILockDeletePasscode;
	}

	public void setmILockDeletePasscode(MHILockDeletePasscode mILockDeletePasscode) {
		this.mILockDeletePasscode = mILockDeletePasscode;
	}

	public MHILockModifyPasscode getmILockModifyPasscode() {
		return mILockModifyPasscode;
	}

	public void setmILockModifyPasscode(MHILockModifyPasscode mILockModifyPasscode) {
		this.mILockModifyPasscode = mILockModifyPasscode;
	}

	public MHILockModifyPasscodePeriod getmILockModifyPasscodePeriod() {
		return mILockModifyPasscodePeriod;
	}

	public void setmILockModifyPasscodePeriod(MHILockModifyPasscodePeriod mILockModifyPasscodePeriod) {
		this.mILockModifyPasscodePeriod = mILockModifyPasscodePeriod;
	}

	public int getKeyboardPwdType() {
		return keyboardPwdType;
	}

	public void setKeyboardPwdType(int keyboardPwdType) {
		this.keyboardPwdType = keyboardPwdType;
	}

	public String getKeyboardPwdOriginal() {
		return keyboardPwdOriginal;
	}

	public void setKeyboardPwdOriginal(String keyboardPwdOriginal) {
		this.keyboardPwdOriginal = keyboardPwdOriginal;
	}

	public String getKeyboardPwdNew() {
		return keyboardPwdNew;
	}

	public void setKeyboardPwdNew(String keyboardPwdNew) {
		this.keyboardPwdNew = keyboardPwdNew;
	}

	public MHILockAddPasscode getmILockAddPasscode() {
		return mILockAddPasscode;
	}

	public void setmILockAddPasscode(MHILockAddPasscode mILockAddPasscode) {
		this.mILockAddPasscode = mILockAddPasscode;
	}

	public MHILockLock getmILockLock() {
		return mILockLock;
	}

	public void setmILockLock(MHILockLock mILockLock) {
		this.mILockLock = mILockLock;
	}

	public MHILockUnlock getmILockUnlock() {
		return mILockUnlock;
	}

	public void setmILockUnlock(MHILockUnlock mILockUnlock) {
		this.mILockUnlock = mILockUnlock;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public MHILockResetEkey getmILockResetEkey() {
		return mILockResetEkey;
	}

	public void setmILockResetEkey(MHILockResetEkey mILockResetEkey) {
		this.mILockResetEkey = mILockResetEkey;
	}

	public MHILockResetKeyboardPwd getmILockResetKeyboardPwd() {
		return mILockResetKeyboardPwd;
	}

	public void setmILockResetKeyboardPwd(MHILockResetKeyboardPwd mILockResetKeyboardPwd) {
		this.mILockResetKeyboardPwd = mILockResetKeyboardPwd;
	}

	public MHILockDeleteLock getmILockDeleteLock() {
		return mILockDeleteLock;
	}

	public void setmILockDeleteLock(MHILockDeleteLock mILockDeleteLock) {
		this.mILockDeleteLock = mILockDeleteLock;
	}

	public int getAutoLockTime() {
		return autoLockTime;
	}

	public void setAutoLockTime(int autoLockTime) {
		this.autoLockTime = autoLockTime;
	}

	public MHILockGetLockStatus getmILockGetLockStatus() {
		return mILockGetLockStatus;
	}

	public void setmILockGetLockStatus(MHILockGetLockStatus mILockGetLockStatus) {
		this.mILockGetLockStatus = mILockGetLockStatus;
	}

	public MHILockModifyAutoLockTime getmILockModifyAutoLockTime() {
		return mILockModifyAutoLockTime;
	}

	public void setmILockModifyAutoLockTime(MHILockModifyAutoLockTime mILockModifyAutoLockTime) {
		this.mILockModifyAutoLockTime = mILockModifyAutoLockTime;
	}

	public MHILockGetAutoLockTime getmILockGetAutoLockTime() {
		return mILockGetAutoLockTime;
	}

	public void setmILockGetAutoLockTime(MHILockGetAutoLockTime mILockGetAutoLockTime) {
		this.mILockGetAutoLockTime = mILockGetAutoLockTime;
	}

	public MHILockGetFirmware getmILockGetFirmware() {
		return mILockGetFirmware;
	}

	public void setmILockGetFirmware(MHILockGetFirmware mILockGetFirmware) {
		this.mILockGetFirmware = mILockGetFirmware;
	}

	public MHILockSetTime getmILockSetTime() {
		return mILockSetTime;
	}

	public void setmILockSetTime(MHILockSetTime mILockSetTime) {
		this.mILockSetTime = mILockSetTime;
	}

	public MHILockGetTime getmILockGetTime() {
		return mILockGetTime;
	}

	public void setmILockGetTime(MHILockGetTime mILockGetTime) {
		this.mILockGetTime = mILockGetTime;
	}

	public MHILockSetAdminKeyboardPwd getmILockSetAdminKeyboardPwd() {
		return mILockSetAdminKeyboardPwd;
	}

	public void setmILockSetAdminKeyboardPwd(MHILockSetAdminKeyboardPwd mILockSetAdminKeyboardPwd) {
		this.mILockSetAdminKeyboardPwd = mILockSetAdminKeyboardPwd;
	}

	public void setmILockEnterAddFingerprint(MHILockEnterAddFingerprint mILockEnterAddFingerprint) {
		this.mILockEnterAddFingerprint = mILockEnterAddFingerprint;
	}

	public MHILockEnterAddFingerprint getmILockEnterAddFingerprint() {
		return mILockEnterAddFingerprint;
	}

	public void setmILockAddFingerprint(MHILockAddFingerprint mILockAddFingerprint) {
		this.mILockAddFingerprint = mILockAddFingerprint;
	}

	public MHILockAddFingerprint getmILockAddFingerprint() {
		return mILockAddFingerprint;
	}

	public void setmILockDeleteFingerprint(MHILockDeleteFingerprint mILockDeleteFingerprint) {
		this.mILockDeleteFingerprint = mILockDeleteFingerprint;
	}

	public MHILockDeleteFingerprint getmILockDeleteFingerprint() {
		return mILockDeleteFingerprint;
	}

	public void setmILockAddCard(MHILockAddCard mILockAddCard) {
		this.mILockAddCard = mILockAddCard;
	}

	public MHILockAddCard getmILockAddCard() {
		return mILockAddCard;
	}

	public void setmILockDeleteCard(MHILockDeleteCard mILockDeleteCard) {
		this.mILockDeleteCard = mILockDeleteCard;
	}

	public MHILockDeleteCard getmILockDeleteCard() {
		return mILockDeleteCard;
	}

	public void setmILockClearFingers(MHILockClearFingers mILockClearFingers) {
		this.mILockClearFingers = mILockClearFingers;
	}

	public MHILockClearFingers getmILockClearFingers() {
		return mILockClearFingers;
	}

	public void setmILockClearCards(MHILockClearCards mILockClearCards) {
		this.mILockClearCards = mILockClearCards;
	}

	public MHILockClearCards getmILockClearCards() {
		return mILockClearCards;
	}

	public void setmILockModifyFingerPeriod(MHILockModifyFingerprintPeriod mILockModifyFingerPeriod) {
		this.mILockModifyFingerPeriod = mILockModifyFingerPeriod;
	}

	public void setmILockModifyCardPeriod(MHILockModifyCardPeriod mILockModifyCardPeriod) {
		this.mILockModifyCardPeriod = mILockModifyCardPeriod;
	}

	public MHILockModifyFingerprintPeriod getmILockModifyFingerPeriod() {
		return mILockModifyFingerPeriod;
	}

	public MHILockModifyCardPeriod getmILockModifyCardPeriod() {
		return mILockModifyCardPeriod;
	}


	public LockOperation getOperation() {
		return operation;
	}

	public void setOperation(LockOperation operation) {
		this.operation = operation;
	}

	public String getLockMac() {
		return lockMac;
	}

	public void setLockMac(String lockMac) {
		this.lockMac = lockMac;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public void setFingerId(String fingerId) {
		this.fingerId = fingerId;
	}

	public String getFingerId() {
		return fingerId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setFingerType(String fingerType) {
		this.fingerType = fingerType;
	}

	public String getFingerType() {
		return fingerType;
	}

	public void setFingerPriority(String fingerPriority) {
		this.fingerPriority = fingerPriority;
	}

	public String getFingerPriority() {
		return fingerPriority;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardPriority(String cardPriority) {
		this.cardPriority = cardPriority;
	}

	public String getCardPriority() {
		return cardPriority;
	}


}
