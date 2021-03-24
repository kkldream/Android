package com.example.t1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Spinner spinner1, spinner2;
    Button button;
    TextView textView;
    final String[] mean1_list = new String[] {"水", "尿", "屎"};
    final String[] mean2_list1 = new String[] {"冰", "溫"};
    final String[] mean2_list2 = new String[] {"冰", "溫", "燙"};
    int[] mode = {0, 0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode[0] = i;
                switch (i) {
                    case 0:
                        spinner2.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mean2_list1));
                        break;
                    case 1:
                    case 2:
                        spinner2.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, mean2_list2));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode[1] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("" + mean1_list[mode[0]] + " + " + mean2_list2[mode[1]]);
            }
        });
    }
}