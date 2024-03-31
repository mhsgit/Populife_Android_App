package com.populstay.populife.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.entity.LockOperateRecord;
import com.populstay.populife.keypwdmanage.KeyPwdConstant;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Jerry
 */

public class PasscodeRecordAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<String> mGroupNames;
	private Map<String, List<LockOperateRecord>> mRecords;
	private int mType;

	/**
	 * 构造函数
	 *
	 * @param context    上下文
	 * @param groupNames 组元素列表
	 * @param records    子元素列表
	 */
	public PasscodeRecordAdapter(Context context, int type, List<String> groupNames,
								 Map<String, List<LockOperateRecord>> records) {
		this.mContext = context;
		this.mType = type;
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

	@SuppressLint("SetTextI18n")
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

		if (KeyPwdConstant.IType.TYPE_PWD == mType) {
			cholder.name.setText(record.getCreateDate() + " " + mContext.getResources().getString(R.string.unlock_with_keypad));
			cholder.content.setVisibility(View.GONE);
			cholder.date.setVisibility(View.GONE);
			cholder.avatar.setImageResource(R.drawable.log_icon_pwd);
		} else if (KeyPwdConstant.IType.TYPE_IC_CARD == mType) {
			cholder.name.setText(record.getNickname());
			cholder.date.setText(record.getCreateDate());
			cholder.content.setText(R.string.unlock_with_ic_card);
			cholder.avatar.setImageResource(R.drawable.log_icon_card);
		} else if (KeyPwdConstant.IType.TYPE_FINGERPRINT == mType) {
			cholder.name.setText(record.getNickname());
			cholder.date.setText(record.getCreateDate());
			cholder.content.setText(R.string.unlock_with_fingerprint);
			cholder.avatar.setImageResource(R.drawable.log_icon_fingerprint);
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
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
		TextView name, date, content;
		CircleImageView avatar;
	}
}

