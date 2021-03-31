package com.example.tcp_client_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity_tcp_2 extends AppCompatActivity {
    EditText editText;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tcp_2);
        editText = findViewById(R.id.editText);
    }
    public void bt_connect(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Button bt = findViewById(R.id.button);
                    bt.setEnabled(false);
                    socket = new Socket("192.168.0.10", 50);

                    Thread.sleep(3 * 1000);//執行緒睡眠3秒
                    while (true) {
                        socket.sendUrgentData(0xFF); // 傳送心跳包
                        Toast.makeText(MainActivity_tcp_2.this,"目前處於連結狀態！",Toast.LENGTH_SHORT);
                        Thread.sleep(3 * 1000);//執行緒睡眠3秒
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void bt_close(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    socket.shutdownOutput();//关闭输出流
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void bt_send(View view){
        new Thread() {
            @Override
            public void run(){
                try {
                    OutputStream outputStream = socket.getOutputStream();//字节输出流
                    outputStream.write((editText.getText().toString() + '\n').getBytes("utf-8"));
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
