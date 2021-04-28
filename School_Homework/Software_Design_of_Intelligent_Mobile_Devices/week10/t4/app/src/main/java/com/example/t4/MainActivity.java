package com.example.t4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setImageResource(R.drawable.snipaste_1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch ((int)(Math.random()*6)) {
                    case 0:
                        imageButton.setImageResource(R.drawable.snipaste_1);
                        break;
                    case 1:
                        imageButton.setImageResource(R.drawable.snipaste_2);
                        break;
                    case 2:
                        imageButton.setImageResource(R.drawable.snipaste_3);
                        break;
                    case 3:
                        imageButton.setImageResource(R.drawable.snipaste_4);
                        break;
                    case 4:
                        imageButton.setImageResource(R.drawable.snipaste_5);
                        break;
                    case 5:
                        imageButton.setImageResource(R.drawable.snipaste_6);
                        break;
                }
            }
        });
    }
}