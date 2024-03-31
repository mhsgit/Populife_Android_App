package com.populock.manhattan.sdk.callback;

public interface AddFingerprintCallback  extends LockCallback{

    void onSuccess(int step, String fingerId);

}
