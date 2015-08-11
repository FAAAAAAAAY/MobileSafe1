package com.example.yifei.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

import com.example.yifei.mobilesafe.db.dao.BlackNumberDao;

import java.util.Objects;

public class CallSafeService extends Service {

    private BlackNumberDao dao;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        CallSafeReceiver callSafeReceiver = new CallSafeReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(callSafeReceiver, intentFilter);
        dao = new BlackNumberDao(this);
    }


    private class CallSafeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);

                String originalAddress = smsMessage.getOriginatingAddress();
                String messagebody = smsMessage.getMessageBody();
                System.out.println("号码:" + originalAddress);
                String mode = dao.findNumber(originalAddress);
                if (mode.equals("1") || mode.equals("3")){
                    abortBroadcast();
                    System.out.println("拦截成功");
                }

            }
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
