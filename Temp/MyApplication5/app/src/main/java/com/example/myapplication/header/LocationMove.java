package com.example.myapplication.header;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

@SuppressLint("MissingPermission")
public class LocationMove {
    String bestProvider;
    LocationManager locationManager;
    LocationListener locationListener;
    double[] last_location = {404, 404};
    @SuppressLint("WrongConstant")
    public LocationMove(Object SystemService) {
        locationManager = (LocationManager) SystemService;
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
        Location location = locationManager.getLastKnownLocation(bestProvider);
        return new double[]{location.getLatitude(), location.getLongitude()};
    }

    public void start(LocationListener locationListener) {
        this.locationListener = locationListener;
        locationManager.requestLocationUpdates(bestProvider, 10, 1, locationListener);
    }

    public void stop() {
        locationManager.removeUpdates(locationListener);
        last_location = new double[]{404, 404};
    }

    double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    double getDistance(double LonA, double LatA, double LonB, double LatB) {
        double R = 6371.004; // 地球半徑(千米)
        double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB)) + Math.cos(rad(LatA)) * Math.cos(rad(LatB)) * Math.cos(rad(LonA - LonB));
//        return R * Math.acos(C); // 單位千米
        return R * Math.acos(C) * 1000; // 單位米
    }
    public double getMoveDistance(double nLon, double nLat) {
        if (last_location[0] == 404) {
            last_location = new double[]{nLon, nLat};
            return 0;
        } else {
            double move_distance = getDistance(last_location[0], last_location[1], nLon, nLat);
            last_location = new double[]{nLon, nLat};
            return move_distance;
        }
    }
}
