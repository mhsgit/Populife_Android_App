package com.populstay.populife.manhattanlock;

/**
 * 读取锁的时间
 * Created by Jerry
 */
public interface MHILockGetTime {

	void onSuccess(long time);

	void onFail();
}
