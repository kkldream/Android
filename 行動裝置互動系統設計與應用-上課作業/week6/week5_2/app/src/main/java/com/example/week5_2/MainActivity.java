package com.example.week5_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt1;
    TextView tv1;
    Dialog dlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1=findViewById(R.id.button);
        tv1=findViewById(R.id.textView);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("");
                dlg = new Dialog(MainActivity.this);
                dlg.setContentView(R.layout.mydlg);
                dlg.setCancelable(false);
                Button bt11=dlg.findViewById(R.id.button11);
                Button bt12=dlg.findViewById(R.id.button12);
                final EditText tv11=dlg.findViewById(R.id.editText11);
                final EditText tv12=dlg.findViewById(R.id.editText12);
                bt11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String t1=tv11.getText().toString();
                        String t2=tv12.getText().toString();
                        tv1.setText("帳號："+t1+"\n密碼："+t2);
                        dlg.cancel();
                    }
                });
                bt12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv1.setText("取消");
                        dlg.cancel();
                    }
                });
                dlg.show();
            }
        });

    }
}
