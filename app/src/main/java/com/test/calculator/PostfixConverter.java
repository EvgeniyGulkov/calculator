package com.test.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PostfixConverter {
    private static String operators = "+-/*%";
    private static String delim = "() " + operators;

    public interface ConvertListener {
        void onCallback(boolean check);

    }

    private static boolean isOperator(String s) {
        if (s.equals("unary")) return true;
        for (int i = 0; i < operators.length(); i++) {
            if (s.charAt(0) == operators.charAt(i)) return true;
        }
        return false;
    }

    private static int operatorPriority(String s) {
        if (s.equals("(")) return 1;
        if (s.equals("+") || s.equals("-")) return 2;
        if (s.equals("*") || s.equals("/")) return 3;
        return 4;
    }

    private static boolean isDelim(String s) {
        if (s.length() != 1) return false;
        for (int i = 0; i < delim.length(); i++) {
            if (s.charAt(0) == delim.charAt(i)) return true;
        }
        return false;
    }

    public static ArrayList<String> convertString(String inString, ConvertListener cl) {
        ArrayList<String> outString = new ArrayList<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        String prev = "";
        String curr;
        StringTokenizer st = new StringTokenizer(inString, delim, true);
        while (st.hasMoreTokens()) {
            curr = st.nextToken();
            if (!st.hasMoreTokens() && isOperator(curr)) {
                cl.onCallback(false);
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
                                    cl.onCallback(false);
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
                cl.onCallback(false);
                return outString;
            }
        }
        cl.onCallback(true);
        return outString;
    }
}
