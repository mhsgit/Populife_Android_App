package com.populstay.populife.manhattanlock;

/**
 * Created by Jerry
 */
public interface MHILockGetLockStatus {
	void onSuccess(boolean isLocked);

	void onFail();
}
