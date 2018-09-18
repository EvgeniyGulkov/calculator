package com.test.calculator;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

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

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        hideKeyboard();
        return super.onCheckIsTextEditor();
    }

    void init(){
        setCursorVisible(true);
        InputFilter inputFilter = (source, start, end, dest, dstart, dend) -> filterText(source.toString());
        this.setFilters(new InputFilter[]{inputFilter,new InputFilter.LengthFilter(88)});
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        sb = new StringBuffer(text);
        if(sb.toString().equals("0")){
            setSelection(1);
        } else {
            ignore=false;
        }

        if(sb.length()==0){
            setText(getResources().getString(R.string.clear));
        }
    }

    public void pressKey(String s){
        filterText(s);
    }

    public void addDigit(String c,int selection) {
            sb.insert(selection, c);
            updateText(selection);
    }

    private void replaceDigit(String c,int selection){
        sb.replace(selection-1,selection,c);
        updateText(selection-1);
    }

    private void deleteDigit(int selection){
        ignore=true;
        sb.deleteCharAt(selection-1);
        setText(sb);
        setSelection(selection - 1);
    }

    private void updateText(int selection){
        setText(sb);
            if(sb.length()<88) {
                setSelection(selection + 1);
            } else {
                setSelection(selection);
            }
    }
////////////////////////////////////////// FILTER ////////////////////////////////////////////////////////////////
    private String filterText(String s) {
        String text = sb.toString();

        if(s.equals("delete") && lastSelection>0){
            deleteDigit(lastSelection);
        }

        if(s.equals("clear")){
            ignore=false;
            return "0";

        } else if (contains("[a-zA-Z$&,:_;=?@#|'<>^!{}\\u005C\\u0022]", s)) {
            return "";

        } else if(s.length()==1 && !text.equals("0") && !ignore) {

                if (!lastDigit.equals(")") && contains("[0-9]", s)) {
                        addDigit(s, lastSelection);
                }

                if (!lastDigit.equals(".") && contains("[-+/*\\u00D7\\u00F7]", s)) {
                    if (!text.equals("-") && contains("[-+/*\\u00D7\\u00F7]", lastDigit)) {
                        replaceDigit(s, lastSelection);
                    } else if (!text.equals("-") && !lastDigit.equals("(")) {
                        addDigit(s, lastSelection);
                    }
                }

                if (lastDigit.equals("(") && !lastDigit.equals(".") && s.equals("-")) {
                    addDigit(s, lastSelection);
                }

                if (s.equals(")")) {
                    if (!checkDelim(text)
                            && contains("[)0-9]", lastDigit)) {
                        addDigit(s, lastSelection);
                    }
                }

                if (s.equals("(")) {
                    if (contains("[-+/*\\u00D7\\u00F7(]", lastDigit) || text.equals("0")) {
                        addDigit(s, lastSelection);
                    }
                }

                if (lastSelection != 0 && contains("[0-9]", lastDigit) && s.equals(".")) {
                    if (contains("[-+/*\\u00D7\\u00F7]", text)) {
                        if (!text.contains(".")) {
                            addDigit(s, lastSelection);
                        } else if (!checkLastWord(text.substring(0, lastSelection), text.substring(lastSelection - 1, text.length()))) {
                            addDigit(s, lastSelection);
                        }
                    } else if (!text.contains(".")) {
                        addDigit(s, lastSelection);
                    }
                }
                return "";
            }
            else if (s.length()==1 && text.equals("0")) {
                if (contains("[1-9-(]", s)) {
                    ignore = true;
                    replaceDigit(s, 1);
                }
                if(s.equals(".")){
                    addDigit(s,1);
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
        if(sb!=null &&  selStart>0) {
            lastDigit = sb.substring(lastSelection-1,lastSelection);
        }
        super.onSelectionChanged(selStart, selEnd);
    }

    public static boolean contains(String pattern, String content) {
        boolean contain=false;
        for(int i=0;i<content.length();i++){
            if(content.substring(i,i+1).matches(pattern)){
                contain=true;
                break;
            }
        }
        return contain;
    }

    private boolean checkLastWord(String firstPart,String lastPart){
        StringBuffer stringBuffer = new StringBuffer(firstPart);
        stringBuffer = stringBuffer.reverse();
        String lastWord = stringBuffer.toString();
        for(int i=0;i<lastWord.length();i++){
            if(contains("[-+*/\\u00D7\\u00F7]",lastWord.substring(i,i+1))){
                firstPart = firstPart.substring(firstPart.length()-i,firstPart.length());
                break;
            }
        }
        lastWord = lastPart;
        for(int i=0;i<lastWord.length();i++){
            if(contains("[-+*/\\u00D7\\u00F7]",lastWord.substring(i,i+1))){
                lastPart = lastPart.substring(0,i);
                break;
            }
        }
        String s = firstPart+lastPart;
        return s.contains(".");
    }
}
