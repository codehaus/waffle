package org.codehaus.waffle.example.paranamer.action;

public class CalculatorController {
    public Number result;

    public Number getResult() {
        return result;
    }

    public void add(int firstNumber, int secondNumber) {
        result = firstNumber + secondNumber;
    }

    public void subtract(long firstNumber, long secondNumber) {
        result = firstNumber - secondNumber;
    }

    public void multiply(float firstNumber, Float secondNumber) {
        result = firstNumber * secondNumber;
    }

}
