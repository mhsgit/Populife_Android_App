package com.populstay.populife.keypwdmanage.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.keypwdmanage.KeyPwdConstant;
import com.populstay.populife.keypwdmanage.entity.KeyPwd;
import com.populstay.populife.ui.recycler.HeaderAndFooterAdapter;
import com.populstay.populife.ui.recycler.ViewHolder;
import com.populstay.populife.ui.widget.extextview.ExTextView;
import com.populstay.populife.util.CollectionUtil;
import com.populstay.populife.util.date.DateUtil;
import com.populstay.populife.util.string.StringUtil;

import java.util.List;


public class KeyPwdListAdapter extends HeaderAndFooterAdapter<KeyPwd> {

	private Context mContext;
	private ListViewActionBtnClickListener mSendBtnClickListener, mRecordsBtnClickListener, mEditBtnClickListener, mDeleteBtnClickListener, mWarningBtnClickListener;
	private int mKeyType, mCategory;

	public KeyPwdListAdapter(Context context, int category, List<KeyPwd> list, int keyType) {

		super(list);
		mCategory = category;
		mContext = context;
		mKeyType = keyType;
	}

	@Override
	public ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(mContext).inflate(R.layout.key_pwd_list_item, parent, false);
		return new KeyPwdViewHolder(view);
	}

	@Override
	public void onBindItemViewHolder(ViewHolder holder, int position, final KeyPwd item) {

		final KeyPwdViewHolder videoViewHolder = (KeyPwdViewHolder) holder;
		videoViewHolder.tvName.setText(item.getSendUser());

		videoViewHolder.tvTime.setVisibility(View.GONE);
		videoViewHolder.llTime.setVisibility(View.GONE);
		videoViewHolder.llTimeRange.setVisibility(View.GONE);
		videoViewHolder.tvSingleTime.setVisibility(View.GONE);
		videoViewHolder.llActionBtns.setVisibility(View.GONE);

		videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
		videoViewHolder.ivShare.setVisibility(View.VISIBLE);
		videoViewHolder.ivEdit.setVisibility(View.VISIBLE);
		videoViewHolder.iv_delete.setVisibility(View.GONE);
		videoViewHolder.ivAdminTag.setVisibility(View.GONE);
		videoViewHolder.iv_warning.setVisibility(View.GONE);

		videoViewHolder.tvName.setText(StringUtil.isBlank(item.getAlias()) ? "-" : item.getAlias());

		// 蓝牙钥匙
		if (KeyPwdConstant.IType.TYPE_KEY == mKeyType) {
            Log.d("TESTTEST", "type: "+ item.getType() + "; name: "+ item.getAlias());
			// 限时
			if (1 == item.getType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_time_limited);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_period_icon);
				videoViewHolder.llTime.setVisibility(View.VISIBLE);
				videoViewHolder.llTimeRange.setVisibility(View.VISIBLE);
				videoViewHolder.tvSingleTime.setVisibility(View.GONE);
				videoViewHolder.tvStartTime.setText(DateUtil.getDateToStringConvert(item.getStartDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
				videoViewHolder.tvEndTime.setText(DateUtil.getDateToStringConvert(item.getEndDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
			}
			// 一次性(兼容老版本数据)
			else if (3 == item.getType()) {
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_one_time_icon);
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_one_time);
			}
			// 永久
			else {
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.bt_key_icon);
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_permanent);
			}

			// 接收人
			String recUser = item.getRecUser();
			videoViewHolder.tvPwd.setTextColor(mContext.getResources().getColor(R.color.text_gray_light));
			videoViewHolder.tvPwd.setText(String.format(mContext.getResources().getString(R.string.received_by), TextUtils.isEmpty(recUser) ? "-" : recUser));
			videoViewHolder.tvPwd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

			// 授权用户标识
			if (item.getKeyRight() == 1) {
				videoViewHolder.tvName.setRightRawIcon(R.drawable.ic_admin_auth, 10);
			} else {
				videoViewHolder.tvName.setNoneIcon();
			}


			videoViewHolder.ivRecords.setVisibility(View.GONE);
			// String钥匙的状态（110401：正常使用，110402：待接收，110405：已冻结，110408：已删除，110410：已重置,110500:已过期,"110501":未生效）;
			// 待接收才显示分享按钮
			if ("110402".equals(item.getStatus()) || "110501".equals(item.getStatus()) || "110400".equals(item.getStatus())) {
				if (!TextUtils.isEmpty(item.getShareKeyUrl())) {
					videoViewHolder.tvPwd.setText(String.format(mContext.getResources().getString(R.string.received_by), "-"));
					videoViewHolder.ivShare.setVisibility(View.VISIBLE);
				} else {
					videoViewHolder.ivShare.setVisibility(View.GONE);
					videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
				}
				videoViewHolder.iv_delete.setVisibility(View.VISIBLE);
			} else if ("110401".equals(item.getStatus())) {
				videoViewHolder.ivShare.setVisibility(View.GONE);
				videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
				videoViewHolder.iv_delete.setVisibility(View.GONE);
			} else {
				videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
				videoViewHolder.ivShare.setVisibility(View.GONE);
				videoViewHolder.iv_delete.setVisibility(View.VISIBLE);
			}
		}
		// 数字密码
		else if (KeyPwdConstant.IType.TYPE_PWD == mKeyType) {
            Log.d("TESTTEST", "type: "+ item.getKeyboardPwdType() + "; name: "+ item.getAlias());
			//2失效不显示分享按钮
			if ("2".equals(item.getStatus())) {
				videoViewHolder.ivShare.setVisibility(View.GONE);
				videoViewHolder.iv_delete.setVisibility(View.GONE);
			} else {
				videoViewHolder.ivShare.setVisibility(View.VISIBLE);
			}
			videoViewHolder.tvPwd.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
			videoViewHolder.tvPwd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			videoViewHolder.tvPwd.setText(item.getKeyboardPwd());
			// 自定义密码       15
			//One-time			1
			//Permanent		    2
			//Period			3
			//Admin			   -1
			//清空密码			4
			//循环密码			5--14
			if (15 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_custom);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_custom_icon);
				videoViewHolder.llTime.setVisibility(View.VISIBLE);
				videoViewHolder.llTimeRange.setVisibility(View.VISIBLE);
				videoViewHolder.tvStartTime.setText(DateUtil.getDateToString(item.getStartDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
				videoViewHolder.tvEndTime.setText(DateUtil.getDateToString(item.getEndDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
			} else if (1 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_one_time);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_one_time_icon);
				videoViewHolder.tvTime.setVisibility(View.GONE);
				videoViewHolder.tvTime.setText(DateUtil.getDateToString(item.getCreateDate(), DateUtil.DATE_TIME_PATTERN_1));
			} else if (2 == item.getKeyboardPwdType() || 16 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_permanent);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_permanent_icon);
				videoViewHolder.tvTime.setVisibility(View.GONE);
				videoViewHolder.tvTime.setText(DateUtil.getDateToString(item.getCreateDate(), DateUtil.DATE_TIME_PATTERN_1));

			} else if (3 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_time_limited);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_period_icon);
				videoViewHolder.llTime.setVisibility(View.VISIBLE);
				videoViewHolder.llTimeRange.setVisibility(View.VISIBLE);
				videoViewHolder.tvStartTime.setText(DateUtil.getDateToString(item.getStartDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
				videoViewHolder.tvEndTime.setText(DateUtil.getDateToString(item.getEndDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
			} else if (-1 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.administrator_code);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_admin_icon);
				if (TextUtils.isEmpty(item.getAlias())) {
					videoViewHolder.tvName.setText(R.string.administrator_code);
				} else {
					videoViewHolder.tvName.setText(item.getAlias());
				}
				videoViewHolder.ivRecords.setVisibility(View.GONE);
				videoViewHolder.ivShare.setVisibility(View.GONE);
				videoViewHolder.ivAdminTag.setVisibility(View.VISIBLE);
			} else if (4 == item.getKeyboardPwdType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_clear);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_clear_icon);
			}
			// (兼容老版本数据)
			else if (isCyclicPwd(item.getKeyboardPwdType())) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_cyclic);
				videoViewHolder.ivTypeIcon.setImageResource(R.drawable.pwd_cyclic_icon);
				videoViewHolder.llTime.setVisibility(View.VISIBLE);
				videoViewHolder.tvSingleTime.setVisibility(View.VISIBLE);
				videoViewHolder.tvSingleTime.setText(getCyclicTime(item));
			} else {
				// 未知
				videoViewHolder.tvTypeName.setText("");
			}
			showWarningIcon(videoViewHolder.iv_warning, item);
			videoViewHolder.ivRecords.setImageResource(R.drawable.history);
		}
		// 门卡/指纹
		else if (KeyPwdConstant.IType.TYPE_IC_CARD == mKeyType || KeyPwdConstant.IType.TYPE_FINGERPRINT == mKeyType) {
			videoViewHolder.ivShare.setVisibility(View.GONE);
			switch (mCategory) {
				case KeyPwdConstant.IKeyPwdCategory.KEY_PWD_CATEGORY_AVAILABLE:
					videoViewHolder.ivEdit.setVisibility(View.VISIBLE);
					videoViewHolder.iv_delete.setVisibility(View.GONE);
					videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
					break;

				case KeyPwdConstant.IKeyPwdCategory.KEY_PWD_CATEGORY_NOT_ACTIVATED:
					videoViewHolder.ivEdit.setVisibility(View.VISIBLE);
					videoViewHolder.iv_delete.setVisibility(View.VISIBLE);
					videoViewHolder.ivRecords.setVisibility(View.GONE);
					break;

				case KeyPwdConstant.IKeyPwdCategory.KEY_PWD_CATEGORY_INVALID:
					videoViewHolder.ivEdit.setVisibility(View.VISIBLE);
					videoViewHolder.iv_delete.setVisibility(View.VISIBLE);
					videoViewHolder.ivRecords.setVisibility(View.VISIBLE);
					break;

				default:
					break;
			}

			videoViewHolder.tvPwd.setTextColor(mContext.getResources().getColor(R.color.text_gray_light));
			videoViewHolder.tvPwd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			if (KeyPwdConstant.IType.TYPE_FINGERPRINT == mKeyType) {
				videoViewHolder.tvPwd.setVisibility(StringUtil.isBlank(item.getRemark()) ? View.GONE : View.VISIBLE);
				videoViewHolder.tvPwd.setText(item.getRemark());
			} else if (KeyPwdConstant.IType.TYPE_IC_CARD == mKeyType) {
				videoViewHolder.tvPwd.setVisibility(View.VISIBLE);
				videoViewHolder.tvPwd.setText(String.format(mContext.getResources().getString(R.string.ic_card_number), item.getCardNumber()));
			}

			// Permanent    2
			// Period   1
			videoViewHolder.ivTypeIcon.setImageResource(KeyPwdConstant.IType.TYPE_IC_CARD == mKeyType ? R.drawable.ic_card_icon : R.drawable.fingerprint_icon);
			if (2 == item.getType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_permanent);
				videoViewHolder.tvTime.setVisibility(View.GONE);
			} else if (1 == item.getType()) {
				videoViewHolder.tvTypeName.setText(R.string.pwd_type_name_time_limited);
				videoViewHolder.llTime.setVisibility(View.VISIBLE);
				videoViewHolder.llTimeRange.setVisibility(View.VISIBLE);
				videoViewHolder.tvStartTime.setText(DateUtil.getDateToString(item.getStartDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
				videoViewHolder.tvEndTime.setText(DateUtil.getDateToString(item.getEndDate(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM));
			} else {
				// 未知
				videoViewHolder.tvTypeName.setText("");
			}
			showWarningIcon(videoViewHolder.iv_warning, item);
			videoViewHolder.ivRecords.setImageResource(R.drawable.history);
		}

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (View.VISIBLE != videoViewHolder.llActionBtns.getVisibility()) {
					videoViewHolder.llActionBtns.setVisibility(View.VISIBLE);
				} else {
					videoViewHolder.llActionBtns.setVisibility(View.GONE);
				}
			}
		});

		videoViewHolder.ivRecords.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mRecordsBtnClickListener) {
					mRecordsBtnClickListener.onClick(v, item);
				}
			}
		});

		videoViewHolder.ivShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mSendBtnClickListener) {
					mSendBtnClickListener.onClick(v, item);
				}
			}
		});

		videoViewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mEditBtnClickListener) {
					mEditBtnClickListener.onClick(v, item);
				}
			}
		});
		videoViewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mDeleteBtnClickListener) {
					mDeleteBtnClickListener.onClick(v, item);
				}
			}
		});
		videoViewHolder.iv_warning.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mWarningBtnClickListener) {
					mWarningBtnClickListener.onClick(v, item);
				}
			}
		});


	}

	private String getCyclicTime(KeyPwd item) {
		String cyclicMode = "";
		String cyclicTime = " " + DateUtil.getDateToString(item.getStartDate(), "HH:00") + "-"
				+ DateUtil.getDateToString(item.getEndDate(), "HH:00");
		Resources res = mContext.getResources();
		switch (item.getKeyboardPwdType()) {
			case 5:
				cyclicMode = res.getString(R.string.weekend);
				break;

			case 6:
				cyclicMode = res.getString(R.string.daily);
				break;

			case 7:
				cyclicMode = res.getString(R.string.workday);
				break;

			case 8:
				cyclicMode = res.getString(R.string.monday);
				break;

			case 9:
				cyclicMode = res.getString(R.string.tuesday);
				break;

			case 10:
				cyclicMode = res.getString(R.string.wednesday);
				break;

			case 11:
				cyclicMode = res.getString(R.string.thursday);
				break;

			case 12:
				cyclicMode = res.getString(R.string.friday);
				break;

			case 13:
				cyclicMode = res.getString(R.string.saturday);
				break;

			case 14:
				cyclicMode = res.getString(R.string.sunday);
				break;
		}
		return cyclicMode + cyclicTime;
	}

	private boolean isCyclicPwd(int keyboardPwdType) {
		switch (keyboardPwdType) {
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
				return true;
		}
		return false;
	}

	/**
	 * 再梳理一下目前期望的方案是这样：
	 * <p>
	 * 创建密码后i不会一直出现，直到：
	 * 1、用户在激活期内同步了操作记录，且密码使用过的，i就消失
	 * （一次性密码激活期只有6小时，6小时后无论是否使用过都会被移动到invalid，i一直不会出现，其他类型的密码只要在有效期内，继续在valid列表）
	 * 2、用户在激活期后不同步操作记录，继续保留i
	 * 3、用户在激活期后同步操作记录，未被使用过会被移到invalid，使用过i就可以消失，至于在哪个列表，就看是否还在有效期。
	 * <p>
	 * - - - - - - - - - - - -- - - - - - - - - - - -
	 * i只在激活期后，未知状态的时候
	 * <p>
	 * 激活期即
	 * 限时密码在开始时间后24小时
	 * 永久密码在创建时间后24小时
	 * <p>
	 * 未知状态即
	 * 用户此前没有任何操作记录，且一直未同步过操作记录。
	 * - - - - - - - - - - - -- - - - - - - - - - - -
	 *
	 * @param iv_warning
	 * @param keyPwd
	 */
	private void showWarningIcon(ImageView iv_warning, KeyPwd keyPwd) {
		// 自定义密码       15
		//One-time			1
		//Permanent		    2
		//Period			3
		//Admin			   -1
		//清空密码			4
		//循环密码			5--14
		int keyboardPwdType = keyPwd.getKeyboardPwdType();
		if (15 == keyboardPwdType
				|| -1 == keyboardPwdType
				|| 4 == keyboardPwdType) {
			return;
		}

		// status：密码状态(0删除，1未激活，2失效， 3正常，4未知)
		if ("4".equals(keyPwd.getStatus())) {
			iv_warning.setVisibility(View.VISIBLE);
		} else if ("3".equals(keyPwd.getStatus())) {
			// useStaus：激活期内的使用状态，1：未同步过，2：使用过，3：未使用
			if (keyPwd.getUseStaus() == 1 || keyPwd.getUseStaus() == 3) {
				iv_warning.setVisibility(View.VISIBLE);
			}
		}
	}

	// 是否在激活期内
	private boolean isActivationPeriod(KeyPwd keyPwd) {
		long currentTime = System.currentTimeMillis();
		long diffTime = 0;
		boolean isActivationPeriod = false;
		switch (keyPwd.getKeyboardPwdType()) {
			//One-time			1
			case 1:
				diffTime = currentTime - keyPwd.getCreateDate();
				isActivationPeriod = (diffTime / 1000 / 60 / 60) <= 6;
				break;
			//Permanent		    2
			case 2:
				diffTime = currentTime - keyPwd.getCreateDate();
				isActivationPeriod = (diffTime / 1000 / 60 / 60) <= 24;
				break;
			//Period			3
			case 3:
				diffTime = currentTime - keyPwd.getStartDate();
				isActivationPeriod = (diffTime / 1000 / 60 / 60) <= 24;
				break;
		}
		return isActivationPeriod;
	}

	public void setmSendBtnClickListener(ListViewActionBtnClickListener mSendBtnClickListener) {
		this.mSendBtnClickListener = mSendBtnClickListener;
	}

	public void setmRecordsBtnClickListener(ListViewActionBtnClickListener mRecordsBtnClickListener) {
		this.mRecordsBtnClickListener = mRecordsBtnClickListener;
	}

	public void setmEditBtnClickListener(ListViewActionBtnClickListener mEditBtnClickListener) {
		this.mEditBtnClickListener = mEditBtnClickListener;
	}

	public void setmDeleteBtnClickListener(ListViewActionBtnClickListener mDeleteBtnClickListener) {
		this.mDeleteBtnClickListener = mDeleteBtnClickListener;
	}

	public void setmWarningBtnClickListener(ListViewActionBtnClickListener mWarningBtnClickListener) {
		this.mWarningBtnClickListener = mWarningBtnClickListener;
	}

	public List<KeyPwd> getDatas() {
		return mList;
	}

	public int getDataCount() {
		return CollectionUtil.isEmpty(mList) ? 0 : mList.size();
	}

	public interface ListViewActionBtnClickListener {
		void onClick(View view, KeyPwd item);
	}

	class KeyPwdViewHolder extends ViewHolder {
		ImageView ivTypeIcon;
		TextView tvTypeName;
		ExTextView tvName;
		TextView tvTime;
		LinearLayout llTime, llTimeRange;
		TextView tvStartTime, tvEndTime, tvSingleTime;
		TextView tvPwd;
		LinearLayout llActionBtns;
		ImageView ivRecords, ivShare, ivEdit, ivAdminTag, iv_delete, iv_warning;

		public KeyPwdViewHolder(View itemView) {
			super(itemView);
			ivTypeIcon = itemView.findViewById(R.id.iv_type_icon);
			tvName = itemView.findViewById(R.id.tv_name);
			tvTypeName = itemView.findViewById(R.id.tv_type_name);
			tvTime = itemView.findViewById(R.id.tv_time);
			llTime = itemView.findViewById(R.id.ll_time);
			llTimeRange = itemView.findViewById(R.id.ll_time_range);
			tvStartTime = itemView.findViewById(R.id.tv_start_time);
			tvEndTime = itemView.findViewById(R.id.tv_end_time);
			tvSingleTime = itemView.findViewById(R.id.tv_single_time);
			tvPwd = itemView.findViewById(R.id.tv_pwd);
			llActionBtns = itemView.findViewById(R.id.ll_action_btns);
			ivRecords = itemView.findViewById(R.id.iv_history);
			iv_delete = itemView.findViewById(R.id.iv_delete);
			ivShare = itemView.findViewById(R.id.iv_share);
			ivEdit = itemView.findViewById(R.id.iv_edit);
			ivAdminTag = itemView.findViewById(R.id.iv_admin_tag);
			iv_warning = itemView.findViewById(R.id.iv_warning);
		}
	}
}
