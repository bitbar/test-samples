package com.bitbar.testdroid.sampletests;

import android.app.Activity;
import android.graphics.Bitmap;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor;
import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import androidx.test.runner.screenshot.Screenshot;
import android.view.View;

import java.io.IOException;
import java.util.HashSet;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;

/**
 * Created by testdroid on 26/03/2018.
 */

public class TestUtilities {

    public void takeScreenshot(String name, Activity activity)
    {
        // stores screenshots in this location (pass it as the "Screenshots directory" value in cloud project):
        // /sdcard/Pictures/Screenshots

        ScreenCapture captureScreen = Screenshot.capture(activity);
        captureScreen.setName(name);
        captureScreen.setFormat(Bitmap.CompressFormat.PNG);

        HashSet<ScreenCaptureProcessor> captureProcessors = new HashSet();
        captureProcessors.add(new BasicScreenCaptureProcessor());

        try {
            captureScreen.process(captureProcessors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Activity getCurrentActivity() {

        final Activity[] activity = new Activity[1];
        onView(isRoot()).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                activity[0] = (Activity) view.findViewById(android.R.id.content).getContext();
            }
        });
        return activity[0];
    }

}
