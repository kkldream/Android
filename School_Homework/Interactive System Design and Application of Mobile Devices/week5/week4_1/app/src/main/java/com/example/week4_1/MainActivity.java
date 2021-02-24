package com.example.week4_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et1;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("HCH", "onCreate()");
        Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();

        et1 = findViewById(R.id.editText);
        tv1 = findViewById(R.id.textView);
    }
    public void bt1(View view) {
        String et1_str = et1.getText().toString();
//        tv1.setText("I am " + et1_str);
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("S",et1_str);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("HCH", "onStart()");
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("HCH", "onStop()");
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("HCH", "onDestroy()");
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("HCH", "onPause()");
        Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("HCH", "onResume()");
        Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("HCH", "onRestart()");
        Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
    }

}