package com.populstay.populife.find;

import android.view.View;

import com.populstay.populife.R;
import com.populstay.populife.activity.PDFActivity;
import com.populstay.populife.find.adapter.UserManualListAdapter;
import com.populstay.populife.find.entity.UserManual;
import com.populstay.populife.find.entity.UserManualInfo;
import com.populstay.populife.util.locale.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserManualFragment extends FindFragment {
	private RecyclerView mDeviceListView;
	private UserManualListAdapter mDeviceListAdapter;
	private List<UserManual> mDeviceList;

	public static FindFragment newInstance() {
		FindFragment fragment = new UserManualFragment();
		return fragment;
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_user_manual;
	}

	@Override
	protected void init(View view) {

		mDeviceListView = view.findViewById(R.id.home_device_list_recyclerview);
		mDeviceListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mDeviceList = new ArrayList<>();
		initData();
		mDeviceListAdapter = new UserManualListAdapter(mDeviceList, getContext());
		mDeviceListView.setAdapter(mDeviceListAdapter);
		mDeviceListAdapter.setOnItemClickListener(new UserManualListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				mDeviceListAdapter.selectItem(position);
				UserManual homeDevice = mDeviceList.get(position);
				if (UserManualInfo.IUserManualType.USER_MANUAL_TYPE_APP.equals(homeDevice.getType())) {
					PDFActivity.actionStart(getActivity(), getString(R.string.user_manual_app),
							"user_manual_app.pdf", true);
				} else if (UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_DEADBOLT.equals(homeDevice.getType())) {
					PDFActivity.actionStart(getActivity(), getString(R.string.user_manual_deadbolt),
							"user_manual_deadbolt.pdf", true);
				} else if (UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_KEY_BOX.equals(homeDevice.getType())) {

					String pdfAssetName = "user_manual_keybox_en.pdf";
					if (LanguageUtil.isChinese(getActivity())) {
						pdfAssetName = "user_manual_keybox_cn.pdf";
					} else if (LanguageUtil.isJp(getActivity())) {
						pdfAssetName = "user_manual_keybox_jp.pdf";
					} else if (LanguageUtil.isDe(getActivity())) {
						pdfAssetName = "user_manual_keybox_de.pdf";
					} else if (LanguageUtil.isEs(getActivity())) {
						pdfAssetName = "user_manual_keybox_es.pdf";
					} else if (LanguageUtil.isFr(getActivity())) {
						pdfAssetName = "user_manual_keybox_fr.pdf";
					} else if (LanguageUtil.isIt(getActivity())) {
						pdfAssetName = "user_manual_keybox_it.pdf";
					}

					PDFActivity.actionStart(getActivity(), getString(R.string.user_manual_keybox), pdfAssetName, true);
				} else if (UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_KEY_BOX_K4.equals(homeDevice.getType())) {

					String pdfAssetName = "user_manual_keybox_k4_en.pdf.pdf";
					if (LanguageUtil.isChinese(getActivity())) {
						pdfAssetName = "user_manual_keybox_k4_cn.pdf";
					}

					PDFActivity.actionStart(getActivity(), getString(R.string.user_manual_keybox), pdfAssetName, true);
				} else if (UserManualInfo.IUserManualType.USER_MANUAL_TYPE_GATEWAY.equals(homeDevice.getType())) {
					String pdfGatewayAssetName = "user_manual_gateway_en.pdf";
					if (LanguageUtil.isChinese(getActivity())) {
						pdfGatewayAssetName = "user_manual_gateway_cn.pdf";
					}
					PDFActivity.actionStart(getActivity(), getString(R.string.user_manual_gateway),
							pdfGatewayAssetName, true);
				}
			}
		});
	}

	private void initData() {
		UserManual device = null;
		// App手册(先屏蔽)
        /*device = new UserManual(getString(R.string.user_manual_app), UserManualInfo.IUserManualType.USER_MANUAL_TYPE_APP);
        mDeviceList.add(device);*/

		// 横闩锁
		device = new UserManual(getString(R.string.user_manual_deadbolt), UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_DEADBOLT);
		mDeviceList.add(device);

		// 密码盒
		device = new UserManual(getString(R.string.user_manual_keybox), UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_KEY_BOX);
		mDeviceList.add(device);

		// 密码盒Max
		device = new UserManual(getString(R.string.user_manual_keybox_k4), UserManualInfo.IUserManualType.USER_MANUAL_LOCK_TYPE_KEY_BOX_K4);
		mDeviceList.add(device);

		// 网关
		device = new UserManual(getString(R.string.user_manual_gateway), UserManualInfo.IUserManualType.USER_MANUAL_TYPE_GATEWAY);
		mDeviceList.add(device);

	}
}
