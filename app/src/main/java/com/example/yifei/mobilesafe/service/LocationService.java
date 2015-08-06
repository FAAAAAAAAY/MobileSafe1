package com.example.yifei.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

    private LocationManager lm;
    private MyLocationListener myLocationListener;
    private SharedPreferences mPref;

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config",MODE_PRIVATE);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, myLocationListener);
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String j = "经度:" + location.getLongitude();
            String w = "维度:" + location.getLatitude();
            String accuracy = "精确度:" + location.getAccuracy();
            String altitude = "海拔:" + location.getAltitude();
            mPref.edit().putString("location", j+w).apply();
            stopSelf();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(myLocationListener);
    }
}
