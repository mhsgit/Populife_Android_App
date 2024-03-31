package com.populstay.populife.manhattanlock;

/**
 * 读取锁的自动闭锁时间
 * Created by Jerry
 */
public interface MHILockGetAutoLockTime {

	void onSuccess(int seconds);

	void onFail();
}
