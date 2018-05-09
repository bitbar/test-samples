
package com.bitbar.testdroid.sampletests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.bitbar.testdroid.BitbarSampleApplicationActivity;
import com.bitbar.testdroid.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class BitbarSampleTests extends TestUtilities {

    public static final String STRING_TO_BE_TYPED = "Espresso";
    public static final String WRONG_ANSWER_TEXT = "Wrong Answer!";
    public static final String RIGHT_ANSWER_TEXT = "You are right!";


    // open mainActivity (BitbarSampleApplicationActivity) at test start
    @Rule
    public ActivityTestRule<BitbarSampleApplicationActivity> mActivityRule = new ActivityTestRule(
            BitbarSampleApplicationActivity.class);

    @Rule
    public final TestName testName = new TestName();


    @Test
    public void runWrongAnswerTest() throws Exception {

        // run test and capture screenshot if test fails
        try
        {
            wrongAnswerTest();
        }
        catch (Exception e)
        {
            takeScreenshot("test-"+testName.getMethodName()+"-failed",getCurrentActivity());
            throw e;
        }

    }

    @Test
    public void runRightAnswerTest() throws Exception {

        try
        {
            rightAnswerTest();
        }
        catch (Exception e)
        {
            takeScreenshot("test-"+testName.getMethodName()+"-failed",getCurrentActivity());
            throw e;
        }

    }

    @Test
    public void runFailIntentionallyTest() throws Exception {

        try
        {
            failTest();
        }
        catch (Exception e)
        {
            takeScreenshot("test-"+testName.getMethodName()+"-failed",getCurrentActivity());
            throw e;
        }

    }

    public void wrongAnswerTest() throws Exception {

        // select "Buy 101 devices"
        onView(withId(R.id.radio0)).perform(click());

        takeScreenshot("wrongAnswer-app-open", getCurrentActivity());

        onView(withId(R.id.editText1)).check(matches(isDisplayed()));

        // type "Espresso", close keyboard
        onView(withId(R.id.editText1))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // click "Answer"
        onView(withId(R.id.button1)).perform(click());

        // check that proper element is visible
        onView(withId(R.id.wrongTextView1)).check(matches(isDisplayed()));

        takeScreenshot("wrongAnswer-atWrongAnswerScreen", getCurrentActivity());

        // check that element contains correct text (Wrong Answer!)
        onView(withId(R.id.wrongTextView1)).check(matches(withText(WRONG_ANSWER_TEXT)));
    }

    public void rightAnswerTest() throws Exception {

        // select "Use Testdroid cloud"
        onView(withId(R.id.radio1)).perform(click());

        takeScreenshot("rightAnswer-app-open", getCurrentActivity());

        onView(withId(R.id.editText1)).check(matches(isDisplayed()));

        // type "Espresso", close keyboard
        onView(withId(R.id.editText1))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // click "Answer"
        onView(withId(R.id.button1)).perform(click());

        // check that proper element is visible
        onView(withId(R.id.correctTextView1)).check(matches(isDisplayed()));

        takeScreenshot("rightAnswer-atRightAnswerScreen", getCurrentActivity());

        // check that element contains correct text (You are right!)
        onView(withId(R.id.correctTextView1)).check(matches(withText(RIGHT_ANSWER_TEXT)));
    }

    public void failTest() throws Exception {

        // select "Ask mom for help
        onView(withId(R.id.radio2)).perform(click());

        takeScreenshot("failTestIntentionally-app-open", getCurrentActivity());

        onView(withId(R.id.editText1)).check(matches(isDisplayed()));

        // type "Espresso", close keyboard
        onView(withId(R.id.editText1))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());

        // click "Answer"
        onView(withId(R.id.button1)).perform(click());

        // check that proper element is visible
        onView(withId(R.id.wrongTextView1)).check(matches(isDisplayed()));

        takeScreenshot("failTestIntentionally-atWrongAnswerScreen", getCurrentActivity());

        // check that element does not contain text (Wrong Answer!)
        onView(withId(R.id.wrongTextView1)).check(matches(not(withText(WRONG_ANSWER_TEXT))));
    }
}
