package com.populstay.populife.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.core.MQManager;
import com.meiqia.core.bean.MQMessage;
import com.meiqia.core.callback.OnGetMessageListCallback;
import com.meiqia.meiqiasdk.imageloader.MQImage;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;
import com.populstay.populife.eventbus.Event;
import com.populstay.populife.fragment.LockDetailFragment;
import com.populstay.populife.permission.PermissionListener;
import com.populstay.populife.ui.MQGlideImageLoader;
import com.populstay.populife.ui.NoScrollViewPager;
import com.populstay.populife.util.log.PeachLogger;
import com.populstay.populife.util.storage.PeachPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LockDetailActivity extends BaseActivity {

	private static final String KEY_KEY_ID = "key_key_id";
	public static final String KEY_LOCK_TYPE = "KEY_LOCK_TYPE";
	public static int mCurrentFragmentIndex = 0;
	private ImageView mIvAddLock, mIvNewMsg;
	private RelativeLayout mRlOnlineService;
	private NoScrollViewPager mViewPager;
	private String mKeyId;//钥匙 id
	private String mLockType;//锁类型
	private TextView  mPageTitle;

	/**
	 * 启动当前 activity
	 *
	 * @param context 上下文
	 * @param keyId   钥匙 id
	 */
	public static void actionStart(Context context, String keyId, String devType) {
		Intent intent = new Intent(context, LockDetailActivity.class);
		intent.putExtra(KEY_LOCK_TYPE,devType);
		intent.putExtra(KEY_KEY_ID, keyId);
		intent.putExtra(KEY_LOCK_TYPE, devType);
		context.startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 刷新子 fragment 页面
		refreshChildFragment();
		getMeiQiaUnreadMsg();
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_detail);
		mKeyId = getIntent().getStringExtra(KEY_KEY_ID);
		mLockType = getIntent().getStringExtra(KEY_LOCK_TYPE);
		initView();
	}

	public void setTitleName(String titleName){
		if (null != mPageTitle){
			mPageTitle.setText(titleName);
		}
	}

	private void initView() {
		mPageTitle = findViewById(R.id.page_title);
		initTitleBarRightBtn();
		mViewPager = findViewById(R.id.nsv_lock_detail);
		setupViewPager(mViewPager);
	}

	private boolean isClickSupportRequestRuntimePermissions = false;
	private void initTitleBarRightBtn() {
		findViewById(R.id.page_action).setVisibility(View.GONE);
		mIvNewMsg = findViewById(R.id.iv_main_lock_msg_new);
		View tvSupport = findViewById(R.id.rl_main_lock_online_service);
		tvSupport.setVisibility(View.VISIBLE);
		tvSupport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isClickSupportRequestRuntimePermissions = true;
				startImServiceActivity(LockDetailActivity.this);
//				requestRuntimePermissions(PERMISSION_IMAGES,
//						new PermissionListener() {
//							@Override
//							public void onGranted() {
//								isClickSupportRequestRuntimePermissions = false;
//								HashMap<String, String> clientInfo = new HashMap<>();
//								clientInfo.put("userId", PeachPreference.readUserId());
//								clientInfo.put("phoneNum", PeachPreference.getStr(PeachPreference.ACCOUNT_PHONE));
//								clientInfo.put("email", PeachPreference.getStr(PeachPreference.ACCOUNT_EMAIL));
//								MQImage.setImageLoader(new MQGlideImageLoader());
//								startActivity(new MQIntentBuilder(LockDetailActivity.this).
//										setCustomizedId(PeachPreference.readUserId())
//										.setClientInfo(clientInfo)
//										.updateClientInfo(clientInfo)
//										.build());
//							}
//
//							@Override
//							public void onDenied(List<String> deniedPermissions) {
//								isClickSupportRequestRuntimePermissions = false;
//								toast(R.string.note_permission_external_storage);
//							}
//						});

			}
		});
	}

	private void setupViewPager(ViewPager viewPager) {
		ViewPagerAdapter localViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		localViewPagerAdapter.addFragment(LockDetailFragment.newInstance(LockDetailFragment.VAL_TAG_ACTIVITY, mKeyId,mLockType));
		viewPager.setAdapter(localViewPagerAdapter);
	}

	private void refreshChildFragment() {
		ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
		if (adapter != null) {
			LockDetailFragment fragment = (LockDetailFragment) adapter.getItem(0);
			if (fragment != null) {
				fragment.doRefresh();
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (isClickSupportRequestRuntimePermissions){
			isClickSupportRequestRuntimePermissions = false;
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}else {
			ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
			if (adapter != null) {
				LockDetailFragment fragment = (LockDetailFragment) adapter.getItem(0);
				if (fragment != null) {
					fragment.onRequestPermissionsResult(requestCode,  permissions, grantResults);
				}
			}
		}
	}

	public class ViewPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> mListFragment = new ArrayList<>();

		private ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private void addFragment(Fragment paramFragment) {
			if (mListFragment == null)
				mListFragment = new ArrayList();
			mListFragment.add(paramFragment);
		}

		public int getCount() {
			if (mListFragment == null)
				return 0;
			return mListFragment.size();
		}

		public Fragment getItem(int paramInt) {
			if (mListFragment == null)
				return null;
			return mListFragment.get(paramInt);
		}
	}

	@Override
	public void onEventSub(Event event) {
		super.onEventSub(event);
		if (Event.EventType.DELETE_LOCK_SUCCESS == event.type){
			finish();
		}
	}
}
