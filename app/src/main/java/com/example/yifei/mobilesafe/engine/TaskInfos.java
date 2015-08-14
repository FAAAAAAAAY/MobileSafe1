package com.example.yifei.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.yifei.mobilesafe.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yifei on 2015/8/14.
 */
public class TaskInfos {

    public static List<TaskInfo> getTaskInfos(Context context){
        List<TaskInfo> list = new ArrayList<TaskInfo>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        PackageManager packageManager = context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();
            String processName = processInfo.processName;
            taskInfo.setPackageName(processName);
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(processName, 0);
                Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.setTaskIcon(icon);
                String taskName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.setTaskName(taskName);


                int flags = packageInfo.applicationInfo.flags;

                if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    taskInfo.setIsUserApp(false);
                } else {
                    taskInfo.setIsUserApp(true);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            list.add(taskInfo);
        }

        return list;
    }

}
