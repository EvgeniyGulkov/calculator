package com.test.calculator;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
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
            hideKeyboard();
            if(check) {
                if (!key.equals("result") && !key.equals("delete") && !key.equals("clear")) {
                    textField.pressKey(key);
                }
                if (key.equals("result")) {
                    getResult();
                }
                if (key.equals("delete")) {
                    deleteLast();
                }
            }
            if (key.equals("clear")) {
                check=true;
                clear();
            }
        });

        linearLayout.addView(ck);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            Rect r = new Rect();
            linearLayout.getWindowVisibleDisplayFrame(r);
            int screenHeight = linearLayout.getRootView().getHeight();

            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                textField.setCursorVisible(true);
            } else {
                textField.setCursorVisible(false);
            }
        });

        check=true;
        textField = findViewById(R.id.digitsField);
        resultText = findViewById(R.id.resultText);
    }

    private void hideKeyboard() {
        textField.clearFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(textField.getWindowToken(), 0);
    }


    public void deleteLast(){
        if(textField.getText()!=null) {
            String text = textField.getText().toString();
            textField.setText(text.substring(0, text.length() - 1));
        }
    }

    public void clear(){
        textField.setText(getResources().getString(R.string.clear));
        resultText.setText("");
    }

    public boolean checkDots(String s) {
        boolean checkFlag=true;
        for (String res : s.split("-|\\+|\\u00F7|\\u00D7|\\*/")) {
            int countDots=0;
            if(res.contains(".")){
                for (char c : res.toCharArray()){
                    if (c == '.') countDots++;
                }
            }
            checkFlag = countDots <= 1;
        }
        return checkFlag;
    }

    private void getResult() {
    if(textField.getText()!=null) {
        String inString = textField.getText().toString();
        inString = inString.replace("\u00F7", "/");
        inString = inString.replace("\u00D7", "*");
        String test = inString.replaceAll("[-+*/()]", "");

        if (test.length() > 0 && checkDots(inString)) {
            ArrayList<String> outArrayString = PostfixConverter.convertString(inString, (check) -> {
                this.check = check;
                if (!check) {
                    resultText.setFocusable(false);
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
                        textField.setFocusable(false);
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
            textField.setFocusable(false);
        }
    }
    }
}