package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.service.AddressService;
import com.example.yifei.mobilesafe.service.CallSafeService;
import com.example.yifei.mobilesafe.utils.ServiceStatusUtils;
import com.example.yifei.mobilesafe.view.SettingClickView;
import com.example.yifei.mobilesafe.view.SettingItemView;



/*
*设置中心
* */

public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SettingClickView scvAddressStyle;
    private SettingClickView scvAddressLocation;
    private SettingItemView sivBlack;
    private SharedPreferences mPref;
    private String[] items = new String[] {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivBlack = (SettingItemView) findViewById(R.id.siv_call_safe);

        autoUpdate();
        showAddress();
        initAddressStyle();
        initAddressLocation();
        initBlackNumber();

    }




    private void showAddress() {
        boolean showAddress = ServiceStatusUtils.isServiceRunning(this, "com.example.yifei.mobilesafe.service.AddressService");

        if (showAddress) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                } else {
                    sivAddress.setChecked(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));
                }
            }
        });
    }


    private void autoUpdate() {
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            sivUpdate.setChecked(true);
        } else {
            sivUpdate.setChecked(false);
        }
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()) {
                    sivUpdate.setChecked(false);
                    mPref.edit().putBoolean("auto_update", false).apply();
                } else {
                    sivUpdate.setChecked(true);
                    mPref.edit().putBoolean("auto_update", true).apply();

                }
            }
        });
    }

    private void initAddressStyle(){
        int which = mPref.getInt("addressStyle", 0);
        String desc = items[which];
        scvAddressStyle= (SettingClickView) findViewById(R.id.siv_address_style);
        scvAddressStyle.setTitle("归属地提示框风格");
        scvAddressStyle.setDesc(desc);
        scvAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoseDialog();
            }
        });
    }

    private void showSingleChoseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("归属地提示框风格");

        int which = mPref.getInt("addressStyle",0);
        builder.setSingleChoiceItems(items, which, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPref.edit().putInt("addressStyle", which).apply();
                dialog.dismiss();
                scvAddressStyle.setDesc(items[which]);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();

    }


    private void initAddressLocation(){

        scvAddressLocation = (SettingClickView) findViewById(R.id.siv_address_location);
        scvAddressLocation.setTitle("归属地提示框位置");
        scvAddressLocation.setDesc("设置归属提提示框的显示位置");
        scvAddressLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
            }
        });
    }

    private void initBlackNumber() {
        boolean callSafe = ServiceStatusUtils.isServiceRunning(this, "com.example.yifei.mobilesafe.service.CallSafeService");

        if (callSafe) {
            sivBlack.setChecked(true);
        } else {
            sivBlack.setChecked(false);
        }
        sivBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivBlack.isChecked()) {
                    sivBlack.setChecked(false);
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));
                } else {
                    sivBlack.setChecked(true);
                    startService(new Intent(SettingActivity.this, CallSafeService.class));
                }
            }
        });
    }
}
