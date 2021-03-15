package com.example.week3_hm_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String str = intent.getStringExtra("S");
        textView = findViewById(R.id.textView9);
        textView.setText(str);
    }
    public void bt_bk(View view){
        super.onBackPressed();
    }
}
