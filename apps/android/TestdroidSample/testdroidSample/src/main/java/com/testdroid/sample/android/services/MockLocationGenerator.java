package com.testdroid.sample.android.services;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

public class MockLocationGenerator {

    private Context context;

    private LocationManager locationManager;

    public MockLocationGenerator(Context context) {

        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.addTestProvider(locationManager.NETWORK_PROVIDER, false, false, false, false, false, true, true, 0, 1);
        locationManager.addTestProvider(locationManager.GPS_PROVIDER, false, false, false, false, false, true, true, 0, 1);

    }

    public void enableMocking() {
        locationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void pushLocation(Location mockLocation) {

        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

        locationManager.setTestProviderLocation(mockLocation.getProvider(), mockLocation);

    }

    public void disableMocking() {
        locationManager.removeTestProvider(LocationManager.NETWORK_PROVIDER);
        locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
    }

}