package com.populstay.populife.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.populstay.populife.R;
import com.populstay.populife.entity.LockOperateRecord;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Jerry
 */

public class LockOperateRecordAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<String> mGroupNames;
	private Map<String, List<LockOperateRecord>> mRecords;

	/**
	 * 构造函数
	 *
	 * @param context    上下文
	 * @param groupNames 组元素列表
	 * @param records    子元素列表
	 */
	public LockOperateRecordAdapter(Context context, List<String> groupNames,
									Map<String, List<LockOperateRecord>> records) {
		this.mContext = context;
		this.mGroupNames = groupNames;
		this.mRecords = records;
	}

	@Override
	public int getGroupCount() {
		return mGroupNames.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String groupName = mGroupNames.get(groupPosition);
		return mRecords.get(groupName).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroupNames.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<LockOperateRecord> itemNames = mRecords.get(mGroupNames.get(groupPosition));
		return itemNames.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		final GroupViewHolder gholder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_lock_operate_record_group, null);
			gholder = new GroupViewHolder();
			gholder.groupName = convertView.findViewById(R.id.tv_lock_record_group_name);
			convertView.setTag(gholder);
		} else {
			gholder = (GroupViewHolder) convertView.getTag();
		}
		String groupName = (String) getGroup(groupPosition);
		gholder.groupName.setText(groupName);

		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 final boolean isLastChild, View convertView, final ViewGroup parent) {

		final ChildViewHolder cholder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_lock_operate_record_child, null);
			cholder = new ChildViewHolder();
			cholder.avatar = convertView.findViewById(R.id.civ_lock_record_child_avatar);
			cholder.name = convertView.findViewById(R.id.tv_lock_record_child_name);
			cholder.date = convertView.findViewById(R.id.tv_lock_record_child_date);
			cholder.content = convertView.findViewById(R.id.tv_lock_record_child_content);
			convertView.setTag(cholder);
		} else {
			cholder = (ChildViewHolder) convertView.getTag();
		}
		LockOperateRecord record = (LockOperateRecord) getChild(groupPosition, childPosition);
		String alias = record.getAlias();
		String password = record.getPassword();
		if (record.getNickname().equalsIgnoreCase("--")) {
			if (alias!=null) {
				cholder.name.setText(alias);
			}else {
				cholder.name.setText(password);
			}
		}else{
			cholder.name.setText(record.getNickname());
		}
		cholder.date.setText(record.getCreateDate());
		String event = record.getEvent();
		if ("1".equals(event)) {
			cholder.content.setText(R.string.unlock_with_app);
			setUserAvatar(record.getAvatar(), cholder.avatar);
		} else if ("2".equals(event)) {
			cholder.content.setText(R.string.lock_with_app);
			setUserAvatar(record.getAvatar(), cholder.avatar);
		} else if ("3".equals(event)) {
			cholder.content.setText(R.string.unlock_with_keypad);
			cholder.avatar.setImageResource(R.drawable.log_icon_pwd);
		} else if ("4".equals(event)) {
			cholder.content.setText(R.string.unlock_with_gateway);
			setUserAvatar(record.getAvatar(), cholder.avatar);
		} else if ("5".equals(event)) {
			cholder.content.setText(R.string.lock_with_gateway);
			setUserAvatar(record.getAvatar(), cholder.avatar);
		} else if ("7".equals(event)) {
			cholder.content.setText(R.string.unlock_with_fingerprint);
			cholder.avatar.setImageResource(R.drawable.log_icon_fingerprint);
		} else if ("8".equals(event)) {
			cholder.content.setText(R.string.unlock_with_ic_card);
			cholder.avatar.setImageResource(R.drawable.log_icon_card);
		} else {
			cholder.content.setText(record.getContent());
		}
		return convertView;
	}

	private void setUserAvatar(String avatar, CircleImageView view) {
		Glide.with(mContext)
				.load(avatar)
				.asBitmap()
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.dontAnimate()
				.centerCrop()
				.placeholder(R.drawable.ic_user_avatar)
				.error(R.drawable.ic_user_avatar)
				.into(view);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/**
	 * 组元素绑定器
	 */
	static class GroupViewHolder {
		TextView groupName;
	}

	/**
	 * 子元素绑定器
	 */
	static class ChildViewHolder {
		CircleImageView avatar;
		TextView name, date, content;
	}
}

