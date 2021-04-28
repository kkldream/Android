package com.example.t3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    WebView webView1, webView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView1 = findViewById(R.id.webView1);
        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl("https://cctvn.freeway.gov.tw/abs2mjpg/bmjpg?camera=10000&0.2024242315879914");
        webView2 = findViewById(R.id.webView2);
        webView2.setWebViewClient(new WebViewClient());
        webView2.loadUrl("https://cctvn.freeway.gov.tw/abs2mjpg/bmjpg?camera=10002&0.3713650846067087");
    }
}