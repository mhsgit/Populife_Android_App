package com.populock.manhattan.sdk.util;


import com.orhanobut.logger.Logger;

/**
 * Log Util
 * <p>
 * Created by Jerry
 */
public class LogUtil {

	private static final String TAG = "PPLock";
	private static boolean DEBUG = true;// todo 打印日志

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean DBG) {
		DEBUG = DBG;
	}

	public static synchronized void d(String content) {
		d(content, true);
	}

	public static synchronized void i(String content) {
		i(content, true);
	}

	public static synchronized void w(String content) {
		w(content, true);
	}

	public static synchronized void e(String content) {
		e(content, true);
	}

	public static synchronized void d(String content, boolean DBG) {
		if (DEBUG && DBG) {
			//Log.d(TAG, content);
			Logger.t(TAG).d(content);
		}
	}

	public static synchronized void i(String content, boolean DBG) {
		if (DEBUG && DBG) {
			//Log.i(TAG, content);
			Logger.t(TAG).i(content);
		}
	}

	public static synchronized void w(String content, boolean DBG) {
		if (DEBUG && DBG) {
			//Log.w(TAG, content);
			Logger.t(TAG).d(content);
		}
	}

	public static synchronized void e(String content, boolean DBG) {
		if (DEBUG && DBG) {
			//Log.e(TAG, content);
			Logger.t(TAG).e(content);
		}
	}
}
