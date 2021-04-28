package com.example.t1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random_r = (int)(Math.random()*255);
                int random_g = (int)(Math.random()*255);
                int random_b = (int)(Math.random()*255);
                button.setBackgroundColor(Color.rgb(random_r,random_g,random_b));
            }
        });
    }
}