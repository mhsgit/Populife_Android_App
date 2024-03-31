package com.populstay.populife.manhattanlock;

/**
 * 开锁接口回调
 * Created by Jerry
 */
public interface MHILockUnlock {

	void onUnlockSuccess(int battery);

	void onUnlockFail();

	void onUnlockFinish();
}
