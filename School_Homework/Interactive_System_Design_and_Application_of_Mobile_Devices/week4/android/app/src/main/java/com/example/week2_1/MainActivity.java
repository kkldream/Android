package com.example.week2_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mEditTestSex;
    EditText mEditTestAge;
    Button mButton;
    TextView mTextView;
    String sexStr = "";
    Spinner mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button2);
        mEditTestSex = findViewById(R.id.editText);
        mEditTestAge = findViewById(R.id.editText2);
        mTextView = findViewById(R.id.textView5);
        mSpinner = findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sexStr = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sexStr = mEditTestSex.getText().toString();
                int ageInt = Integer.parseInt(mEditTestAge.getText().toString());

                String strSug = "";
                if(sexStr.equals("M")){
                    if(ageInt<28){
                        strSug += "不急";
                    }
                    else if(ageInt>33){
                        strSug += "趕快結婚";
                    }
                    else {
                        strSug += "開始找對象";
                    }
                }
                else{
                    if(ageInt<25){
                        strSug += "不急";
                    }
                    else if(ageInt>30){
                        strSug += "趕快結婚";
                    }
                    else {
                        strSug += "開始找對象";
                    }
                }
                mTextView.setText(strSug);
            }
        });
    }
}
