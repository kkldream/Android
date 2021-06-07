package com.example.android_storage;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }
    public void button1_onClick(View view) {
        Log.i(TAG, "----------------------------------------------------------------------------------------------------");
        Log.i(TAG, "getFilesDir：" + getFilesDir());
        Log.i(TAG, "getNoBackupFilesDir：" + getNoBackupFilesDir());
        Log.i(TAG, "Environment.getExternalStorageDirectory：" + Environment.getExternalStorageDirectory());
        Log.i(TAG, "getExternalFilesDir：" + getExternalFilesDir(null));
    }
    public void button2_onClick(View view) {
        File sdCard = Environment.getExternalStorageDirectory();
        File directory_pictures = new File(sdCard, "Pictures");
        Log.i(TAG,"directory_pictures="+directory_pictures);
        File filePath = getExternalFilesDir("Pictures");
        Log.i(TAG,"filePath="+filePath);
    }
}