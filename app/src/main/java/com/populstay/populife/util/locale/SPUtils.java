package com.populstay.populife.util.locale;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * 语言缓存工具类
 */
public class SPUtils {

	private static volatile SPUtils instance;
	private final String SP_NAME = "language_setting";//缓存名称
	private final String TAG_LANGUAGE = "language_select";
	private final SharedPreferences mSharedPreferences;

	private Locale systemCurrentLocal = Locale.CHINESE;//系统当前本地语言为中文 初始值

	private final String LAST_SELECT_WIFI_SSID = "last_select_wifi_ssid";
	private final String LAST_SELECT_WIFI_PWD = "last_select_wifi_pwd";

	public SPUtils(Context context) {
		//通过上下文获取本地缓存
		mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 获取实例
	 *
	 * @param context 上下文参数
	 * @return SPUtils实例对象
	 */
	public static SPUtils getInstance(Context context) {
		if (instance == null) {//等于空则重新创建实例，不为空则直接返回
			synchronized (SPUtils.class) {//增加一个同步锁,如果已经有了实例则跳出
				if (instance == null) {
					//创建新的对象
					instance = new SPUtils(context);
				}
			}
		}
		return instance;
	}

	/**
	 * 获取语言
	 *
	 * @return 从缓存中根据缓存名或者缓存值，如果没有，则返回默认值0
	 */
	public int getLanguage() {
		return mSharedPreferences.getInt(TAG_LANGUAGE, 0);
	}

	/**
	 * 设置语言
	 *
	 * @param select 选中的语言项
	 */
	public void setLanguage(int select) {
		//缓存编辑者
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		//放入保存的语言项
		editor.putInt(TAG_LANGUAGE, select);
		//提交 之后 缓存语言项保存完毕
		editor.commit();
	}

	/**
	 * 获取系统当前本地
	 *
	 * @return 系统当前本地
	 */
	public Locale getSystemCurrentLocal() {
		return systemCurrentLocal;
	}

	/**
	 * 设置系统当前本地
	 *
	 * @param local 本地对象
	 */
	public void setSystemCurrentLocal(Locale local) {
		systemCurrentLocal = local;
	}

	public void saveLastWifi(String wifiSSID) {
		//缓存编辑者
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		//放入保存的语言项
		editor.putString(LAST_SELECT_WIFI_SSID, wifiSSID);
		//提交 之后 缓存语言项保存完毕
		editor.apply();
	}

	public String getWifiSSID() {
		return mSharedPreferences.getString(LAST_SELECT_WIFI_SSID, "");
	}

	public void saveLastWifiPwd(String wifiPwd) {
		//缓存编辑者
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		//放入保存的语言项
		editor.putString(LAST_SELECT_WIFI_PWD, wifiPwd);
		//提交 之后 缓存语言项保存完毕
		editor.apply();
	}

	public String getWifiPwd() {
		return mSharedPreferences.getString(LAST_SELECT_WIFI_PWD, "");
	}

}
