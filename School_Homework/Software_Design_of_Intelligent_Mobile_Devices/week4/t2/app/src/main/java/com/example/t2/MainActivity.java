package com.example.t2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    Button button1, button2;
    EditText editText1, editText2;
    TextView textView;
    LinearLayout linearLayout;
    int mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radiogutton);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        editText1 = findViewById(R.id.editTextNumber1);
        editText2 = findViewById(R.id.editTextNumber2);
        textView = findViewById(R.id.textView);
        linearLayout = findViewById(R.id.linearLayout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                linearLayout.setVisibility(View.VISIBLE);
                switch (i) {
                    case R.id.radioButton1:
                        button1.setText("+");
                        button2.setText("-");
                        mode = 0;
                        break;
                    case R.id.radioButton2:
                        button1.setText("x");
                        button2.setText("/");
                        mode = 1;
                        break;
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mode) {
                    case 0:
                        textView.setText("= " + (Integer.valueOf(editText1.getText().toString()) + Integer.valueOf(editText2.getText().toString())));
                        break;
                    case 1:
                        textView.setText("= " + (Integer.valueOf(editText1.getText().toString()) * Integer.valueOf(editText2.getText().toString())));
                        break;
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mode) {
                    case 0:
                        textView.setText("= " + (Integer.valueOf(editText1.getText().toString()) - Integer.valueOf(editText2.getText().toString())));
                        break;
                    case 1:
//                        if (editText2.getText().toString().equals("0")) {
//                            textView.setText("ERROR");
//                            break;
//                        }
                        try {
                            textView.setText("= " + (Integer.valueOf(editText1.getText().toString()) / Integer.valueOf(editText2.getText().toString())));
                        } catch (Exception e) {
                            Log.e("TAG", String.valueOf(e));
                            Toast.makeText(MainActivity.this, "division by zero exception", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }
}