package com.example.gm_apn_2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 999;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String apn = networkInfo.getExtraInfo();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE);
            return;
        }
        String imei = telephonyManager.getDeviceId();//手機 IMEI
        String imsi = telephonyManager.getSubscriberId();//手機 IMSI
        String roamingStatus = telephonyManager.isNetworkRoaming() ? "漫遊中" : "非漫遊";//手機漫遊狀態
        String country = telephonyManager.getNetworkCountryIso();//電信網路國別
        String operatorName = telephonyManager.getNetworkOperatorName();//電信公司名稱
        String[] networkTypeArray = {"UNKNOWN","GPRS","EDGE","UMTS","CDMA","EVDO 0","EVDO A","1xRTT","HSDPA","HSUPA","HSPA","IDEN","EVDO_B","LTE","EHRPD","HSPAP","GSM","TD SCDMA","IWLAN","LTE_CA","NR"};
        String networkType = networkTypeArray[telephonyManager.getNetworkType()];//行動網路類型
        String[] phoneTypeArray = {"NONE", "GSM", "CDMA"};
        String phoneType = phoneTypeArray[telephonyManager.getPhoneType()];//行動通訊類型
        String imsi_mcc = imsi.substring(0,3);
        String imsi_mnc = imsi.substring(3,5);
        String imsi_msin = imsi.substring(5,15);
        textView.setText("");
        textView.setText(textView.getText() + "\n存取點名稱(APN):" + apn);
        textView.setText(textView.getText() + "\n行動網路類型：" + networkType);
        textView.setText(textView.getText() + "\n行動通訊類型：" + phoneType);
        textView.setText(textView.getText() + "\n手機漫遊狀態：" + roamingStatus);
        textView.setText(textView.getText() + "\n電信網路國別：" + country);
        textView.setText(textView.getText() + "\n電信公司名稱：" + operatorName);
        textView.setText(textView.getText() + "\n國際行動裝置辨識碼(IMEI)：" + imei);
        textView.setText(textView.getText() + "\n國際移動用戶識別碼(IMSI)：" + imsi);
        textView.setText(textView.getText() + "\n移動國家碼(MCC)：" + imsi_mcc);
        textView.setText(textView.getText() + "\n行動網路碼(MNC)：" + imsi_mnc);
        textView.setText(textView.getText() + "\n移動用戶識別碼(MSIN)：" + imsi_msin);
    }
}