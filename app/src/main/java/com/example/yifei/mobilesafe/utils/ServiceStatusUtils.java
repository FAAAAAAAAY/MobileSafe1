package com.example.yifei.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Yifei on 2015/8/7.
 */
public class ServiceStatusUtils {

    public static boolean isServiceRunning(Context context, String serviceName){

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices){
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)){
                return true;
            }
        }
        return false;
    }

}
