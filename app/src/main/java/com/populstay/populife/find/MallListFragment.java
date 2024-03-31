package com.populstay.populife.find;

import android.view.View;

import com.populstay.populife.R;
import com.populstay.populife.find.adapter.MallListAdapter;
import com.populstay.populife.find.entity.Product;
import com.populstay.populife.find.entity.ProductInfo;
import com.populstay.populife.util.locale.LanguageUtil;
import com.populstay.populife.util.pkg.ThirdAppUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MallListFragment extends FindFragment {

	private RecyclerView mDeviceListView;
	private MallListAdapter mDeviceListAdapter;
	private List<Product> mDeviceList;

	public static FindFragment newInstance() {
		FindFragment fragment = new MallListFragment();
		return fragment;
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_mall_list;
	}

	@Override
	protected void init(View view) {
		mDeviceListView = view.findViewById(R.id.home_device_list_recyclerview);
		mDeviceListView.setLayoutManager(new LinearLayoutManager(getContext()));
		mDeviceList = new ArrayList<>();
		initData();
		mDeviceListAdapter = new MallListAdapter(mDeviceList, getContext());
		mDeviceListView.setAdapter(mDeviceListAdapter);
		mDeviceListAdapter.setOnItemClickListener(new MallListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				mDeviceListAdapter.selectItem(position);
				String productUrl = mDeviceList.get(position).getDetailUrl();

				if (LanguageUtil.isChinese(getActivity()) && ThirdAppUtil.isAppInstalled(getActivity(), ThirdAppUtil.TAO_BAO)) {
					// App 为中文语言，同时手机已安装淘宝 App，则用淘宝 App 打开商品详情页
					ThirdAppUtil.startTaobaoAppGoodsDetail(getActivity(), productUrl);
				} else {
					// 否则用手机浏览器打开
					ThirdAppUtil.startPhoneBrowser(getActivity(), productUrl);
				}
			}
		});
	}

	private void initData() {

		// 密码盒
		Product device = new Product(getString(R.string.lock_type_keybox), ProductInfo.IProductInfoType.PRODUCT_LOCK_TYPE_KEY_BOX);
		device.setDetailUrl(LanguageUtil.isChinese(getActivity()) ? "https://item.taobao.com/item.htm?id=642013674650" : "https://www.populife.co/products/smart-keybox");
		mDeviceList.add(device);

		// 横闩锁
		device = new Product(getString(R.string.lock_type_deadbolt), ProductInfo.IProductInfoType.PRODUCT_LOCK_TYPE_DEADBOLT);
		device.setDetailUrl(LanguageUtil.isChinese(getActivity()) ? "https://item.taobao.com/item.htm?id=634528797082" : "https://www.populife.co/products/smart-deadbolt");
		mDeviceList.add(device);

		// 网关
		device = new Product(getString(R.string.device_name_gateway), ProductInfo.IProductInfoType.PRODUCT_TYPE_GATEWAY);
		device.setDetailUrl(LanguageUtil.isChinese(getActivity()) ? "https://item.taobao.com/item.htm?id=626624880701" : "https://www.populife.co/products/gateway");
		mDeviceList.add(device);

		// PopuCare
       /* device = new Product(getString(R.string.product_type_popu_care_name),ProductInfo.IProductInfoType.PRODUCT_TYPE_POPU_CARE);
        device.setDetailUrl("https://www.populife.co");
        mDeviceList.add(device);*/

	}
}
