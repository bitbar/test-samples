package com.testdroid.sample.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Activity which can open text file from external storage.
 *
 * @author Jarno Tuovinen <jarno.tuovinen@bitbar.com>
 */
public class ES_ExternalStorage extends Activity implements View.OnClickListener {

    private static final String TAG = ES_ExternalStorage.class.getName();

    private static final String DEFAULT_FILE = "/sdcard/datadir/datafile.txt";

    private InputMethodManager inputMethodManager;

    // UI Widgets
    private static EditText et_file;
    private static EditText et_error;
    private static Button b_read;
    private static TextView tv_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.es_layout);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        et_file = (EditText) findViewById(R.id.es_et_file);
        et_error = (EditText) findViewById(R.id.es_et_error);
        b_read = (Button) findViewById(R.id.es_b_read);
        tv_output = (TextView) findViewById(R.id.es_tv_output);

        et_file.setText(DEFAULT_FILE);

        b_read.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        switch (view.getId()) {
            case R.id.es_b_read:
                clearError();
                read();
                break;
        }
    }

    private void read() {
        String fileStr = et_file.getText().toString();
        Log.d(TAG, String.format("Read file '%s'", fileStr));
        File file = new File(fileStr);
        String buffer = "";

        try {
            FileInputStream fs = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(fs));
            String row = "";
            while ((row = reader.readLine()) != null) {
                buffer += row + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            String msg = "File not found";
            Log.e(TAG, msg);
            setError(msg);
        } catch (IOException e) {
            setError(e.getMessage());
            Log.e(TAG, "Read error", e);
        }

        tv_output.setText(buffer);
    }

    void clearError() {
        et_error.setText("");
    }

    void setError(String error) {
        et_error.setText(error);
    }
}
