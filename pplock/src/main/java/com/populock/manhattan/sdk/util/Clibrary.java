/*
 * Copyright (c) 2018, szmuen and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package com.populock.manhattan.sdk.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * @author shiwei
 * @date 2018年12月4日
 */
public interface Clibrary extends Library {

	//public static final Clibrary INSTANCE = (Clibrary) Native.load((Platform.isWindows() ? "winaes" : "aes"), Clibrary.class);
	public static final Clibrary INSTANCE = (Clibrary) Native.load("ygl_aes", Clibrary.class);

//	byte[] PASSWORD = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
//
//	String ENCRYPT_NOTICE = "01020304050607080102030405060708";

	/**
	 * 解密
	 *
	 * @param cipher
	 * @param inLen
	 * @param Key
	 * @param KeyLen
	 * @param mode
	 * @param iv
	 * @param plain
	 */
	void AESDecrypt(byte[] cipher, int inLen, byte[] Key, int KeyLen, int mode, byte[] iv, byte[] plain);

	/**
	 * 加密
	 *
	 * @param plain
	 * @param inLen
	 * @param Key
	 * @param KeyLen
	 * @param mode
	 * @param iv
	 * @param cipher
	 */
	void AESEncrypt(byte[] plain, int inLen, byte[] Key, int KeyLen, int mode, byte[] iv, byte[] cipher);
}
