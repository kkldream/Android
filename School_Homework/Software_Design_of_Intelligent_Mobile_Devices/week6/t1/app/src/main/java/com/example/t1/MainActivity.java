package com.example.t1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int FUNC_LOGIN = 87;
    String[] listView_arr = new String[]{"按一下可以編輯備忘", "長按可以清除備忘", "", "", "", "", "", "", "", ""};
    String[] arrayAdapter_listView_arr = new String[listView_arr.length];
    ListView listView;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        for(int a = 0; a < listView_arr.length; a++)
            arrayAdapter_listView_arr[a] = (a + 1) + ". " + listView_arr[a];
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayAdapter_listView_arr);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MainActivity_1.class);
                intent.putExtra("num", String.valueOf(i));
                intent.putExtra("text", listView_arr[i]);
                startActivityForResult(intent, FUNC_LOGIN);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView_arr[i] = "";
                arrayAdapter_listView_arr[i] = (i + 1) + ".";
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FUNC_LOGIN) {
            if (resultCode == RESULT_OK){
                int num = Integer.valueOf(data.getStringExtra("num"));
                String text = data.getStringExtra("text");
                listView_arr[num] = text;
                arrayAdapter_listView_arr[num] = (num + 1) + ". " + text;
                arrayAdapter.notifyDataSetChanged();
            } else {
                finish();
            }

        }
    }
}