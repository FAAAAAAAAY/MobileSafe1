package com.example.yifei.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.yifei.mobilesafe.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Yifei on 2015/8/12.
 */
public class AppInfos {


    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<AppInfo>();

        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (PackageInfo installedPackage : installedPackages) {
            AppInfo appInfo = new AppInfo();
            Drawable drawable = installedPackage.applicationInfo.loadIcon(packageManager);
            String apkName = installedPackage.applicationInfo.loadLabel(packageManager).toString();
            String packageName = installedPackage.applicationInfo.packageName;
            String sourceDir = installedPackage.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            long apkSize = file.length();
            /*System.out.println("名字:" + apkName);
            System.out.println("包名:" + packageName);
            System.out.println("大小:" + apkSize);*/
            int flags = installedPackage.applicationInfo.flags;

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setIsUserApp(false);
            } else {
                appInfo.setIsUserApp(true);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                appInfo.setIsRom(false);
            } else {
                appInfo.setIsRom(true);
            }

            appInfo.setApkName(apkName);
            appInfo.setIcon(drawable);
            appInfo.setApkSize(apkSize);
            appInfo.setApkPackageName(packageName);


            appInfos.add(appInfo);
        }

        return appInfos;

    }
}
