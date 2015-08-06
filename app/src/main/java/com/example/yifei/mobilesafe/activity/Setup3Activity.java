package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {


    private EditText etSafeNumber;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip3);
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        etSafeNumber = (EditText) findViewById(R.id.et_safeNumber);
        String phone = mPref.getString("safePhone","");
        etSafeNumber.setText(phone);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.previous_in, R.anim.previous_out);
    }

    @Override
    public void showNext() {
        String phone = etSafeNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)){
            mPref.edit().putString("safePhone",phone).apply();
            startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
            finish();
            overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
        }else {
            Toast.makeText(this,"请输入安全号码",Toast.LENGTH_SHORT).show();
        }

    }


    public void readContact(View view){
        startActivityForResult(new Intent(this, ContactActivity.class), 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll(" ","").replaceAll("-","");
            etSafeNumber.setText(phone);
        }

    super.onActivityResult(requestCode,resultCode,data);
    }
}
