package com.example.week9_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText1;
    Button button1,button2,button3;
    SharedPreferences msharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.editText1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
    }
    public void button_save(View view) {
        Toast.makeText(this,"button_save",Toast.LENGTH_SHORT).show();
        msharedPreferences = getSharedPreferences("data",0);
        msharedPreferences.edit().putString("S",editText1.getText().toString()).commit();
    }
    public void button_read(View view) {
        Toast.makeText(this,"button_read",Toast.LENGTH_SHORT).show();
        msharedPreferences = getSharedPreferences("data",0);
        editText1.setText(msharedPreferences.getString("S",""));
    }
    public void button_close(View view) {
        Toast.makeText(this,"button_close",Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
