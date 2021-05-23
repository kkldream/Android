package com.example.location;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class LocationService extends Service {
    private MyBinder mBinder = new MyBinder();
    public class MyBinder extends Binder {
        int var = 10;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Tag.log("Service onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Tag.log("Service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Tag.log("Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Tag.log("Service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Tag.log("Service onDestroy");
    }
}
