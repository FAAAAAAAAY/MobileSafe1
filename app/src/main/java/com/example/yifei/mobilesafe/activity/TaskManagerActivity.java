package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.bean.TaskInfo;
import com.example.yifei.mobilesafe.engine.TaskInfos;

import java.util.Formatter;
import java.util.List;

public class TaskManagerActivity extends Activity {
    private TextView tvProcessAmount;
    private TextView tvMemory;
    private ListView listView;
    private int taskAmount;
    private long availMem;
    private long totalMem;
    private List<TaskInfo> taskInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();
    }

    private void initUI(){
        setContentView(R.layout.activity_task_manager);
        tvProcessAmount = (TextView) findViewById(R.id.tv_process_amount);
        tvMemory = (TextView) findViewById(R.id.tv_memory);
        listView = (ListView) findViewById(R.id.lv_task);
        tvProcessAmount.setText("运行中进程:"+taskAmount);
        tvMemory.setText("剩余/总内存:" + android.text.format.Formatter.formatFileSize(this,availMem)
                +"/" + android.text.format.Formatter.formatFileSize(this,totalMem));

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private void initData(){
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        taskAmount = runningAppProcesses.size();
        ActivityManager.MemoryInfo memoryInfo= new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        availMem = memoryInfo.availMem;
        totalMem = memoryInfo.totalMem;

        new Thread(new Runnable() {
            @Override
            public void run() {
                taskInfos = TaskInfos.getTaskInfos(TaskManagerActivity.this);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    class TaskManagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
