package com.example.week11_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText1,editText2;
    Button button1,button2,button3;
    ListView listView;
    SQLitedb sqLitedb = new SQLitedb(this);
    int curosr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);
        sqLitedb.curosr();

        listViewUpdate();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = editText1.getText().toString();
                String str2 = editText2.getText().toString();
                sqLitedb.insert(str1 + "\n" + str2);
                listViewUpdate();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLitedb.delete(curosr);
                listViewUpdate();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = editText1.getText().toString();
                String str2 = editText2.getText().toString();
                sqLitedb.update(curosr,str1 + "\n" + str2);
                listViewUpdate();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sqLitedb.cursor.moveToPosition(position);
                curosr = sqLitedb.cursor.getInt(0);
                String[] strArr = sqLitedb.cursor.getString(1).split("\n");
                editText1.setText(strArr[0]);
                editText2.setText(strArr[1]);
            }
        });
    }
    private void listViewUpdate() {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,sqLitedb.select());
        listView.setAdapter(listAdapter);
        editText1.setText("");
        editText2.setText("");
    }
}
