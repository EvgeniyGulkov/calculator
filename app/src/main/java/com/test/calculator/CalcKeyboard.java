package com.test.calculator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CalcKeyboard extends LinearLayout{
    Callback callback;
    Button oneBtn;
    Button twoBtn;
    Button threeBtn;
    Button fourBtn;
    Button fiveBtn;
    Button sixBtn;
    Button sevenBtn;
    Button eightBtn;
    Button nineBtn;
    Button zeroBtn;
    Button clearBtn;
    Button deleteBtn;
    Button minusBtn;
    Button plusBtn;
    Button multBtn;
    Button divBtn;
    Button leftDelBtn;
    Button rightDelBtn;
    Button dotBtn;
    Button resultBtn;

    public CalcKeyboard(Context context) {
        super(context);
        init(context);
    }

    public CalcKeyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalcKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.calc_keyboard,this);
        (oneBtn = findViewById(R.id.oneBtn)).setOnClickListener(onClickListener);
        (twoBtn = findViewById(R.id.twoBtn)).setOnClickListener(onClickListener);
        (threeBtn = findViewById(R.id.threeBtn)).setOnClickListener(onClickListener);
        (fourBtn = findViewById(R.id.fourBtn)).setOnClickListener(onClickListener);
        (fiveBtn = findViewById(R.id.fiveBtn)).setOnClickListener(onClickListener);
        (sixBtn = findViewById(R.id.sixBtn)).setOnClickListener(onClickListener);
        (sevenBtn = findViewById(R.id.seven_btn)).setOnClickListener(onClickListener);
        (eightBtn = findViewById(R.id.eightBtn)).setOnClickListener(onClickListener);
        (nineBtn = findViewById(R.id.nineBtn)).setOnClickListener(onClickListener);
        (zeroBtn = findViewById(R.id.zeroBtn)).setOnClickListener(onClickListener);
        (clearBtn = findViewById(R.id.clear_btn)).setOnClickListener(onClickListener);
        (deleteBtn = findViewById(R.id.delLastBtn)).setOnClickListener(onClickListener);
        (resultBtn = findViewById(R.id.resultBtn)).setOnClickListener(onClickListener);
        (minusBtn = findViewById(R.id.minusBtn)).setOnClickListener(onClickListener);
        (plusBtn= findViewById(R.id.plusBtn)).setOnClickListener(onClickListener);
        (multBtn = findViewById(R.id.multiplBtn)).setOnClickListener(onClickListener);
        (divBtn = findViewById(R.id.divisionBtn)).setOnClickListener(onClickListener);
        (dotBtn = findViewById(R.id.dotBtn)).setOnClickListener(onClickListener);
        (leftDelBtn = findViewById(R.id.leftDelim)).setOnClickListener(onClickListener);
        (rightDelBtn = findViewById(R.id.rightDelim)).setOnClickListener(onClickListener);

    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String key = "0";
            switch (v.getId()) {
                case R.id.oneBtn:
                    key = "1";
                    break;

                case R.id.twoBtn:
                    key = "2";
                    break;

                case R.id.threeBtn:
                    key = "3";
                    break;

                case R.id.fourBtn:
                    key = "4";
                    break;

                case R.id.fiveBtn:
                    key = "5";
                    break;

                case R.id.sixBtn:
                    key = "6";
                    break;

                case R.id.seven_btn:
                    key = "7";
                    break;

                case R.id.eightBtn:
                    key = "8";
                    break;

                case R.id.nineBtn:
                    key = "9";
                    break;

                case R.id.minusBtn:
                    key = "-";
                    break;

                case R.id.plusBtn:
                    key = "+";
                    break;

                case R.id.multiplBtn:
                    key = "\u00D7";
                    break;

                case R.id.divisionBtn:
                    key = "\u00F7";
                    break;

                case R.id.resultBtn:
                    key = "result";
                    break;

                case R.id.delLastBtn:
                    key = "delete";
                    break;

                case R.id.dotBtn:
                    key = ".";
                    break;

                case R.id.rightDelim:
                    key = ")";
                    break;

                case R.id.leftDelim:
                    key = "(";
                    break;

                case R.id.zeroBtn:
                    key = "0";
                    break;

                case R.id.clear_btn:
                    key = "clear";
                    break;
            }
            callback.onCallback(key);
        }
    };


    interface Callback{
        void onCallback(String key);
    }

    public void addCallback(Callback cb){
        this.callback = cb;
        }
    }
