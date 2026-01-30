package com.populstay.populife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.app.MyApplication;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.entity.Key;
import com.populstay.populife.enumtype.Operation;
import com.populstay.populife.lock.ILockModifyKeypadVolume;
import com.populstay.populife.util.storage.PeachPreference;
import com.ttlock.bl.sdk.callback.SetLockConfigCallback;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.TTLockConfigType;

import static com.populstay.populife.app.MyApplication.mTTLockAPI;

public class LockSoundActivity extends BaseActivity {

	public static final String KEY_LOCK_SOUND = "key_keypad_volume";

	private TextView mTvCurMode, mTvSwitch;

	private Key mKey = MyApplication.CURRENT_KEY;
	private int mLockSoundState; // 0 off, 1 on

	/**
	 * 启动当前 activity
	 *
	 * @param context   上下文
	 * @param lockSound 键盘按键音（0 off, 1 on）
	 */
	public static void actionStart(Activity context, int lockSound, int requestCode) {
		Intent intent = new Intent(context, LockSoundActivity.class);
		intent.putExtra(KEY_LOCK_SOUND, lockSound);
		context.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_sound);

		getIntentData();
		initView();
		initListener();
	}

	private void getIntentData() {
		mLockSoundState = getIntent().getIntExtra(KEY_LOCK_SOUND, 1);
	}

	private void initView() {
		((TextView) findViewById(R.id.page_title)).setText(R.string.lock_sound);
		findViewById(R.id.page_action).setVisibility(View.GONE);

		mTvCurMode = findViewById(R.id.tv_lock_sound_mode);
		mTvSwitch = findViewById(R.id.tv_lock_sound);

		refreshKeypadVolume();
	}

	private void refreshKeypadVolume() {
		if (mLockSoundState == 1) { // 键盘按键音已关闭
			mTvCurMode.setText(R.string.on);
			mTvSwitch.setText(R.string.turn_off);
		} else { // 键盘按键音已开启
			mTvCurMode.setText(R.string.off);
			mTvSwitch.setText(R.string.turn_on);
		}
	}

	private void initListener() {
		mTvSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isBleEnableWithToast()) {
					switchKeypadVolume();
				}
			}
		});
	}

	private void switchKeypadVolume() {
		showLoading();
        boolean value = mLockSoundState != 1;
        mTTLockAPI.setLockConfig(TTLockConfigType.LOCK_SOUND, value, mKey.getLockData(), new SetLockConfigCallback() {
            @Override
            public void onSetLockConfigSuccess(TTLockConfigType ttLockConfigType) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopLoading();
                        toastSuccess();
                        mLockSoundState = value ? 1 : 0;
                        refreshKeypadVolume();
                    }
                });
            }

            @Override
            public void onFail(LockError lockError) {
                stopLoading();
                toastFail();
            }
        });
	}

	@Override
	public void finishCurrentActivity(View view) {
		setResultToBack();
		super.finishCurrentActivity(view);
	}

	@Override
	public void onBackPressed() {
		setResultToBack();
		finish();
	}

	private void setResultToBack() {
		Intent data = new Intent();
		data.putExtra(LockSettingsActivity.KEY_RESULT_DATA,String.valueOf(mLockSoundState));
		setResult(RESULT_OK, data);
	}
}
