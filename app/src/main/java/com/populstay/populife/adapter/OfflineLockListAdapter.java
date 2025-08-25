package com.populstay.populife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.entity.OfflineLock;

import java.util.List;

public class OfflineLockListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<OfflineLock> mLockList;

    public OfflineLockListAdapter(Context context, List<OfflineLock> lockList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLockList = lockList;
    }

    // 获取数量
    public int getCount() {
        return mLockList.size();
    }

    // 获取当前选项
    public Object getItem(int position) {
        return mLockList.get(position);
    }

    // 获取当前选项的 id
    public long getItemId(int position) {
        return position;
    }

    // 获取 View
    public View getView(final int position, View convertView, ViewGroup parent) {
        OfflineLockListAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_office_lock_list, null);
            holder = new OfflineLockListAdapter.ViewHolder();

            holder.name = convertView.findViewById(R.id.tv_item_gateway_binded_lock_name);
            holder.signal = convertView.findViewById(R.id.tv_item_gateway_binded_lock_signal);
            holder.ivDeviceIcon = convertView.findViewById(R.id.iv_device_icon);
            holder.syncBtn = convertView.findViewById(R.id.tv_lock_settings_sync);

            // 在 getView 方法中找到 holder.syncBtn 后添加以下代码
            holder.syncBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSyncButtonClickListener != null) {
                        OfflineLock lock = mLockList.get(position);
                        mOnSyncButtonClickListener.onSyncClick(position, lock);
                    }
                }
            });


            convertView.setTag(holder);
        } else {
            holder = (OfflineLockListAdapter.ViewHolder) convertView.getTag();
        }
        OfflineLock lock = mLockList.get(position);
        holder.name.setText(lock.getLockName());
        return convertView;
    }

    class ViewHolder {
        TextView name, signal,syncBtn;
        ImageView ivDeviceIcon;
    }

    private OnSyncButtonClickListener mOnSyncButtonClickListener;

    public void setOnSyncButtonClickListener(OnSyncButtonClickListener listener) {
        this.mOnSyncButtonClickListener = listener;
    }


    public interface OnSyncButtonClickListener {
        void onSyncClick(int position, OfflineLock lock);
    }

}
