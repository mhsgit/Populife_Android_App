package com.populock.manhattan.sdk.util;

/**
 * Created by Jerry
 */
public class DigitUtil {

	public static String byteArrayToHexString(byte[] array) {
		if (array == null) {
			return null;
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append('[');
			if (array == null || array.length == 0) {
				stringBuilder.append(']');
			}

			for (int i = 0; i < array.length; ++i) {
				stringBuilder.append(byteToHex(array[i]));
				stringBuilder.append(',');
			}

			stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "]");
			return stringBuilder.toString();
		}
	}

	public static String byteToHex(byte value) {
		String hex = Integer.toHexString(value & 255);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}

		return hex;
	}

	public static byte[] strToByteArray(String str) {
		if (str == null) {
			return null;
		}
		byte[] byteArray = str.getBytes();
		return byteArray;
	}

	public static String byteArrayToStr(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}
		String str = new String(byteArray);
		return str;
	}

}
