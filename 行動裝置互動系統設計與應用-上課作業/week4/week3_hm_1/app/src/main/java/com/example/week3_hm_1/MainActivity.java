package com.example.week3_hm_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText in_1,in_2;
    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        in_1=findViewById(R.id.editText);
        in_2=findViewById(R.id.editText2);
        t1=findViewById(R.id.textView);
        t2=findViewById(R.id.textView3);
    }

    public void bt_add(View view) {
        int input_1 = Integer.parseInt(in_1.getText().toString());
        int input_2 = Integer.parseInt(in_2.getText().toString());
        t1.setText("+");
        int temp = input_1 + input_2;
        t2.setText(temp);

    }
}
