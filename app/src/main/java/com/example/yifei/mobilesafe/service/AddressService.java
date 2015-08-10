package com.example.yifei.mobilesafe.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.db.dao.AddressDao;

public class AddressService extends Service {
    private TelephonyManager tm;
    private MyListener listener;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private View toastView;
    private SharedPreferences mPref;
    private int startX;
    private int startY;
    private WindowManager.LayoutParams params ;
    private int winWidth;
    private int winHeight;


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
        mPref = getSharedPreferences("config",MODE_PRIVATE);
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
                    //Toast.makeText(AddressService.this,address,Toast.LENGTH_LONG).show();
                    showToast(address);
                    System.out.println(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (windowManager!=null && toastView!=null){
                        windowManager.removeView(toastView);
                        toastView =null;
                    }

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
            //Toast.makeText(context,address,Toast.LENGTH_LONG).show();
            showToast(address);
        }
    }

    private void showToast(String text){

        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        winWidth = windowManager.getDefaultDisplay().getWidth();
        winHeight = windowManager.getDefaultDisplay().getHeight();
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        params.setTitle("Toast");

        int which = mPref.getInt("addressStyle",0);
        int top = mPref.getInt("dragTop",0);
        int left = mPref.getInt("dragLeft", 0);
        params.x = left;
        params.y = top;
        toastView = View.inflate(this, R.layout.toast_address,null);
        TextView tvNumber = (TextView) toastView.findViewById(R.id.tv_number);
        tvNumber.setText(text);
        int[] styles = new int[] {R.drawable.call_locate_white,R.drawable.call_locate_orange,
                        R.drawable.call_locate_blue,R.drawable.call_locate_gray,R.drawable.call_locate_green};
        toastView.setBackgroundResource(styles[which]);




        windowManager.addView(toastView, params);
        toastView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int dx = endX - startX;
                        int dy = endY - startY;
                        params.x += dx;
                        params.y += dy;
                        if (params.x < 0 ){
                            params.x=0;
                        }
                        if (params.y < 0 ){
                            params.y=0;
                        }
                        if (params.x > winWidth-toastView.getWidth() ){
                            params.x = winWidth - toastView.getWidth();
                        }
                        if (params.y > winHeight-toastView.getHeight() ){
                            params.y = winHeight - toastView.getHeight();
                        }
                        windowManager.updateViewLayout(toastView,params);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                    case MotionEvent.ACTION_UP:
                        mPref.edit().putInt("dragTop", params.y).apply();
                        mPref.edit().putInt("dragLeft", params.x).apply();
                    default:
                        break;

                }
                return true;
            }
        });

    }
}
