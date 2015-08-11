package com.example.yifei.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Message;
import android.telephony.SmsMessage;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.service.LocationService;

import java.util.Objects;

public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences mPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] objects = (Object[]) intent.getExtras().get("pdus");

        for (Object object : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);

            String originalAddress = smsMessage.getOriginatingAddress();
            String messagebody = smsMessage.getMessageBody();

            if ("#*alarm*#".equals(messagebody)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(0.3f, 0.3f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();


                abortBroadcast();
            } else if ("#*location*#".equals(messagebody)) {
                context.startService(new Intent(context, LocationService.class));
                mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String location = mPref.getString("location","Lcationing......");
                abortBroadcast();
            }
        }

    }
}
