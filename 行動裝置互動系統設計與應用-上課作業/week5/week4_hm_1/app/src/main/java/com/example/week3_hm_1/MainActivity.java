package com.example.week3_hm_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText2);
        spinner = findViewById(R.id.spinner);
    }
    public void bt_ok(View view){
        String str = spinner.getSelectedItem().toString() + " " + editText.getText().toString() + "歲";
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("S",str);
        startActivity(intent);
    }

}
