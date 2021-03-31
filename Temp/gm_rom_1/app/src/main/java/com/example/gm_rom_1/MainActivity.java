package com.example.gm_rom_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView1;
    final String FILE_NAME = "/lxp.bin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        Button write = findViewById(R.id.button1);
        Button read = findViewById(R.id.button2);
        final EditText edit1 = findViewById(R.id.editText1);
        final EditText edit2 = findViewById(R.id.editText2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        textView1.setText("");
        textView1.setText(textView1.getText().toString() + "\ngetExternalStorageDirectory：" + Environment.getExternalStorageDirectory());
        textView1.setText(textView1.getText().toString() + "\ngetDataDirectory：" + Environment.getDataDirectory());
        textView1.setText(textView1.getText().toString() + "\ngetDownloadCacheDirectory：" + Environment.getDownloadCacheDirectory());
        write.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                write(edit1.getText().toString().trim());
                edit1.setText("");
            }
        });
        read.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edit2.setText(read());
            }
        });
    }
    private void write(String content){
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //獲取SD卡的目錄
                File sdCardDir = Environment.getExternalStorageDirectory();
                File targetFile = new File(sdCardDir.getCanonicalPath() + FILE_NAME);
                //以指定檔案建立RandomAccessFile物件
                RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
                //將檔案記錄指標移動到最後
                raf.seek(targetFile.length());
                //輸出檔案內容
                raf.write(content.getBytes());
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String read(){
        try {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //獲得SD卡對應的儲存目錄
                File sdCardDir = Environment.getExternalStorageDirectory();
                //獲取指定檔案對應的輸入流
                FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + FILE_NAME);
                //將指定輸入流包裝成BufferReader
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                StringBuilder sb = new StringBuilder("");
                String line = null;
                //迴圈讀取檔案內容
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
                br.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
