package com.example.week5_hm_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button bt;
    TextView tv;
    String[] menu=new String[]{"大麥克","麥香雞","牛排","烏龍麵"};
    boolean[] menubool=new boolean[]{false,false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=findViewById(R.id.button);
        tv=findViewById(R.id.textview);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMultiChoiceItems(menu, menubool, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                menubool[which]=isChecked;
                            }
                        })
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str="你點了：";
                                for(int a=0;a<4;a++)
                                    if(menubool[a])
                                        str+=menu[a]+" ";
                                tv.setText(str);
                            }
                        }).show();
            }
        });
    }
}
