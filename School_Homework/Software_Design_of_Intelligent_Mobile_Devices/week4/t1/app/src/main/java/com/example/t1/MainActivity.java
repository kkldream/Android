package com.example.t1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String USER = "b0742033";
    private static final String PASSWD = "123456";
    EditText editText1, editText2;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.editTextTextPersonName1);
        editText2 = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText1.getText().toString().equals(USER)) {
                    Toast.makeText(MainActivity.this, "USERNAME ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editText2.getText().toString().equals(PASSWD)) {
                    Toast.makeText(MainActivity.this, "PASSWORD ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            }
        });
    }
}