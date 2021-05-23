package com.example.location_service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LocationService extends Service {
    private static final String TAG = "TAG_Service";
    Location location;
    int times;

    public MyBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        LocationService getService()
        {
            return  LocationService.this;
        }
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"Service onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"Service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service onCreate");
        times = 0;
        location = new Location(LocationService.this);
        location.setOnLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                double latitude = location.getLatitude(),
                        longitude = location.getLongitude();
//                Log.i(TAG, String.format("latitude = %f, longitude = %f", latitude, longitude));
                times += 1;
                Log.d(TAG,"times = " + times);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        location.stopLocationListener();
        Log.d(TAG,"Service onDestroy");
    }
}
