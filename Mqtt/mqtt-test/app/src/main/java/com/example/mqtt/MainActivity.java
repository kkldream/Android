package com.example.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
    EditText editText_ip, editText_subTopic, editText_pubTopic, editText_pubMsg, editText_clientID;
    Button button_connect, button_publish;
    TextView textView;
    Handler handler = new Handler();
    MqttClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewId();
        Log.i("TAG","-------------------------STARA-------------------------");

        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_connect.getText().toString().equals("connect")) {
                    try {
                        String brokerUri = editText_ip.getText().toString();
                        String clientID = editText_clientID.getText().toString();
                        String subTopic = editText_subTopic.getText().toString();
                        client = connectBroker(brokerUri,clientID);
                        subscribeTopic(client,subTopic);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        button_connect.setText("connect");
                        client.disconnect();
                        textView.setText("段開連線" + "\n" + textView.getText().toString());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        button_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String topic = editText_pubTopic.getText().toString();
                    String msg = editText_pubMsg.getText().toString();
                    final String str = "[發送" + topic + "]" + msg;
                    textView.setText(str + "\n" + textView.getText().toString());
                    publishMessage(client,topic,msg,2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findViewId() {
        editText_ip = findViewById(R.id.editText_ip);
        editText_subTopic = findViewById(R.id.editText_subTopic);
        editText_pubTopic = findViewById(R.id.editText_pubTopic);
        editText_pubMsg = findViewById(R.id.editText_pubMsg);
        editText_clientID = findViewById(R.id.editText_clientID);
        button_connect = findViewById(R.id.button_connect);
        button_publish = findViewById(R.id.button_publish);
        textView = findViewById(R.id.textView);
    }

    private MqttCallbackExtended mqttCallbackExtended = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {//連線成功
            final String str = "連線成功";
            Log.d("TAG", "connectComplete: " + serverURI);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    button_connect.setText("disconnect");
                    textView.setText(str + "\n" + textView.getText().toString());
                }
            });
        }
        @Override
        public void connectionLost(Throwable cause) {//連線中斷
            final String str = "連線中斷";
            Log.e("TAG", "連線中斷");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(str + "\n" + textView.getText().toString());
                }
            });
        }
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {//訊息到達
            final String str = "[接收" + topic + "]" + message.toString();
            Log.d("TAG", str);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText(str + "\n" + textView.getText().toString());
                }
            });
        }
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {//接收完成
//            final String str = "接收完成";
//            Log.d("TAG", str);
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    textView.setText(str + "\n" + textView.getText().toString());
//                }
//            });
        }
    };
    private MqttClient connectBroker(String brokerUri, String clientId) throws MqttException {
        MqttClient mqttClient = new MqttClient(brokerUri,clientId,new MemoryPersistence());
        mqttClient.setCallback(mqttCallbackExtended);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setUserName(null);
        options.setPassword(null);
        options.setConnectionTimeout(30);  //超时时间
        options.setKeepAliveInterval(60); //心跳时间,单位秒
        options.setAutomaticReconnect(true);//自动重连
        mqttClient.connect(options);
        return mqttClient;
    }
    private void subscribeTopic(MqttClient mqttClient, String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }
    private void publishMessage(MqttClient mqttClient, String topic, String msg, int qos) throws MqttException {
        MqttMessage message = new MqttMessage(msg.getBytes());
        message.setQos(qos);
        message.setRetained(false);
        mqttClient.publish(topic,message);
//        MqttClient mqttClient = new MqttClient(brokerUri,clientId + "_publish",new MemoryPersistence());
//        mqttClient.connect();
//        MqttMessage message = new MqttMessage(msg.getBytes());
//        message.setQos(qos);
//        message.setRetained(false);
//        mqttClient.publish(topic,message);
//        mqttClient.disconnect();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (client != null){
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}