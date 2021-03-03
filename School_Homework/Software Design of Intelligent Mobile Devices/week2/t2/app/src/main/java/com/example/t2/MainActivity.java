package com.example.t2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView textView1, textView2, textView3;
    LinearLayout colorBlock;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        colorBlock = findViewById(R.id.colorBlock);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int red = random.nextInt(256);
                textView1.setText("紅：" + red);
                textView1.setTextColor(Color.rgb(red, 0, 0));
                int green = random.nextInt(256);
                textView2.setText("綠：" + green);
                textView2.setTextColor(Color.rgb(0, green, 0));
                int blue = random.nextInt(256);
                textView3.setText("藍：" + blue);
                textView3.setTextColor(Color.rgb(0, 0, blue));
                colorBlock.setBackgroundColor(Color.rgb(red, green, blue));

            }
        });
    }
}