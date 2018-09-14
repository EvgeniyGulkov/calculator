package com.test.calculator;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText textField;
    TextView resultText;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.rootLayout);
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

        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check = true;
                String s1 = s.toString();
                if (s1.length() == 2 && s1.substring(0).equals(0)) {
                    s1 = s1.substring(1);
                    textField.setText(s1);
                }
                if (s1.length() == 0) {
                    s1 = "0";
                    textField.setText(s1);
                }
                String[] s2 = new String[]{
                        "--", "-+", "-\u00D7", "-\u00F7",
                        "++", "+-", "+\u00D7", "+\u00F7",
                        "\u00D7\u00D7", "\u00D7+", "\u00D7-", "\u00D7\u00F7",
                        "\u00F7\u00F7", "\u00F7+", "\u00F7-", "\u00F7\u00D7",
                };
                for (String aS2 : s2) {
                    if (s1.contains(aS2)) {
                        textField.setText(s1.replace(aS2, aS2.substring(aS2.length() - 1)));
                    }
                }

                String[] s3 = new String[]{"-", "+", "\u00F7", "\u00D7", "*", "/"};
                for (String aS3 : s3) {
                    for (int i = 0; i < 10; i++) {
                        if (s1.contains(aS3 + "0" + i)) {
                            textField.setText(s1.replace(aS3 + "0" + i, aS3 + i));
                        }
                    }
                }

                String []s4 = new String[]{"-.","+.","*.","\u00F7.","\u00D7.",".."};
                for (String aS4 : s4) {
                    if (s1.contains(aS4)) {
                        textField.setText(s1.replace(aS4, aS4.substring(0,1)+"0."));
                    }
                }
            }
        });
    }

    private void hideKeyboard() {
        textField.clearFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(textField.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        String text = textField.getText().toString();
        hideKeyboard();
        switch (view.getId()) {
            case R.id.oneBtn:
                addDigit("1");
                break;

            case R.id.twoBtn:
                addDigit("2");
                break;

            case R.id.threeBtn:
                addDigit("3");
                break;

            case R.id.fourBtn:
                addDigit("4");
                break;

            case R.id.fiveBtn:
                addDigit("5");
                break;

            case R.id.sixBtn:
                addDigit("6");
                break;

            case R.id.seven_btn:
                addDigit("7");
                break;

            case R.id.eightBtn:
                addDigit("8");
                break;

            case R.id.nineBtn:
                addDigit("9");
                break;

            case R.id.minusBtn:
                addDigit("-");
                break;

            case R.id.plusBtn:
                addDigit("+");
                break;

            case R.id.multiplBtn:
                addDigit("\u00D7");
                break;

            case R.id.divisionBtn:
                addDigit("\u00F7");
                break;

            case R.id.resultBtn:
                getResult();
                break;

            case R.id.delLastBtn:
                if (text.length() > 1) {
                    textField.setText(text.substring(0, text.length() - 1));
                } else {
                    textField.setText("0");
                }
                break;

            case R.id.dotBtn:
                addDigit(".");
                break;

            case R.id.rightDelim:
                addDigit(")");
                break;

            case R.id.leftDelim:
                addDigit("(");
                break;

            case R.id.zeroBtn:
                addDigit("0");
                break;

            case R.id.clear_btn:
                textField.setText("0");
                break;
        }
    }

    private void addDigit(String s) {
        String s1 = textField.getText().toString();
        String result;
        if (s1.equals("0") && !s.equals(".")) {
            result = s;
        } else
            result = s1 + s;
        textField.setText(result);
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
    String inString = textField.getText().toString();
    inString = inString.replace("\u00F7","/");
    inString = inString.replace("\u00D7","*");
    String test = inString.replaceAll("[-+*/()]", "");

    if(test.length()>0 && checkDots(inString)) {
        ArrayList<String> outArrayString = PostfixConverter.convertString(inString, (check) -> {
            this.check = check;
            if(!check){
                textField.setText(getResources().getString(R.string.error));
            }
        });
        Log.d("check",""+check);
        if (check) {
            double result = Calculate.calculate(outArrayString);
            int dotIndex;
            String sResult = String.format (Locale.getDefault(),"%f", result);
            if(sResult.contains(",")|| sResult.contains(".")) {
                if (sResult.contains(".")) {
                    dotIndex = sResult.indexOf(".");
                } else {
                    dotIndex = sResult.indexOf(",");
                }
                String tempString = sResult.substring(dotIndex+1, sResult.length());
                for (int i = 0; i < sResult.length(); i++) {
                    if(tempString.length()!=0 && tempString.charAt(tempString.length()-1)=='0'){
                        tempString = tempString.substring(0,tempString.length()-1);
                        sResult = sResult.substring(0,sResult.length()-1);
                    }
                }
            }
            if(sResult.charAt(sResult.length()-1)==',' || (sResult.charAt(sResult.length()-1)=='.')){
                sResult = sResult.substring(0,sResult.length()-1);
            }
            resultText.setText(getResources().getString(R.string.result, sResult));
        }
    } else {
        resultText.setText(getResources().getString(R.string.error));
    }
    }
}