package com.example.gm_gps_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button1, button2;
    TextView textView;
    Location location;
    LocationManager locationManager;
    LocationListener locationListener;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView = findViewById(R.id.textView);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果裝置版本是6.0以上請求權限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_ACCESS_FINE_LOCATION);
                        return;
                    }
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);//位置解析的精度,引數:ACCURACY_FINE=經,ACCURACY_COARSE=模糊模式
                criteria.setAltitudeRequired(false);//是否提供海拔高度資訊
                criteria.setBearingRequired(false);//是否提供方向資訊
                criteria.setCostAllowed(true);//是否允許運營商計費
                criteria.setSpeedRequired(false);//是否提供速度資訊
                criteria.setPowerRequirement(Criteria.POWER_LOW);//電池消耗,引數:無=NO_REQUIREMENT,低=POWER_LOW,中=POWER_MEDIUM,高=POWER_HIGH
                String provider = locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(provider);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        textView.setText("GPS定位(精確)\n經度=" + location.getLongitude() + "\n緯度=" + location.getLatitude());
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
                };
                locationManager.requestLocationUpdates(provider, 10, 0, locationListener);
                location = locationManager.getLastKnownLocation(provider);
                if(location != null)
                    textView.setText("GPS定位(精確)\n經度=" + location.getLongitude() + "\n緯度=" + location.getLatitude());
                else
                    textView.setText("GPS定位(精確)\n定位中");
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果裝置版本是6.0以上請求權限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_ACCESS_COARSE_LOCATION);
                        return;
                    }
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);//位置解析的精度,引數:ACCURACY_FINE=精確模式,ACCURACY_COARSE=模糊模式
                criteria.setAltitudeRequired(false);//是否提供海拔高度資訊
                criteria.setBearingRequired(false);//是否提供方向資訊
                criteria.setCostAllowed(true);//是否允許運營商計費
                criteria.setSpeedRequired(false);//是否提供速度資訊
                criteria.setPowerRequirement(Criteria.POWER_LOW);//電池消耗,引數:無=NO_REQUIREMENT,低=POWER_LOW,中=POWER_MEDIUM,高=POWER_HIGH
                String provider = locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(provider);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        textView.setText("網路定位(模糊)\n經度=" + location.getLongitude() + "\n緯度=" + location.getLatitude());
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
                };
                locationManager.requestLocationUpdates(provider, 10, 0, locationListener);
                location = locationManager.getLastKnownLocation(provider);
                if(location != null)
                    textView.setText("網路定位(模糊)\n經度=" + location.getLongitude() + "\n緯度=" + location.getLatitude());
                else
                    textView.setText("網路定位(模糊)\n定位中");
            }
        });
    }
}