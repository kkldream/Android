package com.niu.native_opencv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.niu.native_opencv.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG_MainActivity";

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_header");
    }
    native String stringFromJNI();
//    native void getTest(Object bitmap, int var);

    private ActivityMainBinding binding;

    Camera camera;
    Bitmap original_bitmap, conversion_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        original_bitmap = Header.imageViewToBitmap(binding.imageView);
        camera = new Camera(this);
        camera.setSurfaceView(binding.surfaceView);
        setListener();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button_camera:
                    camera.open(CameraCharacteristics.LENS_FACING_FRONT);
                    binding.linearLayout1.setVisibility(View.GONE);
                    binding.linearLayout2.setVisibility(View.VISIBLE);
                    binding.imageView.setVisibility(View.INVISIBLE);
                    binding.surfaceView.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_save:
                    String path;
                    if (binding.switchConversion.isChecked()) path = Header.saveBitmapToStorage(conversion_bitmap);
                    else path = Header.saveBitmapToStorage(original_bitmap);
                    Toast.makeText(MainActivity.this,"已將上圖儲存至" + path, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_take_pictures:
                    camera.takePicture(obitmap -> {
                        Bitmap bitmap = (Bitmap) obitmap;
                        int w = bitmap.getWidth();
                        int h = bitmap.getHeight();
                        int c = Math.min(bitmap.getWidth(), bitmap.getHeight());
                        original_bitmap = Bitmap.createBitmap(bitmap,(w-c)/2,(h-c)/2, c, c);
                        binding.imageView.setImageBitmap(original_bitmap);
                        binding.imageView.setVisibility(View.VISIBLE);
                        binding.surfaceView.setVisibility(View.INVISIBLE);
                        camera.close();
                        binding.linearLayout1.setVisibility(View.VISIBLE);
                        binding.linearLayout2.setVisibility(View.GONE);
                        binding.imageView.setVisibility(View.VISIBLE);
                        binding.surfaceView.setVisibility(View.INVISIBLE);
                        return null;
                    });
                    break;
                case R.id.button_flip:
                    camera.flip();
                    break;
                case R.id.button_back:
                    camera.close();
                    binding.linearLayout1.setVisibility(View.VISIBLE);
                    binding.linearLayout2.setVisibility(View.GONE);
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.surfaceView.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };


    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.switch_conversion:
                    if (b) {

                    } else {

                    } break;
                case R.id.toggleButton_light:
                    if (b) camera.setAeMode(Camera.AE_MODE_ON_AUTO_FLASH);
                    else camera.setAeMode(Camera.AE_MODE_OFF);
                    break;
            }
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            switch (seekBar.getId()) {
                case R.id.seekBar_light:
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void setListener() {
        binding.buttonCamera.setOnClickListener(clickListener);
        binding.switchConversion.setOnCheckedChangeListener(checkedChangeListener);
        binding.buttonSave.setOnClickListener(clickListener);
        binding.buttonTakePictures.setOnClickListener(clickListener);
        binding.buttonFlip.setOnClickListener(clickListener);
        binding.buttonBack.setOnClickListener(clickListener);
        binding.toggleButtonLight.setOnCheckedChangeListener(checkedChangeListener);
        binding.seekBarLight.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.seekBar1.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.seekBar3.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.seekBar4.setOnSeekBarChangeListener(seekBarChangeListener);
        binding.seekBar6.setOnSeekBarChangeListener(seekBarChangeListener);
    }
}