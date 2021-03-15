package com.example.t1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final static int r = 10;
    float size;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        size = textView.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        Log.i("TAG", "Start：" + size);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = size + r;
                textView.setTextSize(size);
                Log.i("TAG", String.valueOf(size));
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = size - r;
                textView.setTextSize(size);
                Log.i("TAG", String.valueOf(size));
            }
        });
    }

}