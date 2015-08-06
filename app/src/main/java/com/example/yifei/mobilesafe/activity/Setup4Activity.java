package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yifei.mobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
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
