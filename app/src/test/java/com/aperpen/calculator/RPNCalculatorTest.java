package com.aperpen.calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import util.calculator.InvalidExpressionException;
import util.calculator.RPNCalculator;


public class RPNCalculatorTest {
    final private RPNCalculator calculator = new RPNCalculator();

    @Test
    public void TestConvertToRPN() throws InvalidExpressionException {
        final String infix = "2*(3+4)^2";
        final List<String> rpn = Arrays.asList("2", "3", "4", "+", "2", "^", "*");
        assertEquals(rpn, calculator.infixToRPN(infix));
    }

    @Test
    public void TestConvertToRPNWhenNonIntegerNumber() throws InvalidExpressionException {
        final String infix = "2*(3.3+4.5)^2";
        final List<String> rpn = Arrays.asList("2", "3.3", "4.5", "+", "2", "^", "*");
        assertEquals(rpn, calculator.infixToRPN(infix));
    }

    @Test
    public void TestCalculateRPN() {
        final List<String> rpn = Arrays.asList("2", "3", "4", "+", "2", "^", "*");
        final double result = 98.0;
        assertEquals(result, calculator.calculateRPN(rpn), 0);
    }

    @Test
    public void TestEvaluate() throws InvalidExpressionException {
        final String expression = "2*(3+4)^2+√4-1+4/2";
        final double result = 101.0;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    @Test
    public void TestEvaluateNestedParenthesis() throws InvalidExpressionException {
        final String expression = "2*((3+4)*2)^2";
        final double result = 392.0;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    @Test
    public void TestEvaluateWhenNonIntegerResult() throws InvalidExpressionException {
        final String expression = "2*(3+4)^2+1.5";
        final double result = 99.5;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    @Test
    public void TestEvaluateWhenInitialMinusSymbol() throws InvalidExpressionException {
        final String expression = "-(2*(3+4)^2)";
        final double result = -98.0;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    @Test
    public void TestEvaluateWhenInitialPlusSymbol() throws InvalidExpressionException {
        final String expression = "+(2*(3+4)^2)";
        final double result = 98.0;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    @Test
    public void TestEvaluateWhenNegativeNumber() throws InvalidExpressionException {
        final String expression = "2+(-2)";
        final double result = 0.0;
        assertEquals(result, calculator.evaluate(expression), 0);
    }

    // Test Invalid expression here because error can be detected on different stages
    @Test(expected = InvalidExpressionException.class)
    public void TestEvaluateWhenInvalidExpression_NotMatchingCloseParenthesis()
            throws InvalidExpressionException {
        final String infix = "2*((3+4)^2";
        calculator.evaluate(infix);
    }

    @Test(expected = InvalidExpressionException.class)
    public void TestEvaluateWhenInvalidExpression_DoubleOperator()
            throws InvalidExpressionException {
        final String infix = "2*/4^2";
        calculator.evaluate(infix);
    }

    @Test(expected = InvalidExpressionException.class)
    public void TestEvaluateWhenInvalidExpression_NotMatchingOpenParenthesis()
            throws InvalidExpressionException {
        final String infix = "2*(3+4))^2";
        calculator.evaluate(infix);
    }

    @Test(expected = InvalidExpressionException.class)
    public void TestEvaluateWhenInvalidExpression_OperatorParenthesis()
            throws InvalidExpressionException {
        final String infix = "2(3+4)^2";
        calculator.evaluate(infix);
    }
}