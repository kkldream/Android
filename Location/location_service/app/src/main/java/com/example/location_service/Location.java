package com.example.location_service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

@SuppressLint("MissingPermission")
public class Location {
    String bestProvider;
    LocationManager locationManager;
    LocationListener locationListener;

    public Location(Context context) {
        checkPermission(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//位置解析的精度,引數:ACCURACY_FINE=經,ACCURACY_COARSE=模糊模式
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//位置解析的精度,引數:ACCURACY_FINE=經,ACCURACY_COARSE=模糊模式
        criteria.setAltitudeRequired(false);//是否提供海拔高度資訊
        criteria.setBearingRequired(false);//是否提供方向資訊
        criteria.setCostAllowed(true);//是否允許運營商計費
        criteria.setSpeedRequired(false);//是否提供速度資訊
        criteria.setPowerRequirement(Criteria.POWER_HIGH);//電池消耗,引數:無=NO_REQUIREMENT,低=POWER_LOW,中=POWER_MEDIUM,高=POWER_HIGH
        bestProvider = locationManager.getBestProvider(criteria, true);
    }

    public double[] getLocation() {
        android.location.Location location = locationManager.getLastKnownLocation(bestProvider);
        return new double[]{location.getLatitude(), location.getLongitude()};
    }

    public void setOnLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
        locationManager.requestLocationUpdates(bestProvider, 10, 0, locationListener);
    }

    public void stopLocationListener() {
        locationManager.removeUpdates(locationListener);
    }

    private void checkPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }
}
