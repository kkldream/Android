package com.example.socket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView textView1;
    EditText editText1;
    Button button1, button2;
    SocketTCP socket;
    ToggleButton toggleButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton1 = findViewById(R.id.toggleButton);
        textView1 = findViewById(R.id.textView);
        editText1 = findViewById(R.id.editTextTextPersonName);
        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button1.setOnClickListener(view -> {
            socket.send(editText1.getText().toString());
        });
        toggleButton1.setOnCheckedChangeListener((view, b) -> {
            if (b) {
                socket = new SocketTCP("192.168.1.22",8217);
                socket.setOnListenReceive(new Function() {
                    @Override
                    public Object apply(Object o) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText((String) o);
                            }
                        });
                        return null;
                    }
                });
            } else {
                socket.close();
            }
        });
    }
}