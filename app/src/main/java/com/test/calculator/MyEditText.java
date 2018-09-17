package com.test.calculator;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

public class MyEditText extends android.support.v7.widget.AppCompatEditText{

    StringBuffer sb;
    int lastSelection;
    String lastDigit;
    boolean ignore;

    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        InputFilter inputFilter = (source, start, end, dest, dstart, dend) -> filterText(source.toString());
        this.setFilters(new InputFilter[]{inputFilter});
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        sb = new StringBuffer(text);
        if(sb.toString().equals("0")){
            setSelection(1);
        }

        if(sb.length()==1 && !sb.toString().equals("0")){
            ignore=false;
        }

        if(sb.length()==0){
            setText(getResources().getString(R.string.clear));
        }

        for(int i=0;i<10;i++) {
            if (sb.length()>1 && sb.toString().equals("0" + i)) {
                setText(String.valueOf(i));
            }
        }
    }

    public void pressKey(String s){
        filterText(s);
    }

    public void addDigit(String c,int selection) {
        if(sb.length()!=0) {
            if(sb.toString().equals("0") && !c.equals(".")) {
                ignore=true;
                    sb.replace(0, 1, String.valueOf(c));
            } else {
                sb.insert(selection, c);
            }
            updateText(selection);
        }
    }

    private void replaceDigit(String c,int selection){
        sb.replace(selection-1,selection,c);
        updateText(selection-1);
    }

    private void updateText(int selection){
        setText(sb);
        if(sb.length()>1) {
            setSelection(selection + 1);
        }
        else {
            setSelection(1);
        }
    }
////////////////////////////////////////// FILTER ////////////////////////////////////////////////////////////////
    private String filterText(String s){
        String text = sb.toString();

        if(s.equals("clear")){
            return "0";

        } else if (contains("[a-zA-Z$&,:_;=?@#|'<>^!{}\\u005C\\u0022]", s)) {
            return "";

        } else if (s.length()==1 && !ignore) {
            if(!lastDigit.equals(")") && contains("[0-9]",s)) {
                addDigit(s, lastSelection);
            }

            if(text.equals("0") || lastDigit.equals("(") && !lastDigit.equals(".")) {
                if(s.equals("-")) {
                    addDigit(s, lastSelection);
                }
            } else if (!text.equals("0") && !lastDigit.equals(".") && contains("[-+/*\\u00D7\\u00F7]",s)) {
                if(!text.equals("-") && contains("[-+/*\\u00D7\\u00F7]",lastDigit)){
                    replaceDigit(s,lastSelection);
                } else if(!text.equals("0")&& !text.equals("-") && !lastDigit.equals("(")){
                    addDigit(s,lastSelection);
                }
            }

            if(s.equals(")")){
                if(!checkDelim(text.substring(0,lastSelection))
                && contains("[0-9]",lastDigit)){
                    addDigit(s,lastSelection);
                }
            }
            if(s.equals("(")){
                if(contains("[-+/*\\u00D7\\u00F7]",lastDigit) || text.equals("0")){
                    addDigit(s,lastSelection);
                }
            }
            if(lastSelection!=0 && contains("[0-9]",lastDigit) && s.equals(".")){
                if(!text.substring(0,lastSelection).contains(".")) {
                    addDigit(s, lastSelection);
                } else
                if(!checkLastWord(text.substring(0,lastSelection))){
                    addDigit(s,lastSelection);
                }
            }
            return "";
        }

        else
            return null;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean checkDelim(String s){
        int leftDelim=0, rightDelim=0;
        for (char c : s.toCharArray()) {
            if (c == '(') leftDelim++;
            if (c == ')') rightDelim++;
        }
        return rightDelim >= leftDelim;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        lastSelection = selStart;
        if(sb!=null && selStart>0) {
            lastDigit = sb.substring(lastSelection-1,lastSelection);
        }
        super.onSelectionChanged(selStart, selEnd);
    }

    public static boolean contains(String pattern, String content) {
        return content.matches(pattern);
    }

    private boolean checkLastWord(String s){
        StringBuffer stringBuffer = new StringBuffer(s);
        stringBuffer = stringBuffer.reverse();
        String lastWord = stringBuffer.toString();
        for(int i=0;i<lastWord.length();i++){
            if(contains("[-+*/\\u00D7\\u00F7]",lastWord.substring(i,i+1))){
                s = s.substring(s.length()-i,s.length());
                break;
            }
        }
        return s.contains(".");
    }
}
