package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CallSafeActivity extends Activity {

    private ListView listView;
    private List<BlackNumberInfo> numberInfoList = new ArrayList<>();
    private LinearLayout llPb;
    private int mCurrentPage = 0;
    private int mPageSize = 20;
    private int mPageNumber;
    private TextView tvPageNumber;
    private EditText etPageNumber;
    private BlackNumberDao dao;
    private CallSafeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        initUI();
        initData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int showPage = mCurrentPage + 1;
            tvPageNumber.setText(showPage + "/" + mPageNumber);
            llPb.setVisibility(View.INVISIBLE);
            adapter = new CallSafeAdapter(CallSafeActivity.this, numberInfoList);
            listView.setAdapter(adapter);
        }
    };

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao = new BlackNumberDao(CallSafeActivity.this);
                mPageNumber = dao.getTotal() / mPageSize;

                //numberInfoList = dao.findAll();
                numberInfoList = dao.findPar(mCurrentPage, mPageSize);

                handler.sendEmptyMessage(0);
            }
        }).start();


    }

    private void initUI() {
        llPb = (LinearLayout) findViewById(R.id.ll_pb);
        llPb.setVisibility(View.VISIBLE);
        listView = (ListView) findViewById(R.id.list_view);
        tvPageNumber = (TextView) findViewById(R.id.tv_page_number);
        etPageNumber = (EditText) findViewById(R.id.et_page_number);
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
                convertView = View.inflate(CallSafeActivity.this, R.layout.item_call_safe, null);
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
                        Toast.makeText(CallSafeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        list.remove(info);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(CallSafeActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
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


    public void prePage(View view) {
        if (mCurrentPage == 0) {
            Toast.makeText(this, "已经是第一页", Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentPage--;
        initData();
    }

    public void nextPage(View view) {
        if (mCurrentPage + 1 == mPageNumber) {
            Toast.makeText(this, "已经是最后一页", Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrentPage++;
        initData();
    }


    public void jump(View view) {
        if (TextUtils.isEmpty(etPageNumber.getText().toString())) {
            Toast.makeText(this, "请输入页数", Toast.LENGTH_SHORT).show();
        }
        int page = Integer.parseInt(etPageNumber.getText().toString()) - 1;
        if (page < 0 || page > mPageNumber - 1) {
            Toast.makeText(this, "页数不正确", Toast.LENGTH_SHORT).show();
        } else {
            mCurrentPage = page;
            initData();
            etPageNumber.setText("");
        }
    }


}
