package com.beastweather2.android;


import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.beastweather2.android.db.Settings;

import java.util.List;

/**
 * Created by beast on 2017/12/19.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> implements View.OnClickListener {
    private List<Settings> mSettingsList;
    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View settingView;
        TextView settingName;

        public ViewHolder(View view) {
            super(view);
            settingView = view;
            settingName = (TextView) view.findViewById(R.id.setting_name);
        }
    }

    public SettingsAdapter(List<Settings> settingsList) {
        mSettingsList = settingsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Settings settings = mSettingsList.get(position);
        holder.settingName.setText(settings.getName());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mSettingsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
}
