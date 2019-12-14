package com.driver.porttrucker.Service;

import android.Manifest;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.driver.porttrucker.Common.Common;
import com.driver.porttrucker.Common.Constant;

import java.util.Timer;
import java.util.TimerTask;

public class GpsService extends Service {
    final Handler mHandler = new Handler();
    Timer mTimer = null;

    public LocationManager myLocationManager;
    public boolean w_bGpsEnabled, w_bNetworkEnabled;

    public static double myLat = 0;
    public static double myLng = 0;

    public static DevicePolicyManager mDPM;
    public static ComponentName mAdminName;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Initiate DevicePolicyManager.
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // Set DeviceAdminDemo Receiver for active the component with different option
        mAdminName = new ComponentName(this, DeviceAdminDemo.class);

        initLocationListener();

        // scheduling the current position updating task (Asynchronous)
        mTimer = new Timer();
        MyTimeTask myTimeTask = new MyTimeTask();
        mTimer.schedule(myTimeTask, 0, Constant.LOCATION_UPDATE_TIME);

        return START_STICKY; //super.onStartCommand(intent, flags, startId);
    }

    public void initLocationListener() {

        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean w_bGpsEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean w_bNetworkEnabled = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (w_bGpsEnabled || w_bNetworkEnabled) {
            tryGetLocation();
        } else {
            setMyLocation(null);
        }
    }

    private void tryGetLocation() {

        w_bGpsEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        w_bNetworkEnabled = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (w_bNetworkEnabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location locationNet = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationNet != null) {
                setMyLocation(locationNet);
            }

        }
        if (w_bGpsEnabled) {

            Location locationGps = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGps != null) {
                setMyLocation(locationGps);
            }
        }

        if (w_bNetworkEnabled) {

            myLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000, 0, m_myLocationListener);

        }
        if (w_bGpsEnabled) {
            myLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 0, m_myLocationListener);
        }
    }

    private void setMyLocation(Location p_location) {

        if (p_location != null) {
            myLat = p_location.getLatitude();
            myLng = p_location.getLongitude();
        }
    }

    LocationListener m_myLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onLocationChanged(Location location) {
            setMyLocation(location);
        }
    };

    private class MyTimeTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String lat = String.valueOf(myLat);
                    String lng = String.valueOf(myLng);

                    Common.lat = myLat;
                    Common.lng = myLng;
                }
            });
        }
    }
}
