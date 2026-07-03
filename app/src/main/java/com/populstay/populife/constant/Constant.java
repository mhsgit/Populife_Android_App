package com.populstay.populife.constant;

/**
 * Created by Jerry
 */
public class Constant {
	/**
	 * 账号类型
	 * 1 手机
	 * 2 邮箱
	 */
	public static final int ACCOUNT_TYPE_PHONE = 1;
	public static final int ACCOUNT_TYPE_EMAIL = 2;


	// false 正式环境，true 测试环境（每次开发/发布时，需要修改 DEBUG 的值）
	public static final boolean DEBUG = false;

	public static final boolean IS_SHOW_LOG = true;

	// 蓝牙钥匙分享链接跳转配置参数名称
	public static final String SHARE_KEY_PARAM_PRE_ID = "preId";

	public static final String TEST_ACCOUNT_DEVICE_ID = "testAccDevId"; // 测试账号，固定 deviceId
	// 测试账号，不检测异地登录，直接登录进入主页
	public static String[] mIgnoreRemoteLoginAccountArr = {"test@populife.co","+8613201812820","cammyfu@163.com","mohuansheng888@gmail.com"};
}
