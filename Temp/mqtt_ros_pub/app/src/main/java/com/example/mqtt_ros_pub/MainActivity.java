package com.example.mqtt_ros_pub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView joystick, joystick_background;
    TextView textView;
    MqttClient mqttClient;
    WebView webView;
    int joystick_top, joystick_left, joystick_x_var, joystick_y_var;
    boolean connect_status = false;
    String subscribe_topic = "arm/phone_display";
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
//        webView.loadUrl("http://192.168.0.22:8080/?action=stream");
        webView.loadUrl("http://192.168.0.22:5000/stream");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.onResume();

        joystick = findViewById(R.id.joystick);
        joystick_background = findViewById(R.id.joystick_background);
        textView = findViewById(R.id.textView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int vWidth = metrics.widthPixels; //螢幕橫向解析度
        final int vHeight = metrics.heightPixels; //螢幕直向解析度
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                joystick_top = joystick.getTop();
                joystick_left = joystick.getLeft();
                Log.i("joystick","joystick_top："+ joystick_top+"\njoystick_left："+joystick_left);
                connect_status = true;
            }
        },1000);
        mqttClient = connectBroker("tcp://192.168.0.10:1883","Phone", subscribe_topic);
        joystick.setOnTouchListener(new View.OnTouchListener() {
            private float x, y;    // 原本圖片存在的X,Y軸位置
            private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(connect_status == false) return false;
                //getX()：是獲取當前控件(View)的座標
                //getRawX()：是獲取鼠標相對顯示螢幕左上角的座標
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下圖片時
//                        Log.i("joystick","ACTION_DOWN");
                        x = motionEvent.getX();
                        y = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:// 放開圖片時
//                        Log.i("joystick","ACTION_UP");
                        mx = vWidth / 2 - view.getWidth() / 2;
                        my = joystick_top;
                        joystick_y_var = 0;
                        publishMessage(mqttClient,"arm/mqtt_str","stop," + 0,2);
                        view.layout(mx, my, mx + view.getWidth(), my + view.getHeight());
                        break;
                    case MotionEvent.ACTION_MOVE:// 移動圖片時
//                        Log.i("joystick","ACTION_MOVE");
                        mx = (int) (motionEvent.getRawX() - x);
                        my = (int) (motionEvent.getRawY() - y - view.getHeight());
                        joystick_x_var = (mx - joystick_left) * -1;
                        joystick_y_var = (my - joystick_top) * -1;
//                        Log.i("joystick","x：" + joystick_x_var + "\ty：" + joystick_y_var);
                        if(Math.abs(joystick_x_var) < joystick_background.getWidth() / 2 && Math.abs(joystick_y_var) < joystick_background.getHeight() / 2) {

//                            Log.i("joystick","" + slider_Var);
                            int gx = joystick.getLeft() - joystick_left;
                            int gy = (joystick.getTop() - joystick_top) * -1;
                            int r = (int) (joystick_background.getWidth() * 0.05);
//                            Log.i("joystick","gx：" + gx + "\tgy：" + gy);
                            if (Math.abs(gx) < r && Math.abs(gy) < r) {
                                view.layout(mx, my, mx + view.getWidth(), my + view.getHeight());
                                Log.d("joystick","stop");
                                publishMessage(mqttClient,"arm/mqtt_str","stop," + 0,2);
                            }
                            else if (Math.abs(gy) > Math.abs(gx)) {
                                view.layout(joystick_left, my, joystick_left + view.getWidth(), my + view.getHeight());
                                Log.d("joystick","up：" + gy);
                                publishMessage(mqttClient,"arm/mqtt_str","up," + gy,2);
                            }
                            else {
                                view.layout(mx, joystick_top, mx + view.getWidth(), joystick_top + view.getHeight());
                                Log.d("joystick","right：" + gx);
                                publishMessage(mqttClient,"arm/mqtt_str","right," + gx,2);
                            }
                        }
                        break;
                }
                return true;
            }
        });
        joystick_background.setOnTouchListener(new View.OnTouchListener() {
            int mx, my;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //getX()：是獲取當前控件(View)的座標
                //getRawX()：是獲取鼠標相對顯示螢幕左上角的座標
                if(connect_status == false) return false;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下圖片時
//                        Log.i("joystick","joystick_background：ACTION_DOWN");
                        mx = (int) (motionEvent.getRawX() - joystick.getWidth() / 2);
                        my = (int) (motionEvent.getRawY() - joystick.getHeight() * 1.5);
//                        joystick.layout(mx, my, mx + joystick.getWidth(), my + joystick.getHeight());
                        joystick_x_var = (mx - joystick_left) * -1;
                        joystick_y_var = (my - joystick_top) * -1;
//                        Log.i("joystick","x：" + joystick_x_var + "\ty：" + joystick_y_var);
                        if(Math.abs(joystick_x_var) < view.getWidth() / 2 && Math.abs(joystick_y_var) < view.getHeight() / 2) {
//                            Log.i("joystick","" + slider_Var);
                            int gx = mx - joystick_left;
                            int gy = (my - joystick_top) * -1;
//                            Log.i("joystick","gx：" + gx + "\tgy：" + gy);
                            if (Math.abs(gy) > Math.abs(gx)) {
                                joystick.layout(joystick_left, my, joystick_left + joystick.getWidth(), my + joystick.getHeight());
                                Log.d("joystick","up：" + gy);
                                publishMessage(mqttClient,"arm/mqtt_str","up," + gy,2);
                            }
                            else {
                                joystick.layout(mx, joystick_top, mx + joystick.getWidth(), joystick_top + joystick.getHeight());
                                Log.d("joystick","right：" + gx);
                                publishMessage(mqttClient,"arm/mqtt_str","right," + gx,2);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:// 放開圖片時
//                        Log.i("joystick","joystick_background：ACTION_UP");
                        mx = vWidth / 2 - MainActivity.this.joystick.getWidth() / 2;
                        my = joystick_top;
                        joystick_y_var = 0;
                        publishMessage(mqttClient,"arm/mqtt_str","stop," + 0,2);
                        MainActivity.this.joystick.layout(mx, my, mx + MainActivity.this.joystick.getWidth(), my + MainActivity.this.joystick.getHeight());
                        break;
                    case MotionEvent.ACTION_MOVE:// 移動圖片時
//                        Log.i("joystick","joystick_background：ACTION_MOVE");
                        mx = (int) (motionEvent.getRawX() - joystick.getWidth() / 2);
                        my = (int) (motionEvent.getRawY() - joystick.getHeight() * 1.5);
//                        joystick.layout(mx, my, mx + joystick.getWidth(), my + joystick.getHeight());
                        joystick_x_var = (mx - joystick_left) * -1;
                        joystick_y_var = (my - joystick_top) * -1;
//                        Log.i("joystick","x：" + joystick_x_var + "\ty：" + joystick_y_var);
                        if(Math.abs(joystick_x_var) < view.getWidth() / 2 && Math.abs(joystick_y_var) < view.getHeight() / 2) {
//                            Log.i("joystick","" + slider_Var);
                            int gx = joystick.getLeft() - joystick_left;
                            int gy = (joystick.getTop() - joystick_top) * -1;
                            int r = (int) (view.getWidth() * 0.05);
//                            Log.i("joystick","gx：" + gx + "\tgy：" + gy);
                            if (Math.abs(gx) < r && Math.abs(gy) < r) {
                                joystick.layout(mx, my, mx + joystick.getWidth(), my + joystick.getHeight());
                                Log.d("joystick","stop");
                                publishMessage(mqttClient,"arm/mqtt_str","stop," + 0,2);
                            }
                            else if (Math.abs(gy) > Math.abs(gx)) {
                                joystick.layout(joystick_left, my, joystick_left + joystick.getWidth(), my + joystick.getHeight());
                                Log.d("joystick","up：" + gy);
                                publishMessage(mqttClient,"arm/mqtt_str","up," + gy,2);
                            }
                            else {
                                joystick.layout(mx, joystick_top, mx + joystick.getWidth(), joystick_top + joystick.getHeight());
                                Log.d("joystick","right：" + gx);
                                publishMessage(mqttClient,"arm/mqtt_str","right," + gx,2);
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }
    public void button1_onClick(View view) {
        publishMessage(mqttClient,"arm/mqtt_str","button1",2);
    }
    public void button2_onClick(View view) {
        publishMessage(mqttClient,"arm/mqtt_str","button2",2);
    }
    public void button3_onClick(View view) {
        publishMessage(mqttClient,"arm/mqtt_str","button3",2);
    }
    String msg = "";
    private MqttClient connectBroker(String brokerUri, String clientId, String topic) {
        MqttClient mqttClient = null;
        try {
            mqttClient = new MqttClient(brokerUri,clientId,new MemoryPersistence());
            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {//連線成功
                    Log.i("TAG", "connectComplete:" + serverURI);
                }
                @Override
                public void connectionLost(Throwable cause) {
                    Log.i("TAG", "連線中斷");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(subscribe_topic)){
                        if (!("" + message).equals(msg)) {
                            Log.i("messageArrived","" + message);
                            msg = "" + message;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    textView.setText("" + msg);
                                }
                            });
                        }

                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(false);
            options.setUserName(null);
            options.setPassword(null);
            options.setConnectionTimeout(30);  //超时时间
            options.setKeepAliveInterval(60); //心跳时间,单位秒
            options.setAutomaticReconnect(true);//自动重连
            mqttClient.connect(options);
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return mqttClient;
    }
    private void publishMessage(MqttClient mqttClient, String topic, String msg, int qos){
        try {
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            mqttClient.publish(topic,message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
//        MqttClient mqttClient = new MqttClient(brokerUri,clientId + "_publish",new MemoryPersistence());
//        mqttClient.connect();
//        MqttMessage message = new MqttMessage(msg.getBytes());
//        message.setQos(qos);
//        message.setRetained(false);
//        mqttClient.publish(topic,message);
//        mqttClient.disconnect();
    }

}