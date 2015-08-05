package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yifei.mobilesafe.R;

public class Setup3Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip3);
    }


    public void next(View view){
        startActivity(new Intent(Setup3Activity.this,Setup4Activity.class));
        finish();
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }


    public void previous(View view){
        startActivity(new Intent(Setup3Activity.this,Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }
}
