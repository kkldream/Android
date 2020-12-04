package com.example.week11_4;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView_accelerometer;
    SensorManager sensorManager_accelerometer;
    SensorEventListener listener_accelerometer;
    TextView textView_light;
    SensorManager sensorManager_light;
    SensorEventListener listener_light;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_accelerometer = findViewById(R.id.textView1);
        textView_light = findViewById(R.id.textView2);
        sensorManager_accelerometer = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager_light = (SensorManager)getSystemService(SENSOR_SERVICE);

        listener_light = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                String str = "";
                str += "Name：" + event.sensor.getName() + "\n";
                str += "Type：" + event.sensor.getType() + "\n";
                str += "Power：" + event.sensor.getPower() + "\n";
                str += "values：" + event.values[0] + "\n";
                textView_light.setText(str);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        listener_accelerometer = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                String str = "";
                str += "Name：" + event.sensor.getName() + "\n";
                str += "Type：" + event.sensor.getType() + "\n";
                str += "Power：" + event.sensor.getPower() + "\n";
                str += "\tX：" + event.values[0] + "\n";
                str += "\tY：" + event.values[1] + "\n";
                str += "\tZ：" + event.values[2] + "\n";
                textView_accelerometer.setText(str);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager_light.registerListener(listener_accelerometer,sensorManager_accelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sensorManager_light.SENSOR_DELAY_UI);
        sensorManager_light.registerListener(listener_light,sensorManager_light.getDefaultSensor(Sensor.TYPE_LIGHT),sensorManager_light.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager_light.unregisterListener(listener_accelerometer);
        sensorManager_light.unregisterListener(listener_light);
    }
}
