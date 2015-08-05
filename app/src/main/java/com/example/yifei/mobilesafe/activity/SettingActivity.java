package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.view.SettingItemView;



/*
*设置中心
* */

public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        boolean autoUpdate = mPref.getBoolean("auto_update",true);
        if (autoUpdate){
            sivUpdate.setChecked(true);
        }else {
            sivUpdate.setChecked(false);
        }
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()){
                    sivUpdate.setChecked(false);
                    mPref.edit().putBoolean("auto_update",false).apply();
                }else {
                    sivUpdate.setChecked(true);
                    mPref.edit().putBoolean("auto_update",true).apply();
                }
            }
        });

    }


}
