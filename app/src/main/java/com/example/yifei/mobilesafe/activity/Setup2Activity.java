package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {


    private SettingItemView sivSim;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip2);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        sivSim = (SettingItemView) findViewById(R.id.siv_simCard);
        String sim = mPref.getString("simSerialNumber",null);
        if (TextUtils.isEmpty(sim)){
            sivSim.setChecked(false);
        }else {
            sivSim.setChecked(true);
        }

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sivSim.isChecked()){
                    sivSim.setChecked(true);
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();
                    System.out.println(simSerialNumber);
                    mPref.edit().putString("simSerialNumber",simSerialNumber).apply();
                }else{
                    sivSim.setChecked(false);
                    mPref.edit().remove("simSerialNumber").apply();
                }
            }
        });

    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup2Activity.this, Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }

    @Override
    public void showNext() {
        String sim = mPref.getString("simSerialNumber",null);
        if (!TextUtils.isEmpty(sim)){
            startActivity(new Intent(Setup2Activity.this, Setup3Activity.class));
            finish();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        }else {
            Toast.makeText(this,"请点击绑定sim卡.再进入下一步",Toast.LENGTH_SHORT).show();
        }

    }


}
