package com.populstay.populife.lock;

/**
 * 校正锁的时间
 * Created by Jerry
 */
public interface ILockSetTime {

	void onSuccess();

	void onFail();

	void onTimeOut();
}
