package com.example.week11_2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    SensorManager sensorManager;
    SensorEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                String str = "";
                str += "Name：" + event.sensor.getName() + "\n";
                str += "Type：" + event.sensor.getType() + "\n";
                str += "Power：" + event.sensor.getPower() + "\n";
                str += "values：\n";
                str += "\tX：" + event.values[0] + "\n";
                str += "\tY：" + event.values[1] + "\n";
                str += "\tZ：" + event.values[2] + "\n";
                textView.setText(str);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(listener);
        super.onPause();
    }
}
