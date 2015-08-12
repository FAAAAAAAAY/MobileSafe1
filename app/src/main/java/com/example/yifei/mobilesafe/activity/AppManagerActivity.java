package com.example.yifei.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yifei.mobilesafe.R;
import com.example.yifei.mobilesafe.bean.AppInfo;
import com.example.yifei.mobilesafe.engine.AppInfos;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity {
    private ListView listView;
    private TextView tvRom;
    private TextView tvSd;
    private List<AppInfo> appInfos;
    private AppManagerAdapter adapter;
    private List<AppInfo> userApp;
    private List<AppInfo> systemApp;
    private TextView tvApp;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_app_manager);
        listView = (ListView) findViewById(R.id.list_view);
        tvRom = (TextView) findViewById(R.id.tv_rom);
        tvSd = (TextView) findViewById(R.id.tv_sd);
        tvApp = (TextView) findViewById(R.id.tv_app);
        long freeSpaceRom = Environment.getDataDirectory().getFreeSpace();
        long freeSpaceSd = Environment.getExternalStorageDirectory().getFreeSpace();

        tvRom.setText("内存可用:" + Formatter.formatFileSize(this, freeSpaceRom));
        tvSd.setText("SD卡可用:" + Formatter.formatFileSize(this, freeSpaceSd));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new AppManagerAdapter();
            listView.setAdapter(adapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (userApp != null && systemApp != null) {
                        if (firstVisibleItem > userApp.size() + 1) {
                            tvApp.setText("系统应用 (" + systemApp.size() + ")");
                        } else {
                            tvApp.setText("用户应用 (" + userApp.size() + ")");
                        }
                    }
                    popUpWindowDismiss();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popUpWindowDismiss();
                    Object obj = listView.getItemAtPosition(position);
                    if (obj instanceof AppInfo && obj != null) {
                        View contentView = View.inflate(AppManagerActivity.this, R.layout.popup_app_manager, null);
                        popupWindow = new PopupWindow(contentView, -2, -2);
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        int[] ints = new int[2];
                        view.getLocationInWindow(ints);
                        popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 60, ints[1]);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF,
                                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setDuration(300);
                        contentView.startAnimation(scaleAnimation);

                    }


                }
            });
        }
    };

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfos = AppInfos.getAppInfos(AppManagerActivity.this);

                userApp = new ArrayList<AppInfo>();
                systemApp = new ArrayList<AppInfo>();

                for (AppInfo appInfo : appInfos) {
                    if (appInfo.isUserApp()) {
                        userApp.add(appInfo);
                    } else {
                        systemApp.add(appInfo);
                    }
                }


                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    private class AppManagerAdapter extends BaseAdapter {

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

            AppInfo appInfo;

            if (position < userApp.size() + 1) {
                appInfo = userApp.get(position - 1);
            } else {
                appInfo = systemApp.get(position - userApp.size() - 2);
            }
            return appInfo;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (position == 0) {
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("用户应用 (" + userApp.size() + ")");
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position == userApp.size() + 1) {
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("系统应用 (" + systemApp.size() + ")");
                tv.setBackgroundColor(Color.BLACK);
                tv.setTextColor(Color.WHITE);
                return tv;
            }

            AppInfo appInfo;

            if (position < userApp.size() + 1) {
                appInfo = userApp.get(position - 1);
            } else {
                appInfo = systemApp.get(position - userApp.size() - 2);
            }

            ViewHolder holder;

            if (convertView != null && convertView instanceof LinearLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvSize = (TextView) convertView.findViewById(R.id.tv_size);
                convertView.setTag(holder);
            }


            holder.ivIcon.setImageDrawable(appInfo.getIcon());
            holder.tvName.setText(appInfo.getApkName());
            boolean isRom = appInfo.isRom();
            if (isRom) {
                holder.tvLocation.setText("手机内存");
            } else {
                holder.tvLocation.setText("SD卡");
            }
            holder.tvSize.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));


            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvLocation;
        TextView tvSize;
        ImageView ivIcon;
    }

    private void popUpWindowDismiss(){
        if (popupWindow!=null && popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popUpWindowDismiss();
    }
}
