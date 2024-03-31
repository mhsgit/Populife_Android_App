package com.populstay.populife.util.pkg;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.populstay.populife.R;
import com.populstay.populife.util.toast.ToastUtil;

import androidx.annotation.NonNull;

/**
 * Created by Jerry
 */
public class ThirdAppUtil {

	/**
	 * description: 国外热门 App 包名
	 **/
	public static final String INSTAGRAM = "com.instagram.android";
	public static final String FACE_BOOK = "com.facebook.katana";
	public static final String MESSENGER = "com.facebook.orca";
	public static final String WHATS_APP = "com.whatsapp";
	public static final String GMAIL = "com.google.android.gm";
	public static final String GOOGLE_MAP = "com.google.android.apps.maps";
	public static final String ALLO = "com.google.android.apps.fireball";

	/**
	 * description: 国内热门 App 包名
	 **/
	public static final String MEITUAN_WAIMAI = "com.sankuai.meituan.takeoutnew";
	public static final String E_LE_ME = "me.ele";
	public static final String MO_BAI = "com.mobike.mobikeapp";
	public static final String OFO = "so.ofo.labofo";
	public static final String JIN_RI_TOU_TIAO = "com.ss.android.article.news";
	public static final String SINA_WEI_BO = "com.sina.weibo";
	public static final String WANG_YI_XIN_WEN = "com.netease.newsreader.activity";
	public static final String KUAI_SHOU = "com.smile.gifmaker";
	public static final String ZHI_HU = "com.zhihu.android";
	public static final String HU_YA_ZHI_BO = "com.duowan.kiwi";
	public static final String YING_KE_ZHI_BO = "com.meelive.ingkee";
	public static final String MIAO_PAI = "com.yixia.videoeditor";
	public static final String MEI_TU_XIU_XIU = "com.mt.mtxx.mtxx";
	public static final String MEI_YAN_XIANG_JI = "com.meitu.meiyancamera";
	public static final String XIE_CHENG = "ctrip.android.view";
	public static final String MO_MO = "com.immomo.momo";
	public static final String YOU_KU = "com.youku.phone";
	public static final String AI_QI_YI = "com.qiyi.video";
	public static final String DI_DI = "com.sdu.didi.psnger";
	public static final String ZHI_FU_BAO = "com.eg.android.AlipayGphone";
	public static final String TAO_BAO = "com.taobao.taobao";
	public static final String JING_DONG = "com.jingdong.app.mall";
	public static final String DA_ZONG_DIAN_PING = "com.dianping.v1";
	public static final String JIAN_SHU = "com.jianshu.haruki";
	public static final String BAI_DU_DI_TU = "com.baidu.BaiduMap";
	public static final String GAO_DE_DI_TU = "com.autonavi.minimap";
	public static final String WEI_XIN = "com.tencent.mm";
	public static final String QQ = "com.tencent.mobileqq";

	/**
	 * 用手机浏览器打开链接
	 */
	public static void startPhoneBrowser(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri uri = Uri.parse(url);
		intent.setData(uri);
		context.startActivity(intent);
	}

	/**
	 * 判断第三方 App 是否已安装
	 *
	 * @param appPkgName App 包名
	 */
	public static boolean isAppInstalled(Context context, String appPkgName) {
		boolean installed;

		try {
			PackageManager pm = context.getPackageManager();
			pm.getPackageInfo(appPkgName, PackageManager.GET_ACTIVITIES);
			pm.getApplicationInfo(appPkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}

		return installed;
	}

	/**
	 * 打开淘宝 App
	 */
	public static void startTaobaoApp(Context context) {
		if (isAppInstalled(context, TAO_BAO)) {
			Intent intent = new Intent();
			intent.setAction("Android.intent.action.VIEW");
			intent.setClassName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			ToastUtil.showToast(R.string.hint_enter_taobao_app);
		}
	}

	/**
	 * 跳转到淘宝 App 首页
	 */
	public static void startTaobaoMainActivity(Context context) {
		if (isAppInstalled(context, TAO_BAO)) {
			Intent intent = new Intent();
			intent.setAction("Android.intent.action.VIEW");
			intent.setClassName("com.taobao.taobao", "com.taobao.tao.homepage.MainActivity3");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			ToastUtil.showToast(R.string.hint_enter_taobao_app);
		}
	}

	/**
	 * 跳转到淘宝商家店铺页面
	 *
	 * @param taobaoShopPath 店铺首页 url
	 */
	public static void startTaobaoShop(Context context, String taobaoShopPath) {
		Intent intent = new Intent();
		intent.setAction("Android.intent.action.VIEW");
		Uri uri = Uri.parse(taobaoShopPath);
		intent.setData(uri);
		intent.setClassName(TAO_BAO, "com.taobao.android.shop.activity.ShopHomePageActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 跳转到淘宝 App 商品详情页面
	 *
	 * @param tbGoodsUrl 淘宝商品详情页 url
	 */
	public static void startTaobaoAppGoodsDetail(Context context, @NonNull String tbGoodsUrl) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri uri = Uri.parse(tbGoodsUrl);
		intent.setData(uri);
		intent.setClassName(TAO_BAO, "com.taobao.tao.detail.activity.DetailActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}