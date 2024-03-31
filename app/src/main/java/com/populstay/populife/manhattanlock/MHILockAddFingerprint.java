package com.populstay.populife.manhattanlock;

public interface MHILockAddFingerprint {
    void onSuccess(int step, String fingerId);

    void onFail();
}
