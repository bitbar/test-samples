package com.testdroid.sample.android;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import utils.Constants;
import utils.Helpers;

/**
 * @author Saad Chaudhry <saad.chaudry@bitbar.com>
 */
public class WE_Wear extends Activity implements View.OnClickListener {

    private static final String TAG = WE_Wear.class.getName().toString();

    // UI Widgets
    private static Button b_notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.we_layout);

        b_notify = (Button) findViewById(R.id.we_b_notify);

        b_notify.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.we_b_notify:
                generateNotification();
                break;
        }
    }

    private void generateNotification() {
        // Helpers.toastDefault(getApplicationContext(), "TODO - Generate notification", Toast.LENGTH_SHORT);

        NotificationCompat.Builder notificationBuilderCompat = new NotificationCompat.Builder(WE_Wear.this);
        notificationBuilderCompat.setSmallIcon(R.drawable.ic_launcher);
        notificationBuilderCompat.setContentTitle("Testdroid");
        notificationBuilderCompat.setContentText("Got 350+ devices?");
        notificationBuilderCompat.setVibrate(Constants.VIBRATE_NOTIFICATION);
        notificationBuilderCompat.setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(WE_Wear.this);

        notificationManagerCompat.notify(1, notificationBuilderCompat.build());

    }


}
