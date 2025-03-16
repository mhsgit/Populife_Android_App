package com.populstay.populife.ui.widget.language;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.populstay.populife.R;
import com.populstay.populife.ui.widget.SwitchLanguagePopupWindow;
import com.populstay.populife.util.locale.LocalManageUtils;
import com.populstay.populife.util.locale.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class SwitchLanguageBottomSheet extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private List<LanguageItem> languageList = new ArrayList<>();
    private LanguageAdapter adapter;
    private SwitchLanguagePopupWindow.SelectLanguageListener mSelectLanguageListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.AppBottomSheet);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_language, container, false);
        recyclerView = view.findViewById(R.id.rv_language);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 获取底部弹窗的对话框
        Dialog dialog = getDialog();
        if (dialog != null) {
            // 设置状态栏颜色
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialog.getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.transparent)); // 白色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dialog.getWindow().setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.transparent)); // 白色
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRecyclerView();
    }


    private void initData() {
        languageList.add(new LanguageItem(1, getResources().getString(R.string.language_english), R.mipmap.icon_language_english, getSelectedLanguageId() == 1));
        languageList.add(new LanguageItem(2, getResources().getString(R.string.language_simplified_chinese), R.mipmap.icon_language_simplified_chinese, getSelectedLanguageId() == 2));
        languageList.add(new LanguageItem(3, getResources().getString(R.string.language_japanese), R.mipmap.icon_language_japanese, getSelectedLanguageId() == 3));
        languageList.add(new LanguageItem(4, getResources().getString(R.string.language_german), R.mipmap.icon_language_german, getSelectedLanguageId() == 4));
        languageList.add(new LanguageItem(5, getResources().getString(R.string.language_french), R.mipmap.icon_language_french, getSelectedLanguageId() == 5));
        languageList.add(new LanguageItem(6, getResources().getString(R.string.language_italian), R.mipmap.icon_language_italian, getSelectedLanguageId() == 6));
        languageList.add(new LanguageItem(7, getResources().getString(R.string.language_spanish), R.mipmap.icon_language_spanish, getSelectedLanguageId() == 7));
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LanguageAdapter(languageList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(languageItem -> {
            // 回调给 Activity 处理语言切换逻辑
            if (null != mSelectLanguageListener){
                mSelectLanguageListener.onSelectLanguage(languageItem.getId());
            }
            //saveSelectedLanguageId(languageItem.getId());
            dismiss();
        });
    }

    private void saveSelectedLanguageId(int languageId) {
        SPUtils.getInstance(requireContext()).setLanguage(languageId);
    }

    private int getSelectedLanguageId() {
        return SPUtils.getInstance(requireContext()).getLanguage(LocalManageUtils.getCurrentLanCodeByLocal(requireContext())); // 默认选第一个
    }

    public static void showDialog(androidx.fragment.app.FragmentActivity activity, SwitchLanguagePopupWindow.SelectLanguageListener selectLanguageListener) {
        SwitchLanguageBottomSheet dialog = new SwitchLanguageBottomSheet();
        dialog.setSelectLanguageListener(selectLanguageListener);
        dialog.show(activity.getSupportFragmentManager(), "language_dialog");
    }

    public void setSelectLanguageListener(SwitchLanguagePopupWindow.SelectLanguageListener selectLanguageListener) {
        this.mSelectLanguageListener = selectLanguageListener;
    }
}
