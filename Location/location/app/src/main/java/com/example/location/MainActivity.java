package com.example.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    ToggleButton toggleButton;
    Button button;
    TextView textView1, textView2;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();

        location = new Location(this);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    location.setOnLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull android.location.Location location) {
                            double latitude = location.getLatitude(),
                                    longitude = location.getLongitude();
                            Log.i(TAG, String.format("latitude = %f, longitude = %f", latitude, longitude));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView1.setText(String.format("latitude = %f, longitude = %f", latitude, longitude));
                                }
                            });
                        }
                    });
                } else {
                    location.stopLocationListener();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setText("stopLocationListener");
                        }
                    });
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = location.getLocation()[0],
                        longitude = location.getLocation()[1];
                Log.i(TAG, String.format("latitude = %f, longitude = %f", latitude, longitude));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView2.setText(String.format("latitude = %f, longitude = %f", latitude, longitude));
                    }
                });
            }
        });
    }

    private void findViewById() {
        toggleButton = findViewById(R.id.toggleButton);
        button = findViewById(R.id.button);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
    }
}