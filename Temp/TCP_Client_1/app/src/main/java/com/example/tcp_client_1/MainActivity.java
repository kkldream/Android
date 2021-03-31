package com.example.tcp_client_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bt_connect_1(View view) {
        Intent intent = new Intent(this, MainActivity_tcp_1.class);
        startActivity(intent);
//        echo("Connect 1");
    }

    public void bt_connect_2(View view) {
        Intent intent = new Intent(this, MainActivity_tcp_2.class);
        startActivity(intent);
//        echo("Connect 2");
    }

    public void echo(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}