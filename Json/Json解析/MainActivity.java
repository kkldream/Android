package com.example.json_test_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    String jsonArr = "[{a:1,b:2}]";
//    String jsonObj = "{a:1,b:2}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonArr);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    JSONObject jsonObject = new JSONObject(jsonObj);
                    textView.setText(jsonObject.getString("a"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}