package com.aperpen.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import util.calculator.Calculator;
import util.calculator.InvalidExpressionException;
import util.calculator.RPNCalculator;

public class MainActivity extends AppCompatActivity {
    TextView tvExpression;
    TextView tvResult;
    Calculator calculator;
    boolean isShowingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvExpression = findViewById(R.id.tvExpression);
        this.tvResult = findViewById(R.id.tvResult);
        this.calculator = new RPNCalculator();
        this.isShowingResult = false;
        initKeypad();
    }

    protected void initKeypad() {
        findViewById(R.id.button0).setOnClickListener(this::keypadClick);
        findViewById(R.id.button1).setOnClickListener(this::keypadClick);
        findViewById(R.id.button2).setOnClickListener(this::keypadClick);
        findViewById(R.id.button3).setOnClickListener(this::keypadClick);
        findViewById(R.id.button4).setOnClickListener(this::keypadClick);
        findViewById(R.id.button5).setOnClickListener(this::keypadClick);
        findViewById(R.id.button6).setOnClickListener(this::keypadClick);
        findViewById(R.id.button7).setOnClickListener(this::keypadClick);
        findViewById(R.id.button8).setOnClickListener(this::keypadClick);
        findViewById(R.id.button9).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonDot).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonPower).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonSquareRoot).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonMultiply).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonDivide).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonSum).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonSubtract).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonOpenParenthesis).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonCloseParenthesis).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonClear).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonBack).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonEqual).setOnClickListener(this::keypadClick);
        findViewById(R.id.buttonSignChange).setOnClickListener(this::keypadClick);
    }

    protected void clearExpression() {
        tvExpression.setText("");
    }

    protected void removeLastFromExpression() {
        CharSequence text = tvExpression.getText();
        if (text.length() > 0) {
            tvExpression.setText(text.subSequence(0, text.length() - 1));
        }
    }

    protected boolean expressionIsOnlyDigit() {
        CharSequence text = tvExpression.getText();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    protected void addToExpression(String token) {
        if (!token.equals(".") && tvExpression.getText().equals("0")) {
            clearExpression();
        }

        CharSequence newText = tvExpression.getText() + token;
        tvExpression.setText(newText);
    }

    protected void refreshResult() {
        String text = tvExpression.getText().toString();
        if (expressionIsOnlyDigit()) {
            tvResult.setText("");
            return;
        }

        try {
            evaluateExpression(text, tvResult);
        } catch (InvalidExpressionException e) {
            // Invalid expression, so we clear the result tv
            tvResult.setText("");
        }
    }

    private void evaluateExpression(String expression, TextView place) throws InvalidExpressionException {
        double result = calculator.evaluate(expression);
        String formattedResult;
        if (result == (long) result) {
            formattedResult = String.format(Locale.getDefault(), "%d", (long) result);
        } else {
            formattedResult = String.format(Locale.getDefault(), "%s", result);
        }
        place.setText(formattedResult);
    }

    protected void showResult() {
        if (expressionIsOnlyDigit()) { // Do not do nothing if we only have digits
            return;
        }

        try {
            String expression = tvExpression.getText().toString();
            evaluateExpression(expression, tvExpression);
            tvResult.setText(expression);
            this.isShowingResult = true;
        } catch (InvalidExpressionException e) {
            tvResult.setText(R.string.invalid_expression);
        }
    }

    protected void changeExpressionSign() {
        CharSequence text = tvExpression.getText();
        CharSequence newText;
        if (expressionIsOnlyDigit()) {
            newText = "-" + text;
        } else {
            newText = "-(" + text + ")";
        }

        tvExpression.setText(newText);
    }

    public void keypadClick(View v) {
        int vid = v.getId();
        if (vid == R.id.buttonEqual) {
            showResult();
        } else {
            if (isShowingResult) {
                clearExpression(); // We could add here a if (vid != R.id.buttonClear)
                isShowingResult = false;
            }

            if (vid == R.id.buttonClear) {
                clearExpression();
            } else if (vid == R.id.buttonBack) {
                removeLastFromExpression();
            } else if (vid == R.id.buttonSignChange) {
                changeExpressionSign();
            } else {
                Button b = (Button) v;
                String buttonText = b.getText().toString();
                addToExpression(buttonText);
            }

            refreshResult();
        }
    }
}