package com.testdroid.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import utils.Constants;
import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class AN_Answer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.an_layout);

        // set dialog activity size
        {
            int margin = Helpers.getDimenDp(getApplicationContext(), R.dimen.margin_activity_dialog_wide);
            Pair<Integer, Integer> dialogDimensions = Helpers.getWindowDimensionsWithoutMargin(getApplicationContext(), getWindowManager(), margin, margin);
            getWindow().setLayout(dialogDimensions.first, dialogDimensions.second);

        }

        // UI Widgets
        ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
        LinearLayout ll_score = (LinearLayout) findViewById(R.id.an_ll_score);
        TextView tv_heading = (TextView) findViewById(R.id.an_tv_heading);
        TextView tv_comment = (TextView) findViewById(R.id.an_tv_comment);
        TextView tv_score = (TextView) findViewById(R.id.an_tv_score);
        TextView tv_scoreMax = (TextView) findViewById(R.id.an_tv_scoreMax);

        // getting extras
        Bundle bundle = getIntent().getExtras();
        boolean isAnswerCorrect = bundle.getBoolean(Constants.KV_IS_ANSWER_CORRECT, true);
        String name = bundle.getString(Constants.KV_NAME, null);
        int score = bundle.getInt(Constants.KV_SCORE, -1);

        if (isAnswerCorrect) {
            iv_icon.setImageResource(R.drawable.icon_sun);
            tv_heading.setText(getResources().getString(R.string.an_headingCorrect));
            if (name != null) {
                tv_comment.setText(String.format("%s, %s.", getResources().getString(R.string.an_commentCorrect), name));
            } else {
                tv_comment.setVisibility(View.GONE);
                ll_score.setVisibility(View.VISIBLE);
                tv_score.setText("" + score);
                tv_scoreMax.setText("" + Constants.MAX_GAME_SCORE);
            }
            tv_heading.setTextColor(getResources().getColor(R.color.status_green));
        } else {
            iv_icon.setImageResource(R.drawable.icon_rain);
            tv_heading.setText(getResources().getString(R.string.an_headingWrong));
            tv_comment.setText(String.format("%s, %s?", getResources().getString(R.string.an_commentWrong), name));
            tv_heading.setTextColor(getResources().getColor(R.color.status_red));
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
