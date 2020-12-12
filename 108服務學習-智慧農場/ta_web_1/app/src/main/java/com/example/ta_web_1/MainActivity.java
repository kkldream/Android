package com.example.ta_web_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static final String TEST_IP = "192.168.0.20";
    WebView webView;
    TextView textView;
    Button button;
    EditText editText;
    String WebURL;
    Boolean thread_end = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        webView = findViewById(R.id.webView);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().toString().equals("重設")) {
                    textView.setText("[中斷連線]\n\n" + textView.getText().toString());
                    thread_end = true;
                    webView.onPause();
                    button.setText("連接");
                } else {
                    if (editText.getText().toString().equals("")) {
                        editText.setText(TEST_IP);
                    }
                    WebURL = "http://" + editText.getText().toString() + ":8000";
                    webView.loadUrl(WebURL + "/stream.mjpg");
                    webView.setWebViewClient(new WebViewClient());
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setUseWideViewPort(true);
                    webSettings.setLoadWithOverviewMode(true);
                    webView.onResume();
                    thread_end = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (thread_end == false) {
                                String urlData = "";
                                String decodedString;
                                try {
                                    HttpURLConnection hc;
                                    URL url = new URL(WebURL + "/data");
                                    hc = (HttpURLConnection) url.openConnection();
                                    hc.connect();
                                    BufferedReader in = new BufferedReader(new InputStreamReader(hc.getInputStream()));
                                    while ((decodedString = in.readLine()) != null) {
                                        urlData += decodedString;
                                    }
                                    in.close();
                                    String[] data = urlData.split(",");
                                    for(int a=0;a<3;a++)
                                        if(data[a].equals("-1"))
                                            data[a] = "?";
                                    textView.setText("溫度：" + data[0] + "度\n\n濕度：" + data[1] + "%\n\n土壤濕度：" + data[2] + "%");
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    Log.e("test", e.toString());
                                }
                            }
                        }
                    }).start();
                    Log.i("test","thread.start");
                    button.setText("重設");
                }

            }
        });
    }
}
