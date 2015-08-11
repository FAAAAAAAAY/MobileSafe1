package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.adapter.MyBaseAdapter;
import com.example.yifei.mobilesafe.bean.BlackNumberInfo;
import com.example.yifei.mobilesafe.db.dao.BlackNumberDao;

import java.util.ArrayList;
import java.util.List;

public class CallSafeActivity2 extends Activity {

    private ListView listView;
    private List<BlackNumberInfo> numberInfoList = new ArrayList<>();
    private LinearLayout llPb;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;
    int startIndex = 0;
    int maxCount = 20;
    private int total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe2);
        initUI();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llPb.setVisibility(View.INVISIBLE);
            if (adapter==null){
                adapter = new CallSafeAdapter(CallSafeActivity2.this, numberInfoList);
                listView.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged();
            }

        }
    };

    private void initData() {
        dao = new BlackNumberDao(CallSafeActivity2.this);
        total = dao.getTotal();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (numberInfoList == null){
                    numberInfoList = dao.findPar2(startIndex, maxCount);

                }else{
                    numberInfoList.addAll(dao.findPar2(startIndex, maxCount));
                }
                handler.sendEmptyMessage(0);
            }
        }).start();


    }

    private void initUI() {
        llPb = (LinearLayout) findViewById(R.id.ll_pb);
        llPb.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                System.out.println("scrollstatechanged");
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int lastVisiblePosition = listView.getLastVisiblePosition();
                        if (lastVisiblePosition == numberInfoList.size() -1){
                            startIndex += maxCount;
                            if (lastVisiblePosition >= total - 1){
                                Toast.makeText(CallSafeActivity2.this, "已经没有数据了", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            initData();
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                System.out.println("onscroll");
            }
        });
    }

    class CallSafeAdapter extends MyBaseAdapter<BlackNumberInfo> {


        public CallSafeAdapter(Context context, List<BlackNumberInfo> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(CallSafeActivity2.this, R.layout.item_call_safe, null);
                holder = new ViewHolder();
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
                holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);

                convertView.setTag(holder);
            }

            String number = list.get(position).getNumber();
            String mode = list.get(position).getMode();

            holder.tvNumber.setText(number);
            if (mode.equals("1")) {
                holder.tvMode.setText("拦截电话+短信");
            } else if (mode.equals("2")) {
                holder.tvMode.setText("电话拦截");
            } else {
                holder.tvMode.setText("短信拦截");
            }

            final BlackNumberInfo info = list.get(position);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = info.getNumber();
                    boolean result = dao.delete(number);
                    if (result){
                        Toast.makeText(CallSafeActivity2.this, "删除成功", Toast.LENGTH_SHORT).show();
                        list.remove(info);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(CallSafeActivity2.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }


    }


    static class ViewHolder {
        TextView tvNumber;
        TextView tvMode;
        ImageView ivDelete;
    }



    public void addNumber(View view){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(this,R.layout.dialog_add_number,null);
        final EditText etAddnumber = (EditText) dialogView.findViewById(R.id.et_addNumber);
        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final CheckBox cb_phone = (CheckBox) dialogView.findViewById(R.id.cb_phoneBlock);
        final CheckBox cb_sms = (CheckBox) dialogView.findViewById(R.id.cb_smsBlock);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = etAddnumber.getText().toString().trim();
                boolean phoneChecked = cb_phone.isChecked();
                boolean smsChecked = cb_sms.isChecked();
                String mode = "";

                if (TextUtils.isEmpty(number)){
                    Toast.makeText(CallSafeActivity2.this, "请输入一个号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phoneChecked && !smsChecked){
                    Toast.makeText(CallSafeActivity2.this, "请至少选择一个拦截方式", Toast.LENGTH_SHORT).show();
                    return;
                }else if(phoneChecked && !smsChecked){
                    mode = "2";
                    dialog.dismiss();

                }else if (smsChecked && !phoneChecked){
                    mode = "3";
                    dialog.dismiss();

                }else if (phoneChecked && smsChecked){
                    mode = "1";
                    dialog.dismiss();
                }

                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(number);
                blackNumberInfo.setMode(mode);
                numberInfoList.add(0,blackNumberInfo);

                dao.add(number,mode);
                if (adapter == null){
                    adapter = new CallSafeAdapter(CallSafeActivity2.this,numberInfoList);
                    listView.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }







}
