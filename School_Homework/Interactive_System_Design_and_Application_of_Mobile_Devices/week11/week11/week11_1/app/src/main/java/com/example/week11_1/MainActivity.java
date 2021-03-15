package com.example.week11_1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button1,button2,button3;
    ListView listView;
    SQLitedb sqLitedb = new SQLitedb(this);
    int curosr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);
        sqLitedb.curosr();

        listViewUpdate();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                Toast.makeText(MainActivity.this,"新增" + str,Toast.LENGTH_SHORT).show();
                sqLitedb.insert(str);
                listViewUpdate();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"刪除",Toast.LENGTH_SHORT).show();
                sqLitedb.delete(curosr);
                listViewUpdate();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                Toast.makeText(MainActivity.this,"修改" + str,Toast.LENGTH_SHORT).show();
                sqLitedb.update(curosr,str);
                listViewUpdate();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sqLitedb.cursor.moveToPosition(position);
                curosr = sqLitedb.cursor.getInt(0);
                editText.setText(sqLitedb.cursor.getString(1));
            }
        });
    }
    private void listViewUpdate() {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,sqLitedb.select());
        listView.setAdapter(listAdapter);
        editText.setText("");
    }
}
