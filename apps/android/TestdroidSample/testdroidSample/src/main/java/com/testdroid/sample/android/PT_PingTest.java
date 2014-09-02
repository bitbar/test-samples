package com.testdroid.sample.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import utils.Animations;
import utils.Constants;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class PT_PingTest extends Activity implements View.OnClickListener {

    private static final String TAG = PT_PingTest.class.getName().toString();

    private InputMethodManager inputMethodManager;

    // UI Widgets
    private static EditText et_host;
    private static EditText et_pingCount;
    private static ImageButton ib_pingCountIncrement;
    private static ImageButton ib_pingCountDecrement;
    private static Button b_ping;
    private static ProgressBar pb_pinging;
    private static TextView tv_status;
    private static TextView tv_min;
    private static TextView tv_avg;
    private static TextView tv_max;
    private static TextView tv_sd;
    private static TextView tv_output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pt_layout);

        inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        et_host = (EditText) findViewById(R.id.pt_et_host);
        et_pingCount = (EditText) findViewById(R.id.pt_et_pingCount);
        ib_pingCountIncrement = (ImageButton) findViewById(R.id.pt_ib_pingCountIncrement);
        ib_pingCountDecrement = (ImageButton) findViewById(R.id.pt_ib_pingCountDecrement);
        b_ping = (Button) findViewById(R.id.pt_b_ping);
        pb_pinging = (ProgressBar) findViewById(R.id.pt_pb_pinging);
        tv_status = (TextView) findViewById(R.id.pt_tv_status);
        tv_min = (TextView) findViewById(R.id.pt_tv_min);
        tv_avg = (TextView) findViewById(R.id.pt_tv_avg);
        tv_max = (TextView) findViewById(R.id.pt_tv_max);
        tv_sd = (TextView) findViewById(R.id.pt_tv_sd);
        tv_output = (TextView) findViewById(R.id.pt_tv_output);

        b_ping.setOnClickListener(this);
        ib_pingCountIncrement.setOnClickListener(this);
        ib_pingCountDecrement.setOnClickListener(this);

        et_host.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        et_pingCount.setInputType(InputType.TYPE_CLASS_NUMBER);

        // don't auto-pop keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // setting presets
        et_pingCount.setText("" + Constants.PING_COUNT_DEFAULT);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pt_b_ping:
                ping();
                break;
            case R.id.pt_ib_pingCountIncrement:
                pingCount(true);
                break;
            case R.id.pt_ib_pingCountDecrement:
                pingCount(false);
                break;
        }
    }

    private void ping() {
        inputMethodManager.hideSoftInputFromWindow(et_host.getWindowToken(), 0);

        String url = et_host.getText().toString();
        if (url != null && url.replace(" ", "").equals("")) {
            url = getResources().getString(R.string.pt_defaultHost);
            et_host.setText(url);
        }

        int count = Constants.PING_COUNT_DEFAULT;
        try {
            count = Integer.parseInt(et_pingCount.getText().toString());
            if (count < Constants.PING_COUNT_MIN) {
                count = Constants.PING_COUNT_MIN;
            } else if (count > Constants.PING_COUNT_MAX) {
                count = Constants.PING_COUNT_MAX;
            }
        } catch (Exception ignore) {
            ignore = null;
        }
        et_pingCount.setText("" + count);

        new Ping().execute(url);

    }

    private void pingCount(boolean increment) {
        int count;
        try {
            count = Integer.parseInt(et_pingCount.getText().toString());
        } catch (NumberFormatException e) {
            count = Constants.PING_COUNT_DEFAULT;
        }

        if (increment && count < Constants.PING_COUNT_MAX) {
            et_pingCount.setText("" + (count + 1));
        } else if (!increment && count > Constants.PING_COUNT_MIN) {
            et_pingCount.setText("" + (count - 1));
        }
    }

    private class Ping extends AsyncTask<String, String, Boolean> {

        String[] pingResult;
        String commandOutput;

        int count;

        @Override
        protected void onPreExecute() {
            b_ping.setVisibility(View.GONE);
            pb_pinging.setVisibility(View.VISIBLE);
            tv_status.setText(getResources().getString(R.string.pt_statusPinging));
            tv_output.setText("");
            tv_min.setText("0.0");
            tv_avg.setText("0.0");
            tv_max.setText("0.0");
            tv_sd.setText("0.0");

            count = Integer.parseInt(et_pingCount.getText().toString());
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                Runtime r = Runtime.getRuntime();
                Process p = r.exec(new String[]{"ping", "-c " + count, urls[0]});
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine;
                String lastInputLine = null;
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    stringBuilder.append(inputLine + "\n");
                    lastInputLine = inputLine;
                }
                in.close();

                this.commandOutput = stringBuilder.toString();

                pingResult = lastInputLine.split("=")[1].split(" ")[1].split("/");
                if (pingResult.length == 4) {
                    return true;
                }

            } catch (Exception e) {
                Log.d(TAG, "Ping failed." + e.toString());
                commandOutput = String.format("PING %s\nPing failed", urls[0]);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {

            b_ping.setVisibility(View.VISIBLE);
            pb_pinging.setVisibility(View.GONE);

            if (isSuccess) {
                tv_status.setText(getResources().getText(R.string.pt_statusSuccess));
                tv_min.setText(pingResult[0]);
                tv_avg.setText(pingResult[1]);
                tv_max.setText(pingResult[2]);
                tv_sd.setText(pingResult[3]);
            } else {
                tv_status.setText(getResources().getText(R.string.pt_statusFail));
            }

            tv_output.setText(commandOutput);
            tv_output.startAnimation(Animations.slideInTop());
        }

    }
}
