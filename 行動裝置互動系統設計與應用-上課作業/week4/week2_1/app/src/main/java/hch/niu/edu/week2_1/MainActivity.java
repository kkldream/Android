package hch.niu.edu.week2_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText mEditTextSex;
    EditText mEditTextAge;
    Button   mButton;
    TextView mTextView; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button4);
        mEditTextAge = findViewById(R.id.editText2);
        mEditTextSex = findViewById(R.id.editText);
        mTextView = findViewById(R.id.textView4);  //

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String sex = mEditTextSex.getText().toString();
               int age = Integer.parseInt(mEditTextAge.getText().toString());
               mTextView.setText(sex + age + "歲");

            }
        });





    }
}
