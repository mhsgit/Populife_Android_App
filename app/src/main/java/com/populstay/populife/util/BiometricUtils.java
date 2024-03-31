package com.populstay.populife.util;

import android.content.Context;

import androidx.biometric.BiometricManager;


public class BiometricUtils {

    /**
     * 是否支持生物特征认证
     * @param context
     * @return
     */
    public static boolean isSupportBiometric(Context context){
        try{
            BiometricManager biometricManager = BiometricManager.from(context);
            int currentAuthenticateType =  biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
            return BiometricManager.BIOMETRIC_SUCCESS == currentAuthenticateType;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
