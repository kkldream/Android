package com.example.week7_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    String videoAddress = "http://rfid.niu.edu.tw/web/cch_converted.mp4";
//    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        Uri videoUri = Uri.parse(videoAddress);
        videoView.setVideoURI(videoUri);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this,"LANDSCAPE",Toast.LENGTH_LONG).show();
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this,"PORTRAIT",Toast.LENGTH_LONG).show();
        }

    }
}
