package com.example.t2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.root);
        listView = findViewById(R.id.lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Snackbar.make(linearLayout, "AAAAAAAAAAAAAAA", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Snackbar.make(linearLayout, "BBBBBBBBBBBBB", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Snackbar.make(linearLayout, "CCCCCCCCCCCCC", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}