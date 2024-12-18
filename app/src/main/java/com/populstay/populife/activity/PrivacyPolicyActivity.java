package com.populstay.populife.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.util.locale.LanguageUtil;

import static android.webkit.WebSettings.TextSize.SMALLER;

public class PrivacyPolicyActivity extends BaseActivity {

	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy_policy);

		initView();
	}

	private void initView() {
		((TextView)findViewById(R.id.page_title)).setText(R.string.terms_of_use);
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mWebView = findViewById(R.id.wv_privacy_policy);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(false);
		settings.setSupportZoom(false);
		settings.setTextSize(SMALLER);
		settings.setBuiltInZoomControls(false);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setDefaultFontSize(18);
		settings.setUserAgentString("User-Agent:Android");

		/*String filePath = LanguageUtil.isChinese(this) ? "file:///android_asset/privacy_policy_cn.html"
				: "file:///android_asset/privacy_policy_en.html";*/

		String filePath = LanguageUtil.isChinese(this) ? "https://www.populife.co/pages/populife-app-privacy-policy-cn"
				: "https://www.populife.co/pages/populife-app-privacy-policy";
		mWebView.loadUrl(filePath);
	}

	@Override
	protected void queryLatestDeviceId() {

	}
}
