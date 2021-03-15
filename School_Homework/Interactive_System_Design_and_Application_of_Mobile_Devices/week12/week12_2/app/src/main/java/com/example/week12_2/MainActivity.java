package com.example.week12_2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    Button button1,button2;
    SeekBar seekBar;
    ImageView imageView;
    int[] img = {
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5,
        R.drawable.img6,
        R.drawable.img7
    };
    int i = 3;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        seekBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(img[i]);
        seekBar.setMax(6);
        seekBar.setProgress(3);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                bitmap = BitmapFactory.decodeResource(getResources(),img[progress]);
//                imageView.setImageBitmap(bitmap);
                i = progress;
                imageView.setImageResource(img[i]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>0){
                    i--;
                    imageView.setImageResource(img[i]);
                    seekBar.setProgress(i);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i<img.length-1){
                    i++;
                    imageView.setImageResource(img[i]);
                    seekBar.setProgress(i);
                }
            }
        });
    }
}
