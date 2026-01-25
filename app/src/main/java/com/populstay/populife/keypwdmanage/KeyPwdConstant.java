package com.populstay.populife.keypwdmanage;

public class KeyPwdConstant {

	public interface IFrom {
		// 来自锁详情页面
		int FROM_LOCK_DETAILS = 1;
		// 来自密钥更多设置页面
		int FROM_MORE = 2;

		// 来自指纹/门卡页面（用于添加指纹/门卡时的引导页面，与添加门锁共用 layout 页面）
		int FROM_FINGERPRINT_CARD = 3;
	}


	public interface IType {
		// 钥匙
		int TYPE_KEY = 1;
		// 密码
		int TYPE_PWD = 2;
		// 指纹
		int TYPE_FINGERPRINT = 3;
		// 门卡
		int TYPE_IC_CARD = 4;
	}

	public interface IKeyPwdType {

		// 永久性密码
		String KEY_PWD_TYPE_PERMANENT = "KEY_PWD_TYPE_PERMANENT";
		// 限时密码
		String KEY_PWD_TYPE_PERIOD = "KEY_PWD_TYPE_PERIOD";
		// 一次性密码
        String KEY_PWD_TYPE_ONE_TIME = "KEY_PWD_TYPE_ONE_TIME";
//        循环密码
        String KEY_PWD_TYPE_RECURRING = "KEY_PWD_TYPE_RECURRING";
		// 自定义密码
		String KEY_PWD_TYPE_CUSTOM = "KEY_PWD_TYPE_CUSTOM";
		// 蓝牙钥匙
		String KEY_PWD_TYPE_KEY_BT_KEY = "KEY_PWD_TYPE_KEY_BT_KEY";
	}

	public interface IKeyPwdCategory {
		// 分类，1：可使用,2：待激活,3：已失效
		int KEY_PWD_CATEGORY_AVAILABLE = 1;
		int KEY_PWD_CATEGORY_NOT_ACTIVATED = 2;
		int KEY_PWD_CATEGORY_INVALID = 3;
	}

	public interface IBTKeyType {
		//钥匙类型（1限时，2永久，3单次）
		int TIME_LIMITED = 1;
		int PERMANENT = 2;
	}

	public interface IFingerprintCardValidType {
		// 指纹/门卡有效类型（1 限时，2 永久）
		int TIME_LIMITED = 1;
		int PERMANENT = 2;
	}

	public interface IBTKeyShareThrough {
		//分享类型(1账号,2短信链接)
		int ACCOUNT = 1;
		int SMS_LINK = 2;
	}

	public interface IBTKeyPermissionType {
		//是否授权(Is key authorized:0-NO,1-yes)
		int AUTH = 1;
		int NO_AUTH = 0;
	}
}
