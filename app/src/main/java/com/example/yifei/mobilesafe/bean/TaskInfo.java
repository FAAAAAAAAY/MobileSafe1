package com.example.yifei.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Yifei on 2015/8/14.
 */
public class TaskInfo {
    private String taskName;
    private Drawable taskIcon;
    private String packageName;
    private long taskSize;
    private boolean isUserApp;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    public long getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(long taskSize) {
        this.taskSize = taskSize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getTaskIcon() {
        return taskIcon;
    }

    public void setTaskIcon(Drawable taskIcon) {
        this.taskIcon = taskIcon;
    }
}
