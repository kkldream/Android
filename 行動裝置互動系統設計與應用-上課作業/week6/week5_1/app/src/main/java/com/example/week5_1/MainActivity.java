package com.example.week5_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button bt1,bt2;
    TextView tv1;
    String[] items={"items1","items2","items3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1=findViewById(R.id.button1);
        bt2=findViewById(R.id.button2);
        tv1=findViewById(R.id.textView1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            tv1.setText("");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("AlertDialog")
                    //.setMessage("AlertDialog範例")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,items[which],Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setIcon(R.drawable.ic_launcher_background)
                    .setCancelable(false)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv1.setText("按下\"是\"按鈕");
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv1.setText("按下\"否\"按鈕");
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv1.setText("按下\"取消\"按鈕");
                        }
                    }).show();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("");
                MyAlertDialog myAlertDialog = new MyAlertDialog(MainActivity.this);
                myAlertDialog.setTitle("自建AlertDialog");
                myAlertDialog.setMessage("自建myAlertDialog範例");
                myAlertDialog.setIcon(R.drawable.ic_launcher_foreground);
                myAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv1.setText("按下\"是\"按鈕");
                    }
                });
                myAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv1.setText("按下\"否\"按鈕");
                    }
                });
                myAlertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv1.setText("按下\"取消\"按鈕");
                    }
                });
                myAlertDialog.show();
            }
        });
    }
}
