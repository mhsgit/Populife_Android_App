package com.populstay.populife.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.util.locale.LanguageUtil;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;

import java.util.HashMap;
import java.util.List;

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

	private LinearLayout mLlManualApp, mLlManualDeadbolt, mLlManualKeybox, mLlManualGateway, mLlQuestions,
			mLlFeedback, mLlSendEmail, mLlOnlineCommunication;
	private ImageView mIvNewMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_service);

		initView();
		initListener();
	}

	private void initView() {
		((TextView) findViewById(R.id.page_title)).setText(R.string.help_center);
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mLlManualApp = findViewById(R.id.ll_service_manual_app);
		mLlManualDeadbolt = findViewById(R.id.ll_service_manual_deadbolt);
		mLlManualKeybox = findViewById(R.id.ll_service_manual_keybox);
		mLlManualGateway = findViewById(R.id.ll_service_manual_gateway);
		mLlQuestions = findViewById(R.id.ll_service_questions);
		mLlFeedback = findViewById(R.id.ll_service_feedback);
		mLlSendEmail = findViewById(R.id.ll_service_send_email);
		mLlOnlineCommunication = findViewById(R.id.ll_service_online_communication);
		mIvNewMsg = findViewById(R.id.iv_service_online_new);
	}

	private void initListener() {
		mLlManualApp.setOnClickListener(this);
		mLlManualDeadbolt.setOnClickListener(this);
		mLlManualKeybox.setOnClickListener(this);
		mLlManualGateway.setOnClickListener(this);
		mLlQuestions.setOnClickListener(this);
		mLlFeedback.setOnClickListener(this);
		mLlSendEmail.setOnClickListener(this);
		mLlOnlineCommunication.setOnClickListener(this);
	}
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.ll_service_manual_app) {
            PDFActivity.actionStart(CustomerServiceActivity.this, getString(R.string.user_manual_app),
                    "user_manual_app.pdf", true);
        } else if (id == R.id.ll_service_manual_deadbolt) {
            PDFActivity.actionStart(CustomerServiceActivity.this, getString(R.string.user_manual_deadbolt),
                    "user_manual_deadbolt.pdf", true);
        } else if (id == R.id.ll_service_manual_keybox) {
            String pdfAssetName = "user_manual_keybox_en.pdf";
            if (LanguageUtil.isChinese(CustomerServiceActivity.this)) {
                pdfAssetName = "user_manual_keybox_cn.pdf";
            } else if (LanguageUtil.isJp(CustomerServiceActivity.this)) {
                pdfAssetName = "user_manual_keybox_jp.pdf";
            }
            PDFActivity.actionStart(CustomerServiceActivity.this, getString(R.string.user_manual_keybox),
                    pdfAssetName, true);
        } else if (id == R.id.ll_service_manual_gateway) {
            String pdfGatewayAssetName = "user_manual_gateway_en.pdf";
            if (LanguageUtil.isChinese(CustomerServiceActivity.this)) {
                pdfGatewayAssetName = "user_manual_gateway_cn.pdf";
            }
            PDFActivity.actionStart(CustomerServiceActivity.this, getString(R.string.user_manual_gateway),
                    pdfGatewayAssetName, true);
        } else if (id == R.id.ll_service_questions) {
            goToNewActivity(CommonQuestionActivity.class);
        } else if (id == R.id.ll_service_feedback) {
            goToNewActivity(FeedbackListActivity.class);
        } else if (id == R.id.ll_service_send_email) {
            sendEmail();
        } else if (id == R.id.ll_service_online_communication) {
            startImServiceActivity(CustomerServiceActivity.this);
        }
    }

	private void sendEmail() {
		// 创建Intent
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		//设置内容类型
		emailIntent.setType("message/rfc822");
		//设置额外信息
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.customer_service_email)});
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
		//启动Activity
		startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
	}
}
