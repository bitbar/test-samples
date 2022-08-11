package com.testdroid.sample.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class FC_Functions extends Activity implements View.OnClickListener {

    private static final String TAG = FC_Functions.class.getName();

    // UI Widgets
    private static Button b_pingTest;
    private static Button b_location;
    private static Button b_explode;
    private static Button b_anr;
    private static Button b_externalStorage;

    private boolean isExplode = false;
    private boolean isAnr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fc_layout);

        b_pingTest = (Button) findViewById(R.id.fc_b_ping);
        b_location = (Button) findViewById(R.id.fc_b_location);
        b_explode = (Button) findViewById(R.id.fc_b_explode);
        b_anr = (Button) findViewById(R.id.fc_b_anr);
        b_externalStorage = (Button) findViewById(R.id.fc_b_externalStorage);

        b_pingTest.setOnClickListener(this);
        b_location.setOnClickListener(this);
        b_explode.setOnClickListener(this);
        b_anr.setOnClickListener(this);
        b_externalStorage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fc_b_ping:
                goto_PT();
                break;
            case R.id.fc_b_location:
                goto_LO();
                break;
            case R.id.fc_b_explode:
                explode();
                break;
            case R.id.fc_b_anr:
                anr();
                break;
            case R.id.fc_b_externalStorage:
                goto_ES();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void explode() {

        if (isExplode) {
            String x = null;
            if (x.equals("")) {
                // explode
            }
        } else {
            Helpers.toastWarning(getApplicationContext(), "Click again to Explode", Toast.LENGTH_SHORT);
            isExplode = true;
        }

        new Handler().postDelayed(() -> isExplode = false, 2000);
    }

    private void anr() {

        if (isAnr) {
            int x = 0;
            while (true) {
                x++;
                x--;
            }
        } else {
            Helpers.toastWarning(getApplicationContext(), "Click again for ANR", Toast.LENGTH_SHORT);
            isAnr = true;
        }

        new Handler().postDelayed(() -> isAnr = false, 2000);

    }

    private void goto_PT() {
        Intent intent = new Intent(FC_Functions.this, PT_PingTest.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_ES() {
        Intent intent = new Intent(FC_Functions.this, ES_ExternalStorage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_LO() {
        Log.w(TAG, "LO_Location.java missing");
        //Intent intent = new Intent(FC_Functions.this, LO_Location.class);
        //startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
