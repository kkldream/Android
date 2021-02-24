package com.example.week6_hm_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    CheckBox checkBox;
    WebView webView;
    Spinner spinner;
    String[] web = {"Google", "Yahoo"};
    String[] web_url = {"https://www.google.com.tw/", "https://tw.yahoo.com/"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        webView = findViewById(R.id.webview);
        checkBox = findViewById(R.id.checkBox);
        webView.setVisibility(View.INVISIBLE);
        spinner.setEnabled(false);
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, web);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(checkBox.isChecked())
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(web_url[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    spinner.setEnabled(true);
                    webView.setVisibility(View.VISIBLE);
                }
                else{
                    spinner.setEnabled(false);
                    webView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
