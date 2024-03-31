package com.populock.manhattan.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jerry
 */
public class ConfigUtil {

	private static final String COMMAND_ID = "command_id";
	private static final String ACK_ID = "ack_id";
	private static final String USER_ID = "user_id";
	private static final String LOCK_ID = "lock_id";
	private static final String KEY_ID = "key_id";

	private static SharedPreferences getAppPreference(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 读取 commandId
	 */
	public static int getCommandId(Context context) {
		return getAppPreference(context)
				.getInt(COMMAND_ID, 0);
	}

	/**
	 * 保存 commandID
	 */
	public static void setCommandId(Context context, int commandID) {
		getAppPreference(context)
				.edit()
				.putInt(COMMAND_ID, commandID)
				.apply();
	}

	/**
	 * 读取 ackId
	 */
	public static String getAckId(Context context) {
		return getAppPreference(context)
				.getString(ACK_ID, "");
	}

	/**
	 * 保存 ackId
	 */
	public static void setAckId(Context context, String ackId) {
		getAppPreference(context)
				.edit()
				.putString(ACK_ID, ackId)
				.apply();
	}

	/**
	 * 读取 userId
	 */
	public static String getUserId(Context context) {
		return getAppPreference(context)
				.getString(USER_ID, "");
	}

	/**
	 * 保存 userId
	 */
	public static void setUserId(Context context, String userId) {
		getAppPreference(context)
				.edit()
				.putString(USER_ID, userId)
				.apply();
	}

	/**
	 * 读取 lockId
	 */
	public static String getLockId(Context context) {
		return getAppPreference(context)
				.getString(LOCK_ID, "");
	}

	/**
	 * 保存 lockId
	 */
	public static void setLockId(Context context, String lockId) {
		getAppPreference(context)
				.edit()
				.putString(LOCK_ID, lockId)
				.apply();
	}

	/**
	 * 读取 keyId
	 */
	public static String getKeyId(Context context) {
		return getAppPreference(context)
				.getString(KEY_ID, "");
	}

	/**
	 * 保存 keyId
	 */
	public static void setKeyId(Context context, String keyId) {
		getAppPreference(context)
				.edit()
				.putString(KEY_ID, keyId)
				.apply();
	}
}
