package com.example.gm_test_1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 999;
    //元件定義
    TextView textView1,textView2,textView3,textView4;
    Button button1,button2,button3,button4;
    //GPS定義
    int gps_mode = 0;
    Location location;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //獲取APN
                ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                String apn = networkInfo.getExtraInfo();
                //檢查並獲取READ_PHONE_STATE權限
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE);
                    return;
                }
                //獲取手機資訊
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String android_id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String imei = null;
                String imsi = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    imei = telephonyManager.getDeviceId();//IMEI
                    imsi = telephonyManager.getSubscriberId();//IMSI
                }
                String roamingStatus = telephonyManager.isNetworkRoaming() ? "漫遊中" : "非漫遊";//手機漫遊狀態
                String country = telephonyManager.getNetworkCountryIso();//電信網路國別
                String operatorName = telephonyManager.getNetworkOperatorName();//電信公司名稱
                String[] networkTypeArray = {"UNKNOWN","GPRS","EDGE","UMTS","CDMA","EVDO 0","EVDO A","1xRTT","HSDPA","HSUPA","HSPA","IDEN","EVDO_B","LTE","EHRPD","HSPAP","GSM","TD SCDMA","IWLAN","LTE_CA","NR"};
                String networkType = networkTypeArray[telephonyManager.getNetworkType()];//行動網路類型
                String[] phoneTypeArray = {"NONE", "GSM", "CDMA"};
                String phoneType = phoneTypeArray[telephonyManager.getPhoneType()];//行動通訊類型
                //顯示
                textView1.setText("存取點名稱(APN)：" + apn);
                textView1.setText(textView1.getText() + "\nAndroid ID：" + android_id);
                textView1.setText(textView1.getText() + "\n行動網路類型：" + networkType);
                textView1.setText(textView1.getText() + "\n行動通訊類型：" + phoneType);
                textView1.setText(textView1.getText() + "\n手機漫遊狀態：" + roamingStatus);
                textView1.setText(textView1.getText() + "\n電信網路國別：" + country);
                textView1.setText(textView1.getText() + "\n電信公司名稱：" + operatorName);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    textView1.setText(textView1.getText() + "\n國際行動裝置辨識碼(IMEI)：" + imei);
                    textView1.setText(textView1.getText() + "\n國際移動用戶識別碼(IMSI)：" + imsi);
                    textView1.setText(textView1.getText() + "\n移動國家碼(MCC)：" + imsi.substring(0,3));
                    textView1.setText(textView1.getText() + "\n行動網路碼(MNC)：" + imsi.substring(3,5));
                    textView1.setText(textView1.getText() + "\n移動用戶識別碼(MSIN)：" + imsi.substring(5,15));
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果裝置版本是6.0以上請求權限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE);
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
                        textView2.setText("GPS定位(精確)\n經度：" + location.getLongitude() + "\n緯度：" + location.getLatitude());
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
                    textView2.setText("GPS定位(精確)\n經度：" + location.getLongitude() + "\n緯度：" + location.getLatitude());
                else
                    textView2.setText("GPS定位(精確)\n定位中");
                gps_mode = 1;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果裝置版本是6.0以上請求權限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE);
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
                        textView3.setText("網路定位(模糊)\n經度：" + location.getLongitude() + "\n緯度：" + location.getLatitude());
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
                    textView3.setText("網路定位(模糊)\n經度：" + location.getLongitude() + "\n緯度：" + location.getLatitude());
                else
                    textView3.setText("網路定位(模糊)\n定位中");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        });

    }
    private class MyPhoneStateListener extends PhoneStateListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int asu = signalStrength.getGsmSignalStrength();
            int dbm = -113 + 2 * asu;
            int level = signalStrength.getLevel();
            if (asu == 99) {
                textView4.setText("訊號強度(1~6)：Level " + (level + 1));
            }
            else {
                textView4.setText("訊號強度(1~6)：" + (level + 1));
                textView4.setText(textView4.getText() + "\nasu：" + asu);
                textView4.setText(textView4.getText() + "\ndBm：" + dbm);
            }
        }
    }
}