package com.example.web_api_post_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://127.0.0.1/Web-API/t3.php" + "?" +
                    "username=" + "123" +
                    "&password=" + "456");
            HttpResponse response = client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG","" + e);
        }
    }
}