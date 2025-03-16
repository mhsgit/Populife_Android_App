package com.populstay.populife.ui.widget.language;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.populstay.populife.R;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private List<LanguageItem> languageList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(LanguageItem languageItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public LanguageAdapter(List<LanguageItem> languageList) {
        this.languageList = languageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageItem item = languageList.get(position);
        holder.tvLanguage.setText(item.getLanguageName());
        holder.ivIcon.setImageResource(item.getLanguageIcon());
        holder.ivSelected.setVisibility(item.isSelected() ? View.VISIBLE : View.GONE);
        holder.tvLanguage.setTextColor(holder.tvLanguage.getResources().getColor(item.isSelected() ? R.color.lan_color_selected : R.color.lan_color_unselected));

        holder.itemView.setOnClickListener(v -> {
            for (LanguageItem languageItem : languageList) {
                languageItem.setSelected(false);
            }
            item.setSelected(true);
            notifyDataSetChanged();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvLanguage;
        ImageView ivSelected;

        public ViewHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.iv_language_icon);
            tvLanguage = view.findViewById(R.id.tv_language_name);
            ivSelected = view.findViewById(R.id.iv_selected);
        }
    }
}
