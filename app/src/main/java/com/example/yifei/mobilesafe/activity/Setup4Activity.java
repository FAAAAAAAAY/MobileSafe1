package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.yifei.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences mPref;
    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        boolean protect = mPref.getBoolean("protect",false);
        if (protect){
            cbProtect.setChecked(true);
            cbProtect.setText("防盗保护已经开启");
        }else {
            cbProtect.setChecked(false);
            cbProtect.setText("防盗保护没有开启");
        }
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect",true).apply();
                }else{
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect",false).apply();
                }
            }
        });
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup4Activity.this, Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup4Activity.this, LostFindActivity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        mPref.edit().putBoolean("configed",true).apply();
    }


}
