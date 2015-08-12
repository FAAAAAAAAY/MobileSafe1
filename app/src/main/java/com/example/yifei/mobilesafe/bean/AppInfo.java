package com.example.yifei.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Yifei on 2015/8/12.
 */
public class AppInfo {
    private Drawable icon;

    private String apkName;

    private long apkSize;

    private boolean isUserApp;

    private boolean isRom;

    private String apkPackageName;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApkSize() {
        return apkSize;
    }

    public void setApkSize(long apkSize) {
        this.apkSize = apkSize;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    @Override
    public String toString() {
        return "AppInfos{" +
                "apkName='" + apkName + '\'' +
                ", apkSize=" + apkSize +
                ", isUserApp=" + isUserApp +
                ", isRom=" + isRom +
                ", apkPackageName='" + apkPackageName + '\'' +
                '}';
    }
}
