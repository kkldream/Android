package com.example.life_1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG","onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("TAG","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TAG","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TAG","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG","onResume");
    }
}
