package com.test.calculator;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MyEditText textField;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        CalcKeyboard ck = new CalcKeyboard(getBaseContext());
        ck.addCallback(key -> {
                if (!key.equals("result") && !key.equals("clear")) {
                    textField.pressKey(key);
                }
                if (key.equals("result") && textField.getText()!=null) {
                    String result = CheckAndResult.getResult(textField.getText().toString());
                    resultText.setText(getResources().getString(R.string.result,result));
                }
                if (key.equals("clear")) {
                    clear();
                }
        });

        linearLayout.addView(ck);
        textField = findViewById(R.id.digitsField);
        resultText = findViewById(R.id.resultText);
    }

    public void clear(){
        textField.setText(getResources().getString(R.string.clear));
        resultText.setText("");
    }

}