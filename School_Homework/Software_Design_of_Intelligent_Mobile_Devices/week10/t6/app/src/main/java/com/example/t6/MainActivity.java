package com.example.t6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    TextView textView1, textView2;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        textView1.setText("選購了：");
        if (checkBox1.isChecked()) textView1.setText(textView1.getText().toString() + checkBox1.getText().toString() + " ");
        if (checkBox2.isChecked()) textView1.setText(textView1.getText().toString() + checkBox2.getText().toString() + " ");
        if (checkBox3.isChecked()) textView1.setText(textView1.getText().toString() + checkBox3.getText().toString() + " ");
        if (checkBox4.isChecked()) textView1.setText(textView1.getText().toString() + checkBox4.getText().toString() + " ");
        int var = 0;
        if (checkBox1.isChecked()) var += 10;
        if (checkBox2.isChecked()) var += 20;
        if (checkBox3.isChecked()) var += 30;
        if (checkBox4.isChecked()) var += 40;
        textView2.setText("總額：" + var + "元");
    }
}