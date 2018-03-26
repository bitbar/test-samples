
package com.bitbar.testdroid.sampletests;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;


import android.content.res.Resources;
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


@RunWith(AndroidJUnit4.class)
@LargeTest
public class BitbarSampleTests extends TestUtilities {

    public static final String STRING_TO_BE_TYPED = "Espresso";
    public static final String WRONG_ANSWER_TEXT = "Wrong Answer!";
    public static final String RIGHT_ANSWER_TEXT = "You are right!";

    private Resources resources;

    @Rule
    public ActivityTestRule<BitbarSampleApplicationActivity> mActivityRule = new ActivityTestRule(
            BitbarSampleApplicationActivity.class);

    @Before
    public void init() {
        resources = mActivityRule.getActivity().getResources();
    }


    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            //take screenshot at test failure
            try {
                takeScreenshot("failed-"+description.getClassName() + "-" + description.getMethodName(), getCurrentActivity());
            }catch (Exception e1)
            {}
        }
    };


    @Test
    public void wrongAnswerTest() throws Exception {

        // select "Buy 101 devices"
        onView(withId(R.id.radio0)).perform(click());

        takeScreenshot("wrongAnswer-app-open", getCurrentActivity());

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

    @Test
    public void rightAnswerTest() throws Exception {

        // select "Use Testdroid cloud"
        onView(withId(R.id.radio1)).perform(click());

        takeScreenshot("rightAnswer-app-open", getCurrentActivity());

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
}
