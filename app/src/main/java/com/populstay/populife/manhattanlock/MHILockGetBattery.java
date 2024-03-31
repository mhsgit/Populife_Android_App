package com.populstay.populife.manhattanlock;

/**
 * 读取锁的电量
 * Created by Jerry
 */
public interface MHILockGetBattery {

	void onSuccess(int battery);

	void onFail();
}
