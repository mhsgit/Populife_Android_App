package com.populstay.populife.lock;

/**
 * 读取锁的时间
 * Created by Jerry
 */
public interface ILockGetTime {

	void onSuccess(long time);

	void onFail();

	void onTimeOut();
}
