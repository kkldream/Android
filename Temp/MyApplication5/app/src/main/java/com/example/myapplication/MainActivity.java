package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myapplication.header.LocationMove;
import com.example.myapplication.header.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private static final String TAG = "TAG";
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    ImageView imageView;
    Button button1, button2;
    TextView textView1;
    ToggleButton toggleButton1;
    Handler handler = new Handler();
    Bitmap baseBitmap;
    Canvas canvas;
    Paint paint;
    Double[][] map_range = {{24.741500, 121.741281}, {24.750720, 121.750946}};
    double distance = 0;
    LocationMove locationMove;
    String imagePath = "/storage/emulated/0/myapplication/map.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"------------------------------------------------------");
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        textView1 = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        toggleButton1 = findViewById(R.id.toggleButton);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        locationMove = new LocationMove(getSystemService(Context.LOCATION_SERVICE));
        baseBitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(baseBitmap);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG,"baseBitmap: " + baseBitmap.getWidth() + "x" + baseBitmap.getHeight());
                Log.d(TAG,"imageView: " + imageView.getWidth() + "x" + imageView.getHeight());
                baseBitmap = Bitmap.createScaledBitmap(baseBitmap, imageView.getWidth(), imageView.getHeight(), false);
                canvas = new Canvas(baseBitmap);
//                canvas.drawColor(Color.WHITE);
                paint = new Paint();
                paint.setStrokeWidth(5);
                paint.setColor(Color.RED);
            }
        },1000);
        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    locationMove.start(new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull final Location location) {
                            Log.v(TAG,"Longitude = " + location.getLongitude() + ", Latitude = " + location.getLatitude());
                            double move_distance = locationMove.getMoveDistance(location.getLongitude(), location.getLatitude());
                            Log.d(Tag.Location, String.valueOf(move_distance));
                            distance += move_distance;
                            double x_pos = (location.getLongitude() - map_range[0][1]) / (map_range[1][1] - map_range[0][1]);
                            double y_pos = (location.getLatitude() - map_range[0][0]) / (map_range[1][0] - map_range[0][0]);
//                            Log.v(TAG,"x_pos = " + x_pos + ", y_pos = " + y_pos);
                            float x_ptr = (float)(baseBitmap.getHeight() * x_pos);
                            float y_ptr = (float)(baseBitmap.getWidth() * (1 - y_pos));
//                            Log.v(TAG,"x_ptr = " + x_ptr + ", y_ptr = " + y_ptr);
                            canvas.drawCircle(x_ptr, y_ptr, 10, paint);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView1.setText(String.valueOf(distance));
                                    imageView.setImageBitmap(baseBitmap);
                                }
                            });
                        }
                    });
                } else {
                    locationMove.stop();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distance = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView1.setText(String.valueOf(distance));
                    }
                });
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                baseBitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false);
                Log.d(TAG,baseBitmap.getWidth() + "x" + baseBitmap.getHeight());
                canvas = new Canvas(baseBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(baseBitmap);
                    }
                });
            }
        });
    }

    public void saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "myapplication");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(getFilesDir(), fileName);
        File file = new File(appDir, fileName);
        Log.i(TAG,file.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        }
    }
}