package com.testdroid.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.testdroid.sample.android.game.GameView;

public class GA_Game extends Activity {

    // UI Widgets
    // private static TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GameView(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
