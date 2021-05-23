package com.example.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    ToggleButton toggleButton1;
    Button button1, button2;
    Location location;
    LocationService.MyBinder mBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        location = new Location(this);
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    location.setOnLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull android.location.Location location) {
                            double latitude = location.getLatitude(),
                                    longitude = location.getLongitude();
                            Log.i(Tag.location, String.format("latitude = %f, longitude = %f", latitude, longitude));
                        }
                    });
                } else {
                    location.stopLocationListener();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Tag.location, Arrays.toString(location.getLocation()));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LocationService.class);
//                MainActivity.this.startService(intent);
                MainActivity.this.bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        mBinder = (LocationService.MyBinder)iBinder;
                        Tag.log("iBinder: " + String.valueOf(mBinder.var));
                        mBinder.var += 1;
                        Tag.log("onServiceConnected");
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        Tag.log("onServiceDisconnected");

                    }
                }, Context.BIND_AUTO_CREATE);
            }
        });
    }

    private void findViewById() {
        toggleButton1 = findViewById(R.id.toggleButton1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag.activity, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag.activity, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag.activity, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag.activity, "onStop");
    }
}