package com.populock.manhattan.sdk.callback;

import com.populock.manhattan.sdk.entity.LockError;

/**
 * Created by Jerry
 */
public interface LockCallback {

	void onFail(LockError error);

}
