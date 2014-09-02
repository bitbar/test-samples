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

    private static final String TAG = AN_Answer.class.getName().toString();

    private boolean isAnswerCorrect;
    private String name;
    private int score;

    // UI Widgets
    private static ImageView iv_icon;
    private static LinearLayout ll_score;
    private static TextView tv_heading;
    private static TextView tv_comment;
    private static TextView tv_score;
    private static TextView tv_scoreMax;

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

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        ll_score = (LinearLayout) findViewById(R.id.an_ll_score);
        tv_heading = (TextView) findViewById(R.id.an_tv_heading);
        tv_comment = (TextView) findViewById(R.id.an_tv_comment);
        tv_score = (TextView) findViewById(R.id.an_tv_score);
        tv_scoreMax = (TextView) findViewById(R.id.an_tv_scoreMax);

        // getting extras
        Bundle bundle = getIntent().getExtras();
        this.isAnswerCorrect = bundle.getBoolean(Constants.KV_IS_ANSWER_CORRECT, true);
        this.name = bundle.getString(Constants.KV_NAME, null);
        this.score = bundle.getInt(Constants.KV_SCORE, -1);

        if (isAnswerCorrect) {
            iv_icon.setImageResource(R.drawable.icon_sun);
            tv_heading.setText(getResources().getString(R.string.an_headingCorrect));
            String comment;
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
