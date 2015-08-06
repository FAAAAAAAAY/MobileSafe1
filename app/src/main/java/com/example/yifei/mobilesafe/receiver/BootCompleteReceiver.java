package com.example.yifei.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences mPref;

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mPref = context.getSharedPreferences("config",context.MODE_PRIVATE);
        String sim = mPref.getString("simSerialNumber",null);
        if (TextUtils.isEmpty(sim)){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String currentSim = tm.getSimSerialNumber();

            if (sim.equals(currentSim)){
                System.out.println("sim卡没有变化,手机安全");
            }else {
                System.out.println("sim卡已经更换!");
            }
        }
    }
}
