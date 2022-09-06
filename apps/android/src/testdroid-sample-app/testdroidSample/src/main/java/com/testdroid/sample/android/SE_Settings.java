package com.testdroid.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class SE_Settings extends Activity implements View.OnClickListener {

    // UI Widgets
    private static Button b_deleteCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.se_layout);

        b_deleteCache = (Button) findViewById(R.id.se_b_deleteCache);

        b_deleteCache.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.se_b_deleteCache:
                deleteCache();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void deleteCache() {
        try {
            File dir = getApplicationContext().getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
                Helpers.toastDefault(getApplicationContext(), "Cache deleted", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            Helpers.toastDefault(getApplicationContext(), "Error deleting cache", Toast.LENGTH_SHORT);
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
