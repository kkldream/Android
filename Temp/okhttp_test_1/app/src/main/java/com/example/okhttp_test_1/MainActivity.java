package com.example.okhttp_test_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "http_post_test";
    static final String API_URL = "http://120.101.8.240:5000/api";
    Handler handler;
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormBody formBody = new FormBody.Builder()
                        .add("action", "debug")
                        .add("key", "show_status")
                        .add("num", "10")
                        .build();
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder().url(API_URL).post(formBody).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        final String error_str = e.getMessage();
                        Log.i(TAG, "Post Error：" + error_str);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(error_str);
                            }
                        });
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        final String response_str = response.body().string();
                        Log.i(TAG, "Post Response：" + response_str);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(response_str);
                            }
                        });
                    }
                });
            }
        });
    }
}