package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yifei.mobilesafe.R;

public class Setup2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip2);
    }

    public void next(View view){
        startActivity(new Intent(Setup2Activity.this,Setup3Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

    public void previous(View view){
        startActivity(new Intent(Setup2Activity.this,Setup1Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }
}
