package com.populstay.populife.lock;

import com.ttlock.bl.sdk.entity.Error;

/**
 * Created by Jerry
 */
public interface ILockFingerprintSearch {

	void onSuccess(String fingerprintInfo);

	void onFail(Error error);
}
