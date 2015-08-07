package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.db.dao.AddressDao;

public class AddressActivity extends Activity {
    private EditText etNumber;
    private TextView tvAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        etNumber = (EditText) findViewById(R.id.et_number);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(s.toString());
                tvAddress.setText(address);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void query (View view){
        String number= etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)){
            Toast.makeText(this,"号码不能为空",Toast.LENGTH_SHORT).show();
            vibrate();
        }else {
            String address = AddressDao.getAddress(number);
            tvAddress.setText(address);
        }
    }

    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

}
