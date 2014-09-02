package com.testdroid.sample.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class FC_Functions extends Activity implements View.OnClickListener {

    private static final String TAG = FC_Functions.class.getName().toString();

    // UI Widgets
    private static Button b_pingTest;
    private static Button b_explode;
    private static Button b_anr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_layout);

        b_pingTest = (Button) findViewById(R.id.fc_b_ping);
        b_explode = (Button) findViewById(R.id.fc_b_explode);
        b_anr = (Button) findViewById(R.id.fc_b_anr);

        b_pingTest.setOnClickListener(this);
        b_explode.setOnClickListener(this);
        b_anr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fc_b_ping:
                goto_PT();
                break;
            case R.id.fc_b_explode:
                explode();
                break;
            case R.id.fc_b_anr:
                anr();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void explode() {
        String x = null;
        if (x.equals("")) {
            // explode
        }
    }

    private void anr() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {

        }
    }

    private void goto_PT() {
        Intent intent = new Intent(FC_Functions.this, PT_PingTest.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
