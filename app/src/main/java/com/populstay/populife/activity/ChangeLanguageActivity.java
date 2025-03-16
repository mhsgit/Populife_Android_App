package com.populstay.populife.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.common.Urls;
import com.populstay.populife.net.RestClient;
import com.populstay.populife.net.callback.IFailure;
import com.populstay.populife.net.callback.ISuccess;
import com.populstay.populife.util.locale.LanguageUtil;
import com.populstay.populife.util.locale.LocalManageUtils;
import com.populstay.populife.util.locale.SPUtils;
import com.populstay.populife.util.log.PeachLogger;

import java.util.Locale;

import androidx.appcompat.widget.LinearLayoutCompat;

public class ChangeLanguageActivity extends BaseActivity implements View.OnClickListener {

	public static final String FROM_ENTRY_KEY = "from_entry";
	public static final String FROM_ENTRY_VAL_SIGN = "from_entry_val_sign";
	private String mFromEntry;
	private TextView mTvSave;
	private LinearLayoutCompat mLlSystem, mLlEnglish, mLlChinese, mLlJapanese, mLlFrench, mLlGerman, mLlItalian, mLlSpanish;
	private ImageView mIvSystem, mIvEnglish, mIvChinese, mIvJapanese, mIvFrench, mIvGerman, mIvItalian, mIvSpanish;

	private int mLanguageType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_language);
		getIntentData();
		initView();
		initListener();
	}

	private void getIntentData() {
		mFromEntry = getIntent().getStringExtra(FROM_ENTRY_KEY);
	}

	private void initView() {
		((TextView) findViewById(R.id.page_title)).setText(R.string.switch_language);
		mTvSave = findViewById(R.id.page_action);
		mTvSave.setText(R.string.save);

		mLlSystem = findViewById(R.id.ll_change_language_system);
		mLlEnglish = findViewById(R.id.ll_change_language_english);
		mLlChinese = findViewById(R.id.ll_change_language_simplified_chinese);
		mLlJapanese = findViewById(R.id.ll_change_language_japanese);
		mLlFrench = findViewById(R.id.ll_change_language_french);
		mLlGerman = findViewById(R.id.ll_change_language_german);
		mLlItalian = findViewById(R.id.ll_change_language_italian);
		mLlSpanish = findViewById(R.id.ll_change_language_spanish);
		mIvSystem = findViewById(R.id.iv_change_language_system);
		mIvEnglish = findViewById(R.id.iv_change_language_english);
		mIvChinese = findViewById(R.id.iv_change_language_simplified_chinese);
		mIvJapanese = findViewById(R.id.iv_change_language_japanese);
		mIvFrench = findViewById(R.id.iv_change_language_french);
		mIvGerman = findViewById(R.id.iv_change_language_german);
		mIvItalian = findViewById(R.id.iv_change_language_italian);
		mIvSpanish = findViewById(R.id.iv_change_language_spanish);

//		int type = LanguageUtil.getLanguageType(this);
		int type = SPUtils.getInstance(this).getLanguage();
		refreshLanguageUI(type);

		PeachLogger.e("系统", LocalManageUtils.getSystemLocal(this).getDisplayLanguage());
		PeachLogger.e("当前", LocalManageUtils.getSelectLanguage(this));
	}

	private void refreshLanguageUI(int languageType) {
		switch (languageType) {
			case 0: // 跟随系统
				mLanguageType = 0;
				mIvSystem.setVisibility(View.VISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;

			case 1: // 英文
				mLanguageType = 1;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.VISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;

			case 2: // 简体中文
				mLanguageType = 2;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.VISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;

			case 3: // 日语
				mLanguageType = 3;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.VISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;

			case 4: // 法语
				mLanguageType = 4;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.VISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;

			case 5: // 德语
				mLanguageType = 5;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.VISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;
			case 6: // 意大利语
				mLanguageType = 6;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.VISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;
			case 7: // 西班牙语
				mLanguageType = 7;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.INVISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvGerman.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.VISIBLE);
				break;
				// 德语  法语 意大利语 西班牙语

			default: // 默认英语
				mLanguageType = 1;
				mIvSystem.setVisibility(View.INVISIBLE);
				mIvEnglish.setVisibility(View.VISIBLE);
				mIvChinese.setVisibility(View.INVISIBLE);
				mIvJapanese.setVisibility(View.INVISIBLE);
				mIvFrench.setVisibility(View.INVISIBLE);
				mIvItalian.setVisibility(View.INVISIBLE);
				mIvSpanish.setVisibility(View.INVISIBLE);
				break;
		}
	}

	private void initListener() {
		mTvSave.setOnClickListener(this);
		mLlSystem.setOnClickListener(this);
		mLlEnglish.setOnClickListener(this);
		mLlChinese.setOnClickListener(this);
		mLlJapanese.setOnClickListener(this);
		mLlFrench.setOnClickListener(this);
		mLlGerman.setOnClickListener(this);
		mLlItalian.setOnClickListener(this);
		mLlSpanish.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.page_action:
				changeLanguage();
				break;

			case R.id.ll_change_language_system:
				refreshLanguageUI(0);
				break;

			case R.id.ll_change_language_english:
				refreshLanguageUI(1);
				break;

			case R.id.ll_change_language_simplified_chinese:
				refreshLanguageUI(2);
				break;

			case R.id.ll_change_language_japanese:
				refreshLanguageUI(3);
				break;

			case R.id.ll_change_language_french:
				refreshLanguageUI(4);
				break;

			case R.id.ll_change_language_german:
				refreshLanguageUI(5);
				break;
			case R.id.ll_change_language_italian:
				refreshLanguageUI(6);
				break;

			case R.id.ll_change_language_spanish:
				refreshLanguageUI(7);
				break;

			default:
				break;
		}
	}

	/**
	 * 选择语言后，重新进入setting页面
	 *
	 * @param context
	 * @param select
	 */
	public void reStart(Context context, int select) {
		LocalManageUtils.setSelectLanguage(this, select);

		finish();
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void changeLanguage() {
//		boolean sameLanguage = LanguageUtil.isSameLanguage(mLanguageType);
//		if (!sameLanguage) {
//			LanguageUtil.setLocale(mLanguageType);
//			// 设置完语言后缓存type
//			LanguageUtil.putLanguageType(mLanguageType);
//			if (FROM_ENTRY_VAL_SIGN.equals(mFromEntry)){
//				LanguageUtil.toRestartSignActvity(this);
//			}else {
//				LanguageUtil.toRestartMainActvity(this);
//			}
//			sendCurrentLanguage();
//		}else {
//			// 设置完语言后缓存type
//			LanguageUtil.putLanguageType(mLanguageType);
//			finish();
//		}

		reStart(this, mLanguageType);
	}

	/**
	 * 请求服务器，告知当前语言环境
	 */
	private void sendCurrentLanguage() {
		Locale locale = LanguageUtil.getLocaleByType(mLanguageType);
		RestClient.builder()
				.url(Urls.USER_LANGUAGE_SWITCH)
				.loader(ChangeLanguageActivity.this)
				// 语种，取值：en_US（英语），zh_CN（简体中文），ja_JP（日语）
				.params("locale", locale)
				.success(new ISuccess() {
					@Override
					public void onSuccess(String response) {
						finish();
					}
				})
				.failure(new IFailure() {
					@Override
					public void onFailure() {
						finish();
					}
				})
				.build()
				.get();
	}
}
