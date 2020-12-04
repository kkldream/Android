package com.example.week2_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mEditTestSex;
    EditText mEditTestAge;
    Button mButton;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button2);
        mEditTestSex = findViewById(R.id.editText);
        mEditTestAge = findViewById(R.id.editText2);
        mTextView = findViewById(R.id.textView5);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexStr = mEditTestSex.getText().toString();
                int ageInt = Integer.parseInt(mEditTestAge.getText().toString());
                String ansStr = sexStr + ageInt + "歲";
                mTextView.setText(ansStr);
            }
        });
    }
}
