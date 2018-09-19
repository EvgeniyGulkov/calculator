package com.test.calculator;

import java.util.ArrayList;
import java.util.Locale;

public class CheckAndResult {
    private static boolean checker=true;

    public static String getResult(String s) {
        if(s!=null) {
            String inString = s;
            inString = inString.replace("\u00F7", "/");
            inString = inString.replace("\u00D7", "*");
            String test = inString.replaceAll("[-+*/()]", "");

            for(int i=0;i<10;i++){
                if(inString.contains(i+"(")){
                    inString = inString.replaceAll(i+"\\(",i+"*(");
                }
                if(inString.contains(")"+i)){
                    inString = inString.replaceAll("\\)"+i,")*"+i);
                }
            }

            if (!inString.contains("/0") && test.length() > 0) {
                ArrayList<String> outArrayString = PostfixConverter.convertString(inString, (check) -> {
                    checker=check;
                });

                if (checker) {
                    double result = Calculate.calculate(outArrayString);
                    String sResult = String.format(Locale.getDefault(), "%f", result);
                    for (int i = 0; i < 10; i++) {
                        if (sResult.contains(i + "(") || sResult.contains(")" + i)
                                || sResult.contains(").") || sResult.contains(".(")) {
                            checker = false;
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
                    return sResult;
                }
            }
        }
        return "error";
    }
}
