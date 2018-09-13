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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String operators = "+-/*%";
    private String delim = "() " + operators;

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
                check = true;
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
        ArrayList<String> outArrayString = convertString(inString);
        if (check) {
            double result = calculate(outArrayString);
            String sResult = String.format (Locale.getDefault(),"%f", result);
            if(sResult.contains(",")) {
                int dotIndex = sResult.indexOf(",");
                String tempString = sResult.substring(dotIndex+1, sResult.length());
                for (int i = 0; i < sResult.length(); i++) {
                    if(tempString.length()!=0 && tempString.charAt(tempString.length()-1)=='0'){
                        tempString = tempString.substring(0,tempString.length()-1);
                        sResult = sResult.substring(0,sResult.length()-1);
                    }
                }
            }
            if(sResult.charAt(sResult.length()-1)==','){
                sResult = sResult.substring(0,sResult.length()-1);
            }
            resultText.setText(getResources().getString(R.string.result, sResult));
        }
    } else {
        resultText.setText(getResources().getString(R.string.error));
    }
    }

    private boolean isOperator(String s) {
        if (s.equals("unary")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (s.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private int operatorPriority(String s) {
        if (s.equals("(")) return 1;
        if (s.equals("+") || s.equals("-")) return 2;
        if (s.equals("*") || s.equals("/")) return 3;
        return 4;
    }

    private boolean isDelim(String s) {
        if (s.length() != 1) return false;
        for (int i = 0; i < delim.length(); i++) {
            if (s.charAt(0) == delim.charAt(i)) return true;
        }
        return false;
    }

    public ArrayList<String> convertString(String inString) {
        ArrayList<String> outString = new ArrayList<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        String prev = "";
        String curr;
        StringTokenizer st = new StringTokenizer(inString, delim, true);
        while (st.hasMoreTokens()) {
            curr = st.nextToken();
            if (!st.hasMoreTokens() && isOperator(curr)) {
                check = false;
                return outString;
            }
            if (curr.equals(" ")) continue;
            else if (isDelim(curr)) {
                switch (curr) {
                    case "(":
                        stack.push(curr);
                        break;
                    case ")":
                        if(stack.peek()!=null) {
                            while (!stack.peek().equals("(")) {
                                outString.add(stack.pop());
                                if (stack.isEmpty()) {
                                    check = false;
                                    resultText.setText(getResources().getString(R.string.error));
                                    return outString;
                                }
                            }
                            stack.pop();
                        }
                        break;
                    default:
                        if (curr.equals("-") && (prev.equals("") || (isDelim(prev) && !prev.equals(")")))) {
                            curr = "unary";
                        } else {
                            while (!stack.isEmpty() && (operatorPriority(curr) <= operatorPriority(stack.peek()))) {
                                outString.add(stack.pop());
                            }

                        }
                        stack.push(curr);
                        break;
                }

            }

            else {
                outString.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) outString.add(stack.pop());
            else {
                resultText.setText(getResources().getString(R.string.error));
                check = false;
                return outString;
            }
        }
        return outString;
    }

    public static Double calculate(ArrayList<String> inString) {
        ArrayDeque<Double> stack = new ArrayDeque<>();
        for (String s : inString) {
            switch (s) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a - b);
                    break;
                }
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a / b);
                    break;
                }
                case "unary":
                    stack.push(-stack.pop());
                    break;
                default:
                    stack.push(Double.valueOf(s));
                    break;
            }
        }
        return stack.pop();
    }
}