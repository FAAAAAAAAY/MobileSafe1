package com.example.yifei.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.db.dao.AddressDao;

public class AddressService extends Service {
    private TelephonyManager tm;
    private MyListener listener;
    private OutCallReceiver receiver;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        System.out.println("服务已启动");
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        receiver =new OutCallReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);
    }

    class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("电话铃响");
                    String address = AddressDao.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this,address,Toast.LENGTH_LONG).show();
                    System.out.println(address);
                    break;
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    class OutCallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = getResultData();
            String address = AddressDao.getAddress(number);
            Toast.makeText(context,address,Toast.LENGTH_LONG).show();
        }
    }

    private void showToast(String text){

        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.GREEN);
        windowManager.addView(textView,params);

    }
}
