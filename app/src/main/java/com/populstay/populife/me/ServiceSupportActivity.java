package com.populstay.populife.me;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.activity.CommonQuestionActivity;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.maintservice.MaintServiceActivity;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.util.storage.PeachPreference;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class ServiceSupportActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mLlServiceSupportHelp, mLlServiceSupportMaintain, mLlServiceSupportSendEmail, mLlServiceSupportCustomer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_support);
        initView();
        setListener();
    }

    private void initView() {
        initTitleBar();
        mLlServiceSupportHelp = findViewById(R.id.ll_service_support_help);
        mLlServiceSupportMaintain = findViewById(R.id.ll_service_support_maintain);
        mLlServiceSupportSendEmail = findViewById(R.id.ll_service_support_send_email);
        mLlServiceSupportCustomer = findViewById(R.id.ll_service_support_customer);
    }

    private void initTitleBar() {
        ((TextView)findViewById(R.id.page_title)).setText(R.string.me_list_item_name_service);
        findViewById(R.id.page_action).setVisibility(View.GONE);
    }

    private void setListener() {
        mLlServiceSupportHelp.setOnClickListener(this);
        mLlServiceSupportMaintain.setOnClickListener(this);
        mLlServiceSupportSendEmail.setOnClickListener(this);
        mLlServiceSupportCustomer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.ll_service_support_help) {
            goToNewActivity(CommonQuestionActivity.class);

        } else if (id == R.id.ll_service_support_maintain) {
            goToNewActivity(MaintServiceActivity.class);

        } else if (id == R.id.ll_service_support_send_email) {
            sendEmail();

        } else if (id == R.id.ll_service_support_customer) {
            onlineCustomer();
        }
    }

    private void onlineCustomer(){
        startImServiceActivity(ServiceSupportActivity.this);
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
