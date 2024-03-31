package com.populstay.populife.util.locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import com.populstay.populife.R;

import java.util.Locale;

/**
 * 本地管理工具类
 */
public class LocalManageUtils {

	private static final String TAG = "LocalManage";

	/**
	 * 获取系统的 locale对象
	 *
	 * @param context 上下文参数
	 * @return locale对象
	 */
	public static Locale getSystemLocal(Context context) {
		return SPUtils.getInstance(context).getSystemCurrentLocal();
	}

	/**
	 * 获取当前APP使用的语言
	 *
	 * @param context 上下文参数
	 * @return 语言类型描述
	 */
	public static String getSelectLanguage(Context context) {
		//根据缓存中的语言返回当前设置的语言类型文字
		switch (SPUtils.getInstance(context).getLanguage()) {
			case 0:
				return context.getString(R.string.language_system);
			case 1:
				return context.getString(R.string.language_english);
			case 2:
				return context.getString(R.string.language_simplified_chinese);
			case 3:
				return context.getString(R.string.language_japanese);
			default:
				return context.getString(R.string.language_english);
		}
	}

	/**
	 * 获取选择的语言设置
	 *
	 * @param context 上下文参数
	 * @return locale
	 */
	public static Locale getSelectLanguageLocal(Context context) {
		Locale locale;
		switch (SPUtils.getInstance(context).getLanguage()) {
			case 0:
				locale = getSystemLocal(context);//系统语言
				break;

			case 1: // 英语
				locale = Locale.US;
				break;

			case 2: // 简体中文
				locale = Locale.SIMPLIFIED_CHINESE;
				break;

			case 3: // 日语
				locale = Locale.JAPAN;
				break;

			case 4: // 法语
				locale = Locale.FRANCE;
				break;

			case 5: // 德语
				locale = Locale.GERMAN;
				break;

			default: // 默认英语
				locale = Locale.US;
				break;
		}
		return locale;
	}

	/**
	 * 设置选中的语言
	 *
	 * @param context 上下文参数
	 * @param select  选中的语言项
	 */
	public static void setSelectLanguage(Context context, int select) {
		//放入缓存中
		SPUtils.getInstance(context).setLanguage(select);
		//设置APP语言
		setAppLanguage(context);
	}

	/**
	 * 设置App语言
	 *
	 * @param context
	 */
	public static void setAppLanguage(Context context) {
		//通过应用全局上下文获取资源对象
		Resources resources = context.getApplicationContext().getResources();
		//获取资源配置对象
		Configuration config = resources.getConfiguration();
		//获取系统本地对象
		Locale locale = getSelectLanguageLocal(context);
		//配置本地资源
		config.setLocale(locale);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0及以上
			//获取本地语言列表
			LocaleList localeList = new LocaleList(locale);
			//设置默认语言列表
			LocaleList.setDefault(localeList);
			//设置语言环境列表
			config.setLocales(localeList);
			//创建配置系统的上下文参数
			context.getApplicationContext().createConfigurationContext(config);
			//设置默认语言
			Locale.setDefault(locale);
		}
		//更新资源配置
		resources.updateConfiguration(config, resources.getDisplayMetrics());
	}

	/**
	 * 设置本地语言
	 *
	 * @param context 上下文参数
	 * @return 设置后的对象
	 */
	public static Context setLocale(Context context) {
		return updateResource(context, getSelectLanguageLocal(context));
	}

	/**
	 * 更新资源
	 *
	 * @param context 上下文参数
	 * @param locale  本地语言
	 * @return
	 */
	private static Context updateResource(Context context, Locale locale) {
		//设置默认语言
		Locale.setDefault(locale);
		//通过上下文获取资源对象
		Resources resources = context.getResources();
		//资源配置对象
		Configuration config = new Configuration(resources.getConfiguration());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//Android4.2及以上
			//设置语言
			config.setLocale(locale);
			//获取设置之后的上下文参数
			context = context.createConfigurationContext(config);
		} else {
			//配置语言
			config.setLocale(locale);
			//更新资源配置
			resources.updateConfiguration(config, resources.getDisplayMetrics());
		}
		return context;
	}

	/**
	 * 设置系统当前语言
	 *
	 * @param context
	 */
	public static void setSystemCurrentLanguage(Context context) {
		Locale locale;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0及以上
			locale = LocaleList.getDefault().get(0);
		} else {
			locale = Locale.getDefault();
		}
		Log.d(TAG, locale.getLanguage());
		//设置应用当前本地语言
		SPUtils.getInstance(context).setSystemCurrentLocal(locale);
	}

	/**
	 * 更改应用配置
	 *
	 * @param context 上下文参数
	 */
	public static void onConfigurationChanged(Context context) {
		setSystemCurrentLanguage(context);
		setLocale(context);
		setAppLanguage(context);
	}


}
