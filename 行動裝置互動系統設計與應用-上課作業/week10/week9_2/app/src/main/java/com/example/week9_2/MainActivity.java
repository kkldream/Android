package com.example.week9_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button;
    ListView listView;
    String[] mean = {"A","B","C","D"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,mean);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = "";
                for (int a=0;a<mean.length;a++) {
                    if (listView.getCheckedItemPositions().get(a)) {
                        str1 += mean[a] + ",";
                    }
                }
                Toast.makeText(MainActivity.this,str1,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
