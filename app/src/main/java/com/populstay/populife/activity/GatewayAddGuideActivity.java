package com.populstay.populife.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.imageloader.MQImage;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.populstay.populife.R;
import com.populstay.populife.base.BluetoothBaseActivity;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.ui.MQGlideImageLoader;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class GatewayAddGuideActivity extends BluetoothBaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

	private static final String KEY_LOCK_TYPE = "KEY_LOCK_TYPE";

	private TextView mTvPageTitle, mTvNext;
	private CheckBox mCbConfirmActivateDevice;
	private CheckBox mCbConfirmGatewayReconnect;
	private ImageView mIvNewMsg;

	public static void actionStartByTaskTop(Context context, String lockType) {
		Intent intent = new Intent(context, GatewayAddGuideActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(KEY_LOCK_TYPE, lockType);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gateway_add_guide);
		initView();
		setListener();
		initStatus();
	}

	private void initView() {
		mTvPageTitle = findViewById(R.id.page_left_title);
		mTvPageTitle.setVisibility(View.VISIBLE);
		findViewById(R.id.page_title).setVisibility(View.GONE);
		mTvNext = findViewById(R.id.tv_next_gateway_add_guide);
		mCbConfirmActivateDevice = findViewById(R.id.cb_confirm_activate_device);
		mCbConfirmGatewayReconnect = findViewById(R.id.cb_confirm_gateway_reconnect);

		initTitleBarRightBtn();
	}

	private void initTitleBarRightBtn() {
		/*TextView tvQuestion = findViewById(R.id.page_action);
		tvQuestion.setText("");
		tvQuestion.setCompoundDrawablesWithIntrinsicBounds(
				getResources().getDrawable(R.drawable.help_icon), null, null, null);

		tvQuestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PDFActivity.actionStart(GatewayAddGuideActivity.this, getString(R.string.user_manual_gateway),
						"user_manual_gateway.pdf", true);
			}
		});*/

		findViewById(R.id.page_action).setVisibility(View.GONE);
		mIvNewMsg = findViewById(R.id.iv_main_lock_msg_new);
		View tvSupport = findViewById(R.id.rl_main_lock_online_service);
		tvSupport.setVisibility(View.VISIBLE);
		tvSupport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startImServiceActivity(GatewayAddGuideActivity.this);
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

	private void setListener() {
		mTvNext.setOnClickListener(this);
		mCbConfirmActivateDevice.setOnCheckedChangeListener(this);
		mCbConfirmGatewayReconnect.setOnCheckedChangeListener(this);
	}

	private void initStatus() {
		mTvPageTitle.setText(R.string.add_lock_pre_check);
		// 默认不选中
		//startRefreshCountDownTimerUI();
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_next_gateway_add_guide) {
            if (isBleNetEnableWithToast()) {
                requestRuntimePermissions(
                        isAndroid12() ? PERMISSION_BLE_SCAN_CONNECT
                                : new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        new PermissionListener() {
                            @Override
                            public void onGranted() {
                                // 开启蓝牙扫描
                                goToNewActivity(GatewayAddActivity.class);
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions) {
                                toast(isAndroid12() ? R.string.note_permission_ble_scan_connect
                                        : R.string.note_permission_lbs);
                            }
                        });
            }
        }
    }


    @Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (mCbConfirmActivateDevice.isChecked() && mCbConfirmGatewayReconnect.isChecked()) {
			startRefreshCountDownTimerUI();
		} else {
			resetRefreshCountDownTimerUI();
		}
	}

	public void startRefreshCountDownTimerUI() {
		mCbConfirmActivateDevice.setChecked(true);
		mCbConfirmGatewayReconnect.setChecked(true);
		setNextBtnEnable();
	}

	public void resetRefreshCountDownTimerUI() {
		setNextBtnEnable();
	}

	private void setNextBtnEnable() {
		boolean enable = false;
		enable = mCbConfirmActivateDevice.isChecked() && mCbConfirmGatewayReconnect.isChecked();
		enable = enable && isBleEnableWithoutToast() && isLbsEnableWithoutToast();
		mTvNext.setEnabled(enable);
	}


	@Override
	public void onBluetoothStateChanged(boolean isOpen) {
		setNextBtnEnable();
	}

	@Override
	public void onLocationStateChanged(boolean isOpen) {
		setNextBtnEnable();
	}

}