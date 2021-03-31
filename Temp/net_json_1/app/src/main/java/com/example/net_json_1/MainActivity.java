package com.example.net_json_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button1,button2;
    TextView textView1;
    ListView listView;
    String gps_address = "";
    Handler handler = new Handler();
    String url = "http://192.168.0.11/getdata.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView1 = findViewById(R.id.textView1);
        listView = findViewById(R.id.listView);


        setListView(new String[][]{{"NULL"}});

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray array = new JSONArray(HtmlService.getHtml(url));
                            final String[][] listView_arr = new String[array.length()][4];
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                listView_arr[array.length() - i - 1][0] = jsonObject.getString("time");;
                                listView_arr[array.length() - i - 1][1] = jsonObject.getString("status");;
                                listView_arr[array.length() - i - 1][2] = jsonObject.getString("trash");;
                                listView_arr[array.length() - i - 1][3] = jsonObject.getString("gps");;
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setListView(listView_arr);
                                    button2.setEnabled(false);
                                    textView1.setText("Time：\nStatus：\nTrash：\nGPS：");
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String str = "Time：" + listView_arr[position][0]
                                            + "\nStatus：" + listView_arr[position][1]
                                            + "\nTrash：" + listView_arr[position][2]
                                            + "\nGPS：" + listView_arr[position][3];
                                    gps_address = listView_arr[position][3];
                                    button2.setEnabled(true);
                                    textView1.setText(str);
//                                    Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + gps_address));
                startActivity(maps);
            }
        });
    }

    void setListView(final String[][] listView_arr) {
        String[] _listView_arr = new String[listView_arr.length];
        for(int a = 0; a < listView_arr.length; a++)  _listView_arr[a] = listView_arr[a][0];
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,_listView_arr);
        listView.setAdapter(arrayAdapter);

    }

}