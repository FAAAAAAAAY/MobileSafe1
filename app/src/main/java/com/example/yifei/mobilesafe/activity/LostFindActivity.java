package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;

public class LostFindActivity extends Activity {

    private SharedPreferences mPref;
    private TextView safePhone;
    private ImageView protectLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);

        boolean configed = mPref.getBoolean("configed",false);

        if (configed){
            setContentView(R.layout.activity_lost_find);
            safePhone = (TextView) findViewById(R.id.safePhone);
            protectLock = (ImageView) findViewById(R.id.protect_lock);
            String phone = mPref.getString("safePhone","");
            safePhone.setText(phone);
            boolean protect = mPref.getBoolean("protect",false);
            if (protect){
                protectLock.setImageResource(R.drawable.lock);
            }else {
                protectLock.setImageResource(R.drawable.unlock);
            }
        }else {
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }


    }


    public void reEnter(View view){
        startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }

}
