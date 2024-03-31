package com.populstay.populife.manhattanlock;

/**
 * 闭锁接口回调
 * Created by Jerry
 */
public interface MHILockLock {

	void onLockSuccess(int battery);

	void onLockFail();

	void onLockFinish();
}
