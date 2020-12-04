package com.example.week8_hm;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    String[] spinner_list = {"請選擇","國棟鬧鐘1","國棟鬧鐘2"};
    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        videoView = findViewById(R.id.videoView);
        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,spinner_list);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Uri uri;
                switch (position) {
                    case 1:
                        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
                        break;
                    case 2:
                        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video2);
                        break;
                    default:
                        videoView.stopPlayback();
                        videoView.setEnabled(false);
                        return;
                }
                videoView.setEnabled(true);
                String str = "你選取了「" + spinner_list[position] + "」";
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                videoView.setVideoURI(uri);
                videoView.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
