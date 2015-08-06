package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yifei.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip3);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }


}
