package com.example.week12_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView textView1;
    Button button1,button2,button3;
    Timer timer = new Timer();
    int sec = 0;
    boolean btn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        timer.schedule(task, 0, 1000);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn = true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn = false;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn = false;
                sec = 0;
                textView1.setText("00:00");
            }
        });
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            if (btn) {
                message.what = 1;
                handler.sendMessage(message);
            }
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                sec++;
                String m = (sec/60<10)?"0"+(sec/60):""+(sec/60);
                String s = (sec%60<10)?"0"+(sec%60):""+(sec%60);
                textView1.setText(m + ":" + s);
            }
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        task.cancel();
    }
}
