package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.bean.AppInfo;
import com.example.yifei.mobilesafe.bean.TaskInfo;
import com.example.yifei.mobilesafe.engine.TaskInfos;

import java.util.ArrayList;
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
    private List<TaskInfo> userApp;
    private List<TaskInfo> systemApp;
    private TaskManagerAdapter taskManagerAdapter;
    private List<TaskInfo> clearTasks;
    private ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_task_manager);
        tvProcessAmount = (TextView) findViewById(R.id.tv_process_amount);
        tvMemory = (TextView) findViewById(R.id.tv_memory);
        listView = (ListView) findViewById(R.id.lv_task);
        bar = (ProgressBar) findViewById(R.id.bar);
        tvProcessAmount.setText("运行中进程:" + taskAmount);
        tvMemory.setText("剩余/总内存:" + android.text.format.Formatter.formatFileSize(this, availMem)
                + "/" + android.text.format.Formatter.formatFileSize(this, totalMem));


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            clearTasks = new ArrayList<TaskInfo>();
            bar.setVisibility(View.INVISIBLE);
            taskManagerAdapter = new TaskManagerAdapter();
            listView.setAdapter(taskManagerAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object obj = listView.getItemAtPosition(position);
                    if (obj instanceof TaskInfo) {
                        TaskInfo selectedTask = (TaskInfo) obj;
                        if (selectedTask.getPackageName().equals("com.example.yifei.mobilesafe")) {
                            return;
                        }
                        CheckBox cbTask = (CheckBox) view.findViewById(R.id.cb_select);
                        if (cbTask.isChecked()) {
                            cbTask.setChecked(false);
                            clearTasks.remove(selectedTask);
                        } else {
                            cbTask.setChecked(true);
                            clearTasks.add(selectedTask);
                        }
                        System.out.println(selectedTask.getPackageName());
                    }
                }
            });


        }
    };

    private void initData() {

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        taskAmount = runningAppProcesses.size();
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        availMem = memoryInfo.availMem;
        totalMem = memoryInfo.totalMem;

        new Thread(new Runnable() {
            @Override
            public void run() {
                taskInfos = TaskInfos.getTaskInfos(TaskManagerActivity.this);
                userApp = new ArrayList<TaskInfo>();
                systemApp = new ArrayList<TaskInfo>();

                for (TaskInfo taskInfo : taskInfos) {
                    if (taskInfo.isUserApp()) {
                        userApp.add(taskInfo);
                    } else {
                        systemApp.add(taskInfo);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    class TaskManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userApp.size() + systemApp.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userApp.size() + 1) {
                return null;
            }

            TaskInfo taskInfo;

            if (position < userApp.size() + 1) {
                taskInfo = userApp.get(position - 1);
            } else {
                taskInfo = systemApp.get(position - userApp.size() - 2);
            }
            return taskInfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView tv = new TextView(TaskManagerActivity.this);
                tv.setText("用户应用 (" + userApp.size() + ")");
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position == userApp.size() + 1) {
                TextView tv = new TextView(TaskManagerActivity.this);
                tv.setText("系统应用 (" + systemApp.size() + ")");
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextColor(Color.WHITE);
                return tv;
            }

            TaskInfo taskInfo;

            if (position < userApp.size() + 1) {
                taskInfo = userApp.get(position - 1);
            } else {
                taskInfo = systemApp.get(position - userApp.size() - 2);
            }

            ViewHolder holder;

            if (convertView != null && convertView instanceof LinearLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(TaskManagerActivity.this, R.layout.item_task_manager, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvMemory = (TextView) convertView.findViewById(R.id.tv_memory);
                convertView.setTag(holder);
            }


            holder.ivIcon.setImageDrawable(taskInfo.getTaskIcon());
            holder.tvName.setText(taskInfo.getTaskName());

            holder.tvMemory.setText("" + taskInfo.getTaskMemory() / 1024 + "MB");

            if (taskInfo.getPackageName().equals("com.example.yifei.mobilesafe")) {
                convertView.findViewById(R.id.cb_select).setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvMemory;
        ImageView ivIcon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearTasks.clear();
    }

    public void clear(View view){
        if (clearTasks==null){
            Toast.makeText(TaskManagerActivity.this, "请至少选择一个进程", Toast.LENGTH_SHORT).show();
        }else{
            TaskInfos taskInfos = new TaskInfos();
            for (TaskInfo taskInfo : clearTasks ) {
                taskInfos.killTasks(taskInfo.getPackageName(),this);
            }
            clearTasks.clear();
            initData();
            initUI();
        }
    }
    public void selectAll(View view){
        return;
    }

    public void unSelectAll(View view){
        return;
    }
}
