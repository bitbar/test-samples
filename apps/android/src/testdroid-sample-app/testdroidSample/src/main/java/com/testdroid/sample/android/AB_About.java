package com.testdroid.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;

import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class AB_About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab_layout);

        // set dialog activity size
        {
            int margin = Helpers.getDimenDp(getApplicationContext(), R.dimen.margin_activity_dialog_narrow);
            Pair<Integer, Integer> dialogDimensions = Helpers.getWindowDimensionsWithoutMargin(getApplicationContext(), getWindowManager(), margin, margin);
            getWindow().setLayout(dialogDimensions.first, dialogDimensions.second);
        }

    }
}
