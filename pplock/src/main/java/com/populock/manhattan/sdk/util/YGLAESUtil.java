package com.populock.manhattan.sdk.util;

/**
 * Created by Jerry
 */
public class  YGLAESUtil{

	static {
		System.loadLibrary("ygl_aes");
	}

	public native static byte[] AESDecrypt(byte [] cipher, int inLen, byte[] key, int KeyLen, int mode, byte[] iv);

	public native static  byte[] AESEncrypt(byte [] plain, int inLen, byte[] key, int KeyLen, int mode, byte[] iv);

}
