package com.aperpen.calculator;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void TestInput() {
        onView(withId(R.id.button0)).perform(click());
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonDot)).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());
        onView(withId(R.id.button3)).perform(click());
        onView(withId(R.id.buttonPower)).perform(click());
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.buttonSubtract)).perform(click());
        onView(withId(R.id.buttonSquareRoot)).perform(click());
        onView(withId(R.id.button5)).perform(click());
        onView(withId(R.id.buttonMultiply)).perform(click());
        onView(withId(R.id.buttonOpenParenthesis)).perform(click());
        onView(withId(R.id.button7)).perform(click());
        onView(withId(R.id.buttonSubtract)).perform(click());
        onView(withId(R.id.button5)).perform(click());
        onView(withId(R.id.buttonCloseParenthesis)).perform(click());
        onView(withId(R.id.buttonDivide)).perform(click());
        onView(withId(R.id.button8)).perform(click());
        onView(withId(R.id.buttonSignChange)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.button0)).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.buttonBack)).perform(click());

        ViewInteraction tvExpression = onView(withId(R.id.tvExpression));
        tvExpression.check(matches(withText("-(1.2+3^4-√5*(7-5)/8)+10")));

        onView(withId(R.id.buttonClear)).perform(click());
        tvExpression.check(matches(withText("")));
    }

    @Test
    public void TestCalculateWhenCorrectExpression() {
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonDot)).perform(click());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());
        onView(withId(R.id.button3)).perform(click());
        onView(withId(R.id.buttonEqual)).perform(click());

        ViewInteraction tvExpression = onView(withId(R.id.tvExpression));
        tvExpression.check(matches(withText("4.2")));
        ViewInteraction tvResult = onView(withId(R.id.tvResult));
        tvResult.check(matches(withText("1.2+3")));
    }

    @Test
    public void TestResetAfterCalculateCorrectExpression() {
        ViewInteraction tvExpression = onView(withId(R.id.tvExpression));
        ViewInteraction tvResult = onView(withId(R.id.tvResult));

        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());
        onView(withId(R.id.button3)).perform(click());
        onView(withId(R.id.buttonEqual)).perform(click());
        tvExpression.check(matches(withText("4")));
        tvResult.check(matches(withText("1+3")));

        onView(withId(R.id.button3)).perform(click());
        tvExpression.check(matches(withText("3")));
        tvResult.check(matches(withText("")));
    }

    @Test
    public void TestCalculateWhenIncorrectExpression() {
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonOpenParenthesis)).perform(click());
        onView(withId(R.id.buttonEqual)).perform(click());

        ViewInteraction tvResult = onView(withId(R.id.tvResult));
        tvResult.check(matches(withText("Invalid expression")));
    }

    @Test
    public void TestLiveCalculationWhenCorrectExpression() {
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());
        onView(withId(R.id.button1)).perform(click());

        ViewInteraction tvResult = onView(withId(R.id.tvResult));
        tvResult.check(matches(withText("2")));
    }

    @Test
    public void TestLiveCalculationWhenIncorrectExpression() {
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.buttonSum)).perform(click());

        ViewInteraction tvResult = onView(withId(R.id.tvResult));
        tvResult.check(matches(withText("")));
    }
}
