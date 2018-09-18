package com.test.calculator;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    MyEditText textField;
    TextView resultText;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.rootLayout);
        CalcKeyboard ck = new CalcKeyboard(getBaseContext());
        ck.addCallback(key -> {
            check=true;
                if (!key.equals("result") && !key.equals("clear")) {
                    textField.pressKey(key);
                }
                if (key.equals("result")) {
                    getResult();
                }
                if (key.equals("clear")) {
                    clear();
                }
        });

        linearLayout.addView(ck);

        check=true;
        textField = findViewById(R.id.digitsField);
        resultText = findViewById(R.id.resultText);
    }

    public void clear(){
        textField.setText(getResources().getString(R.string.clear));
        resultText.setText("");
    }

    private void getResult() {
    if(textField.getText()!=null) {
        String inString = textField.getText().toString();
        inString = inString.replace("\u00F7", "/");
        inString = inString.replace("\u00D7", "*");
        String test = inString.replaceAll("[-+*/()]", "");

        if (test.length() > 0) {
            ArrayList<String> outArrayString = PostfixConverter.convertString(inString, (check) -> {
                this.check = check;
                if (!check) {
                    resultText.setText(getResources().getString(R.string.error));
                }
            });

            if (check) {
                double result = Calculate.calculate(outArrayString);
                String sResult = String.format(Locale.getDefault(), "%f", result);
                for (int i = 0; i < 10; i++) {
                    if (sResult.contains(i + "(") || sResult.contains(")" + i)
                            || sResult.contains(").") || sResult.contains(".(")) {
                        textField.setText(getResources().getString(R.string.error));
                        check = false;
                    }
                }
                sResult = sResult.replace(",", ".");
                int dotIndex = sResult.indexOf(".");
                String tempString = sResult.substring(dotIndex, sResult.length());
                int max = tempString.length();
                for (int i = 0; i < max; i++) {
                    if (tempString.length() != 0 && tempString.charAt(tempString.length() - 1) == '0') {
                        tempString = tempString.substring(0, tempString.length() - 1);
                        sResult = sResult.substring(0, sResult.length() - 1);
                    }
                }
                if (sResult.charAt(sResult.length() - 1) == '.') {
                    sResult = sResult.substring(0, sResult.length() - 1);
                }
                resultText.setText(getResources().getString(R.string.result, sResult));
            }
        } else {
            resultText.setText(getResources().getString(R.string.error));
        }
    }
    }
}