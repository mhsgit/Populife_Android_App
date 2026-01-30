package com.populstay.populife.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.core.callback.OnRegisterDeviceTokenCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
//import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.populstay.populife.R;
import com.populstay.populife.app.CrashHandler;
import com.populstay.populife.app.CustomUncaughtExceptionHandler;
import com.populstay.populife.constant.Constant;
import com.populstay.populife.ui.loader.PeachLoader;
import com.populstay.populife.util.device.DeviceUtil;
import com.populstay.populife.util.locale.LocalManageUtils;
import com.populstay.populife.util.log.LogToFile;
import com.populstay.populife.util.log.MyDiskLogStrategy;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.toast.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.ttlock.bl.sdk.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import androidx.annotation.NonNull;


/**
 * App 基类
 * Created by Jerry
 */

public class BaseApplication extends Application {

	@SuppressLint("HandlerLeak")
	private static final Handler HANDLER = new Handler();
	@SuppressLint("StaticFieldLeak")
	private static Context mContext;

	public static Handler getHandler() {
		return HANDLER;
	}

	/**
	 * 获取 Context
	 */
	public static Context getApplication() {
		return mContext;
	}

	@Override
	public void onCreate() {
		mContext = getApplicationContext();
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler(getApplicationContext()));

		// 初始化 开发/发布 模式（每次开发/发布时，需要修改 Constant.DEBUG 的值）
		initDebugMode(!Constant.DEBUG);

//		// 设置本地化语言
//		languageWork();

		//设置App的语言
		LocalManageUtils.setAppLanguage(this);
		Log.d(TAG,"onCreate");

		// 初始化分享
		//MobSDK.init(this);

		// 初始化美洽（在线客服）
		initMeiqiaSDK();

		CrashReport.initCrashReport(mContext, "c0b163a8b5", true);

		PeachLogger.d("App 构建时间 AppBuildTime=" + getAppBuildTime());
	}

	private String getAppBuildTime() {
		String result = "";
		try {
			ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
			long time = ze.getTime() * 1000;
			SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
			formatter.applyPattern("yyyy/MM/dd HH:mm:ss");
			result = formatter.format(new java.util.Date(time));
			zf.close();
		} catch (Exception e) {
		}

		return result;
	}

	private void initCollectCrashTool() {
		LogToFile.init(this);
		CrashHandler.getInstance().init(this);
	}

	private void initMeiqiaSDK() {
		MQConfig.init(this, Constant.MEI_QIA_APP_KEY, new OnInitCallback() {
			@Override
			public void onSuccess(String clientId) {
			}

			@Override
			public void onFailure(int code, String message) {
			}
		});

		customMeiqiaSDK();

		MQManager.getInstance(this).registerDeviceToken(DeviceUtil.getDeviceId(this), new OnRegisterDeviceTokenCallback() {
			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int i, String s) {
			}
		});
//		MQManager.getInstance(this).closeMeiqiaService();
	}

	/**
	 * （可选）配置美洽自定义信息
	 */
	private void customMeiqiaSDK() {

		MQConfig.ui.titleGravity = MQConfig.ui.MQTitleGravity.LEFT;
		MQConfig.ui.backArrowIconResId = R.drawable.ic_back;
		MQConfig.ui.titleBackgroundResId = R.color.colorPrimary;
		MQConfig.ui.titleTextColorResId = R.color.white;
//		MQConfig.ui.leftChatBubbleColorResId = R.color.test_green;
//		MQConfig.ui.leftChatTextColorResId = R.color.test_red;
//		MQConfig.ui.rightChatBubbleColorResId = R.color.test_red;
//		MQConfig.ui.rightChatTextColorResId = R.color.test_green;
//		MQConfig.ui.robotEvaluateTextColorResId = R.color.test_red;
//		MQConfig.ui.robotMenuItemTextColorResId = R.color.test_blue;
//		MQConfig.ui.robotMenuTipTextColorResId = R.color.test_blue;
	}

	/**
	 * 初始化 开发/发布 模式
	 *
	 * @param isDebug 是否开发（调试）模式
	 *                开发模式：true
	 *                发布模式：false
	 */
	private void initDebugMode(boolean isDebug) {
		// 测试环境异常收集
		//initCollectCrashTool();
		// Logger 日志
		initLogger(Constant.IS_SHOW_LOG);
		// TTLock SDK
		LogUtil.setDBG(isDebug);
//		com.ttlock.gateway.sdk.util.LogUtil.setDBG(isDebug);
		// 美洽
		MQManager.setDebugMode(isDebug);
	}

	public static final String TAG = "BaseApplication";

	@Override
	protected void attachBaseContext(Context base) {
		//设置系统当前语言
		LocalManageUtils.setSystemCurrentLanguage(base);
		super.attachBaseContext(LocalManageUtils.setLocale(base));
		Log.d(TAG,"attachBaseContext");
	}

	@Override
	public void onConfigurationChanged(@NonNull Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//通过全局的上下文参数更改相关资源配置
		LocalManageUtils.onConfigurationChanged(getApplicationContext());
		Log.d(TAG,"onConfigurationChanged");
	}

//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		languageWork();
//	}

//	private void languageWork() {
//		int type = LanguageUtil.getLanguageType(this);
//		LanguageUtil.setLocale(type);
//	}

	/**
	 * 初始化 logger 日志工具
	 *
	 * @param isDebug 是否开发（调试）模式
	 *                开发模式：true
	 *                发布模式：false
	 */
	private void initLogger(boolean isDebug) {
		if (isDebug) {
			// 本地log打印
			FormatStrategy formatStrategy1 = PrettyFormatStrategy.newBuilder()
					.tag(PeachLogger.LOGGER_TAG)
					.build();

			// 本地log记录
			String folder = getExternalCacheDir().getAbsolutePath() + File.separator + "logs";
			HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
			ht.start();
			Handler handler = new MyDiskLogStrategy.WriteHandler(ht.getLooper(), folder, 500 * 1024);
			LogStrategy logStrategy = new MyDiskLogStrategy(handler);

			FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
					.logStrategy(logStrategy)
					.tag(PeachLogger.LOGGER_TAG)
					.build();

			Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
			Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy1));
		} else {
			// 禁用 logger
			Logger.addLogAdapter(new AndroidLogAdapter() {
				@Override
				public boolean isLoggable(int priority, String tag) {
					return BuildConfig.DEBUG;
				}
			});
		}

	}

	public void myToast(int resId) {
		ToastUtil.showToast(resId);
	}

	public void myToast(final String msg) {
		ToastUtil.showToast(msg);
	}

	public void showLoading() {
		PeachLoader.showLoading(this);
	}

	public void stopLoading() {
		PeachLoader.stopLoading();
	}
}
