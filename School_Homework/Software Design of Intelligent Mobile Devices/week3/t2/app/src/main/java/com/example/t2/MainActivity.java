package com.example.t2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    ImageView imageView;
    Button button;
    Bitmap baseBitmap;
    Canvas canvas;
    Paint paint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                canvasInit();
                imageView.setOnTouchListener(onTouchListener);
                button.setOnClickListener(onClickListener);
            }
        },100);
    }

    void canvasInit() {
        baseBitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.WHITE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (baseBitmap != null) {
                canvasInit();
                imageView.setImageBitmap(baseBitmap);
            }
        }
    };

    float startX;
    float startY;
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float stopX = motionEvent.getX();
                    float stopY = motionEvent.getY();
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    imageView.setImageBitmap(baseBitmap);
                    break;
            }
            return true;
        }
    };
}
