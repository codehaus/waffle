package org.codehaus.waffle.example.simple.controllers;

import org.codehaus.waffle.action.annotation.ActionMethod;

public class CalculatorController {
    private Number result;

    public Number getResult() {
        return result;
    }

    public void add(int firstNumber, int secondNumber) {
        result = firstNumber + secondNumber;
    }

    @ActionMethod(parameters = {"firstNumber", "secondNumber"})
    public void subtract(long firstNumber, long secondNumber) {
        result = firstNumber - secondNumber;
    }

    @ActionMethod(parameters = {"firstNumber", "secondNumber"})
    public void multiply(float firstNumber, Float secondNumber) {
        result = firstNumber * secondNumber;
    }

}
