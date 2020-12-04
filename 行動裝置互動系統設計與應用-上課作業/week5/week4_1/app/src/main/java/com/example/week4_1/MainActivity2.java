package com.example.week4_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tv2 = findViewById(R.id.textView2);

        Intent intent2 = getIntent();
        String s2 = intent2.getStringExtra("S");
        tv2.setText(s2);
    }

    public void back(View view) {
        super.onBackPressed();
//        Intent intent2 = new Intent(MainActivity2.this, MainActivity.class);
//        startActivity(intent2);
    }
}