package com.populock.manhattan.sdk.callback;

public interface AddCardCallback extends LockCallback{

    void onSuccess(String cardId);
}
