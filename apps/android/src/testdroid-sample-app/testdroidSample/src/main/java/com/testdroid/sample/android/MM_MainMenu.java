package com.testdroid.sample.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class MM_MainMenu extends Activity implements View.OnClickListener {

    // UI Widgets
    private static TextView tv_version;
    private static ImageView iv_title;
    private static Button b_native;
    private static Button b_hybrid;
    private static Button b_game;
    private static Button b_wear;
    private static Button b_functions;
    private static Button b_deviceInfo;
    private static Button b_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.mm_layout);

        tv_version = (TextView) findViewById(R.id.mm_tv_version);
        iv_title = (ImageView) findViewById(R.id.mm_iv_title);
        b_native = (Button) findViewById(R.id.mm_b_native);
        b_hybrid = (Button) findViewById(R.id.mm_b_hybrid);
        b_game = (Button) findViewById(R.id.mm_b_game);
        b_wear = (Button) findViewById(R.id.mm_b_wear);
        b_functions = (Button) findViewById(R.id.mm_b_functions);
        b_deviceInfo = (Button) findViewById(R.id.mm_b_deviceInfo);
        b_settings = (Button) findViewById(R.id.mm_b_settings);


        iv_title.setOnClickListener(this);
        b_native.setOnClickListener(this);
        b_hybrid.setOnClickListener(this);
        b_game.setOnClickListener(this);
        b_wear.setOnClickListener(this);
        b_functions.setOnClickListener(this);
        b_deviceInfo.setOnClickListener(this);
        b_settings.setOnClickListener(this);

        tv_version.setText(String.format(getString(R.string.app_version), Helpers.getAppVersion(getApplicationContext())));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            goto_AB();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mm_iv_title:
                goto_AB();
                break;
            case R.id.mm_b_native:
                goto_NA();
                break;
            case R.id.mm_b_hybrid:
                goto_HY();
                break;
            case R.id.mm_b_game:
                goto_GA();
                break;
            case R.id.mm_b_wear:
                goto_WE();
                break;
            case R.id.mm_b_functions:
                goto_FC();
                break;
            case R.id.mm_b_deviceInfo:
                goto_DI();
                break;
            case R.id.mm_b_settings:
                goto_SE();
                break;
        }

    }

    private void goto_AB() {
        Intent intent = new Intent(MM_MainMenu.this, AB_About.class);
        startActivity(intent);
    }

    private void goto_NA() {
        Intent intent = new Intent(MM_MainMenu.this, NA_Native.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_HY() {
        Intent intent = new Intent(MM_MainMenu.this, HY_Hybrid.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_GA() {
        Intent intent = new Intent(MM_MainMenu.this, GA_Game.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_WE() {
        Intent intent = new Intent(MM_MainMenu.this, WE_Wear.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_FC() {
        Intent intent = new Intent(MM_MainMenu.this, FC_Functions.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_DI() {
        Intent intent = new Intent(MM_MainMenu.this, DI_DeviceInfo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void goto_SE() {
        Intent intent = new Intent(MM_MainMenu.this, SE_Settings.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
