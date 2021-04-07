package com.example.t1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.google.com.tw/maps/dir/24.7453738,121.7448656/260%E5%AE%9C%E8%98%AD%E7%B8%A3%E5%AE%9C%E8%98%AD%E5%B8%82%E6%B0%91%E6%AC%8A%E8%B7%AF%E4%BA%8C%E6%AE%B538%E5%B7%B76%E8%99%9F%E6%96%B0%E6%9C%88%E5%BB%A3%E5%A0%B4/@24.7497004,121.7438339,16z/data=!3m1!4b1!4m17!1m6!3m5!1s0x0:0x2bd1e2ef01b06991!2z5paw5pyI5buj5aC0!8m2!3d24.7540295!4d121.750906!4m9!1m1!4e1!1m5!1m1!1s0x3467e4c364bd7dd3:0x2bd1e2ef01b06991!2m2!1d121.750906!2d24.7540294!3e0?hl=zh-TW&authuser=0");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }
}