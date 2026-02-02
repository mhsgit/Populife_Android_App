package com.populstay.populife.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.populstay.populife.R;
import com.populstay.populife.app.MyApplication;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.databinding.ActivityLockNotificationBinding;
import com.populstay.populife.entity.Key;
import com.populstay.populife.helper.ClickableTextHelper;

public class LockNotificationActivity extends BaseActivity {
    private final Key mKey = MyApplication.CURRENT_KEY;
    private ActivityLockNotificationBinding binding;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LockNotificationActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLockNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView(){
        binding.titleBar.pageTitle.setText(getString(R.string.notification));
        binding.titleBar.pageAction.setVisibility(View.GONE);
        ClickableTextHelper.setClickableText(binding.tvCombined,
                getString(R.string.setting_transfer_link_pre),
                getString(R.string.setting_transfer_link),
                getColor(R.color.text_main),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LockSendEkeyActivity.actionStart(LockNotificationActivity.this, mKey.getLockId(), mKey.isAdmin(), mKey);
                    }
                });
        binding.tvContine.setOnClickListener(v -> {
//            LockTransferConfirmActivity.actionStart(LockNotificationActivity.this);
        });
    }
}
