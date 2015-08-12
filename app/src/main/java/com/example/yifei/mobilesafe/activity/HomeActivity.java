package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.utils.MD5Utils;

import org.w3c.dom.Text;

/*
* 主界面
* */


public class HomeActivity extends Activity {
    private GridView gvHome;
    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"};
    private int[] mPic = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());

        //设置点击监听
        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //高级工具
                    case 7:
                        startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;

                    case 1:
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity2.class));
                        break;

                    case 2:
                        startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
                        break;
                    //设置中心
                    case 8:
                        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                        break;

                    //手机防盗
                    case 0:
                        showPasswordDialog();
                        break;


                    default:
                        break;
                }
            }
        });
    }


    //显示密码弹窗
    private void showPasswordDialog() {
        //判断是否已经设置过密码
        String savedPassword = mPref.getString("password", null);
        if (!TextUtils.isEmpty(savedPassword)) {
            showInputDialog();
        } else {
            showPasswordSetDialog();
        }


    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_input_password, null);
        dialog.setView(view);

        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);


        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(password)) {
                    String savedPassword = mPref.getString("password", null);
                    if (MD5Utils.encode(password).equals(savedPassword)) {
                        Toast.makeText(HomeActivity.this, "登陆成功!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "密码错误!!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }


    /*
    * 弹出设置密码弹窗
    * */
    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dialog_set_password, null);
        dialog.setView(view);

        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);

        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String passworfConfirm = etPasswordConfirm.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passworfConfirm)) {
                    if (password.equals(passworfConfirm)) {
                        Toast.makeText(HomeActivity.this, "登录成功!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        //登录成功,将密码写入数据库
                        mPref.edit().putString("password", MD5Utils.encode(password)).apply();

                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    } else {
                        Toast.makeText(HomeActivity.this, "两次输入不一致!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "输入框不能为空!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPic[position]);
            return view;
        }
    }
}
