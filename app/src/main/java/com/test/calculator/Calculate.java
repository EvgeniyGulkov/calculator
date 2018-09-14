package com.test.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Calculate {

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
