package com.example.week6_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Main3Activity extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        wv = findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("http://www.google.com.tw/");
    }
}
