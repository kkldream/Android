package com.example.gm_tcp_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Button button_send,button_connect,button_close;
    EditText editText;
    TextView textView;
    ExecutorService mThreadPool;
    Socket socket;
    boolean isConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button_connect = findViewById(R.id.button_connect);
        button_close = findViewById(R.id.button_close);
        button_send = findViewById(R.id.button_send);
        mThreadPool = Executors.newCachedThreadPool();
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("192.168.0.10", 50);
                            Log.i("socket","建立連線");
                            textView.setText("連線中");
                            isConnect = true;
                            while (isConnect) {
                                socket.sendUrgentData(0xFF);//傳送心跳包
                                Log.i("socket","連線中");
                                Thread.sleep(3000);
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                            textView.setText("連線意外中斷");
                            Log.e("socket","連線意外中斷");
                            Log.e("socket",e.toString());
                            isConnect = false;
                        }
                    }
                });
            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isConnect = false;
                            socket.shutdownOutput();//关闭输出流
                            socket.close();
                            Log.i("socket","終止連線");
                            textView.setText("終止連線");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OutputStream outputStream = socket.getOutputStream();//字节输出流
                            outputStream.write((editText.getText().toString() + '\n').getBytes("utf-8"));
                            outputStream.flush();
                            Log.i("socket","送出：" + editText.getText().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
