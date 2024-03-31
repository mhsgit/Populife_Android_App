package com.populstay.populife.manhattanlock;

/**
 * 删除/重置锁（管理员操作）
 * Created by Jerry
 */
public interface MHILockDeleteLock {

	void onSuccess();

	void onFail();

	void onFinish();
}
