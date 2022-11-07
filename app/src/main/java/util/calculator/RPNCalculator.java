package util.calculator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPNCalculator implements Calculator {

    final private Map<Character, Integer> OPERATORS = Map.of('√', 3, '^', 3, '*', 2, '/', 2, '+', 0, '-', 0);

    private enum Associativity {
        LEFT, RIGHT
    }

    private boolean isOperator(String token) {
        if (token.length() != 1) {
            return false;
        } else {
            return isOperator(token.charAt(0));
        }
    }

    private boolean isOperator(char ch) {
        for (Character op : OPERATORS.keySet()) {
            if (ch == op) {
                return true;
            }
        }

        return false;
    }

    private int getPrecedence(Character operator) {
        Integer precedence = OPERATORS.get(operator);
        return Objects.requireNonNullElse(precedence, -1);
    }

    private Associativity getAssociativity(Character operator) {
        Integer precedence = OPERATORS.get(operator);
        if (precedence == null) {
            throw new Error("This code should not be reached... Evaluating non-existing operator");
        } else {
            if (precedence % 2 == 1) {
                return Associativity.RIGHT;
            } else {
                return Associativity.LEFT;
            }
        }
    }

    public List<String> infixToRPN(String expression) throws InvalidExpressionException {
        Stack<Character> stack = new Stack<>();
        List<String> tokens = new ArrayList<>();
        StringBuilder operand = new StringBuilder(); // For allowing numbers with multiple digits

        for (int i = 0; i < expression.length(); i++) {
            char current = expression.charAt(i);
            if (Character.isDigit(current) || current == '.') {
                operand.append(current);
            } else {
                if (operand.length() > 0) {
                    tokens.add(operand.toString());
                    operand.setLength(0);
                }

                if (isOperator(current)) {
                    int currentPrecedence = getPrecedence(current);
                    // Precedence will be -1 in case of non-operator and stop the loop
                    while (!stack.isEmpty() && currentPrecedence <= getPrecedence(stack.peek())
                            && getAssociativity(current) == Associativity.LEFT) {
                        tokens.add(Character.toString(stack.pop()));
                    }

                    stack.push(current);
                } else if (current == '(') {
                    stack.push(current);
                } else if (current == ')') {
                    while (stack.peek() != '(') {
                        if (stack.isEmpty()) {
                            throw new InvalidExpressionException();
                        }
                        tokens.add(Character.toString(stack.pop()));
                    }
                    stack.pop(); // Remove matching parenthesis
                }
            }
        }
        if (operand.length() > 0) {
            tokens.add(operand.toString());
        }
        while (!stack.isEmpty()) {
            Character peek = stack.pop();
            if (peek == '(') {
                throw new InvalidExpressionException();
            }

            tokens.add(Character.toString(peek));
        }

        return tokens;
    }

    private double singleOperate(Character operator, double a, double b) {
        double result = 0.0;
        if (operator == '√') { // b = 2
            result = Math.sqrt(b);
        } else if (operator == '^') {
            result = Math.pow(a, b);
        } else if (operator == '*') {
            result = a * b;
        } else if (operator == '/') {
            result = a / b;
        } else if (operator == '+') {
            result = a + b;
        } else if (operator == '-') {
            result = a - b;
        }

        return result;
    }

    public double calculateRPN(List<String> tokens) {
        Stack<Double> stack = new Stack<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                double a = stack.pop();
                double b = stack.pop();
                stack.push(singleOperate(token.charAt(0), b, a));
            } else {
                stack.push(Double.parseDouble(token));
            }
        }

        return stack.pop();
    }

    private String preprocessInfix(String expression) throws InvalidExpressionException{
        // This feature could be used to implement nth-roots in future
        expression = expression.replaceAll("√", "2√");
        //expression = expression.replaceAll("-\\(", "-1*(");
        expression = expression.replaceAll("\\(-", "(0-");

        Pattern invalidPatterns = Pattern.compile("([0-9]√)|([0-9]\\()|(\\)[0-9])");
        Matcher m = invalidPatterns.matcher(expression);
        if (m.find()) {
            // Do not allow skipping operators after/before parenthesis i.e. {digit}( or ){digit}
            throw new InvalidExpressionException();
        }

        if (expression.startsWith("-") || expression.startsWith("+")) {
            expression = "0" + expression;
        }

        return expression;
    }

    public double evaluate(String expression) throws InvalidExpressionException {
        try {
            String processedExpression = preprocessInfix(expression);
            List<String> tokens = infixToRPN(processedExpression);
            // System.out.println(String.join(" ", tokens));

            return calculateRPN(tokens);

        } catch (EmptyStackException | NumberFormatException ne) {
            throw new InvalidExpressionException();
        }
    }
}

