package com.beastweather2.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.beastweather2.android.db.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beast on 2017/12/18.
 */

public class SettingsActivity extends Fragment {
    private RecyclerView recyclerView;
    private List<Settings> settingsList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_settings,container,false);
        initSettingsList();
        recyclerView = (RecyclerView) view.findViewById(R.id.settings_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        SettingsAdapter adapter = new SettingsAdapter(settingsList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(new SettingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0){
                    Toast.makeText(view.getContext(),"Bye",Toast.LENGTH_SHORT).show();
                    getActivity().finishAffinity();
                }
                else if (position == 1){
                    Toast.makeText(view.getContext(),"Location",Toast.LENGTH_SHORT).show();
                }
            }
        });
return view;
    }
    private void initSettingsList(){
        Settings exit = new Settings("Exit");
        settingsList.add(exit);
        Settings location = new Settings("Location");
        settingsList.add(location);
    }


}
