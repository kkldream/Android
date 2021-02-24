package com.example.week6_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    CheckBox cb;
    Button bt;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        tv = findViewById(R.id.textView);
        cb = findViewById(R.id.checkBox);
        bt.setEnabled(false);
        cb.setChecked(false);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked())
                    bt.setEnabled(true);
                else{
                    bt.setEnabled(false);
                    tv.setText(R.string.str1);
                }
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("B0742033 你已同意且接受");
            }
        });
    }
}
