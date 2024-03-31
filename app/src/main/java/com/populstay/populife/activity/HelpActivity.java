package com.populstay.populife.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.imageloader.MQImage;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.ui.MQGlideImageLoader;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class HelpActivity extends BaseActivity {

    private TextView mTvPageTitle;
    private ImageView mIvNewMsg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_layout);
        initView();

    }

    private void initView() {
        mTvPageTitle = findViewById(R.id.page_left_title);
        mTvPageTitle.setVisibility(View.VISIBLE);
        mTvPageTitle.setText(R.string.lock_paired_goto_help);
        findViewById(R.id.page_title).setVisibility(View.GONE);
        initTitleBarRightBtn();
    }

    private void initTitleBarRightBtn() {
        findViewById(R.id.page_action).setVisibility(View.GONE);

        mIvNewMsg = findViewById(R.id.iv_main_lock_msg_new);
        View tvSupport = findViewById(R.id.rl_main_lock_online_service);
        tvSupport.setVisibility(View.VISIBLE);
        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRuntimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new PermissionListener() {
                            @Override
                            public void onGranted() {
                                HashMap<String, String> clientInfo = new HashMap<>();
                                clientInfo.put("userId", PeachPreference.readUserId());
                                clientInfo.put("phoneNum", PeachPreference.getStr(PeachPreference.ACCOUNT_PHONE));
                                clientInfo.put("email", PeachPreference.getStr(PeachPreference.ACCOUNT_EMAIL));
                                MQImage.setImageLoader(new MQGlideImageLoader());
                                startActivity(new MQIntentBuilder(HelpActivity.this).
                                        setCustomizedId(PeachPreference.readUserId())
                                        .setClientInfo(clientInfo)
                                        .updateClientInfo(clientInfo)
                                        .build());
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                toast(R.string.note_permission_external_storage);
                            }
                        });

            }
        });
    }

    /**
     * 获取美洽未读消息
     */
    private void getMeiQiaUnreadMsg() {
        MQManager.getInstance(this).getUnreadMessages(new OnGetMessageListCallback() {
            @Override
            public void onSuccess(List<MQMessage> messageList) {
                PeachLogger.d(messageList);
                if (messageList != null && !messageList.isEmpty())
                    mIvNewMsg.setVisibility(View.VISIBLE);
                else
                    mIvNewMsg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int code, String message) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMeiQiaUnreadMsg();
    }
}
