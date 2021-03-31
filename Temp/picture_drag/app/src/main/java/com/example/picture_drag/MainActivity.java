package com.example.picture_drag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int vWidth = metrics.widthPixels; //螢幕橫向解析度
        final int vHeight = metrics.heightPixels; //螢幕直向解析度
        slider = findViewById(R.id.slider);
        slider.setOnTouchListener(new View.OnTouchListener() {
            private float x, y;    // 原本圖片存在的X,Y軸位置
            private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //getX()：是獲取當前控件(View)的座標
                //getRawX()：是獲取相對顯示螢幕左上角的座標
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下圖片時
                        Log.i("TAG","ACTION_DOWN");
                        y = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:// 放開圖片時
                        Log.i("TAG","ACTION_UP");
                        mx = vWidth/2-view.getWidth()/2;
                        my = vHeight/2-view.getHeight()/2;
                        view.layout(mx, my, mx + view.getWidth(), my + view.getHeight());
                        break;
                    case MotionEvent.ACTION_MOVE:// 移動圖片時
                        Log.i("TAG","ACTION_MOVE");
                        my = (int) (motionEvent.getRawY() - y - view.getHeight() / 2);
                        mx = vWidth/2-view.getWidth()/2;
                        view.layout(mx, my, mx + view.getWidth(), my + view.getHeight());
                        break;
                }
                return true;
            }
        });
    }
}