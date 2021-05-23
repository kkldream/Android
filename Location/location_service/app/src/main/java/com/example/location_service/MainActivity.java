package com.example.location_service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    ToggleButton toggleButton1;
    Button button1, button2;
    LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent intent = new Intent(MainActivity.this, LocationService.class);
                    MainActivity.this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                } else {
                    locationService = null;
                    Intent intent = new Intent(MainActivity.this, LocationService.class);
                    stopService(intent);
                    unbindService(serviceConnection);
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            locationService = ((LocationService.MyBinder)iBinder).getService();
            Log.d(TAG,"onServiceConnected");
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"onServiceDisconnected");
        }
    };

    private void findViewById() {
        toggleButton1 = findViewById(R.id.toggleButton1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (locationService != null)
            Log.d(TAG,"onStart" + locationService.times);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationService != null)
            locationService = null;
            unbindService(serviceConnection);
            Log.d(TAG,"onStop" + locationService.times);
    }
}