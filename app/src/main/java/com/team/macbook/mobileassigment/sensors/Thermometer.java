/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;


public class Thermometer {
    private static final String TAG = Thermometer.class.getSimpleName();
    private SensorEventListener mTemperatureListener = null;
    private SensorManager mSensorManager;
    private Sensor mThermometerSensor;
    private long timePhoneWasLastRebooted = 0;
    private long THERMOMETER_READING_FREQUENCY= 20000;
    private long mSamplingRateNano;
    private long lastReportTime = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private float temp = 0;

    public Thermometer(Context context) {
        // http://androidforums.com/threads/how-to-get-time-of-last-system-boot.548661/
        timePhoneWasLastRebooted = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mThermometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSamplingRateNano = (long) (THERMOMETER_READING_FREQUENCY) * 1000000;
        initThermometerListener();
    }

    /**
     * it inits the listener and establishes the actions to take when a reading is available
     */
    private void initThermometerListener() {
        if (!standardThermometerAvailable()) {
            Log.d(TAG, "Standard Thermometer unavailable");
        } else {
            Log.d(TAG, "Using Thermometer");
            mTemperatureListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    long diff = event.timestamp - lastReportTime;
                    if (diff >= mSamplingRateNano) {
                        temp = event.values[0];
                        long actualTimeInMseconds = timePhoneWasLastRebooted + (long) (event.timestamp / 1000000.0);
                        int accuracy = event.accuracy;

                        Log.i(TAG, Utilities.mSecsToString(actualTimeInMseconds) + ": current temperature: " + temp + "with accuract: " + accuracy);
                        lastReportTime = event.timestamp;
                    }
                }


                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }
    }


    /**
     * it returns true if the sensor is available
     *
     * @return
     */
    public boolean standardThermometerAvailable() {
        return (mThermometerSensor != null);
    }

    /**
     * it starts the pressure monitoring
     */
    public void startThermometerRecording() {
        // if the sensor is null,then mSensorManager is null and we get a crash
        if (standardThermometerAvailable()) {
            Log.d(TAG, "starting listener");
            // THE THERMOMETER receives as frequency a predefined subset of timing
            // https://developer.android.com/reference/android/hardware/SensorManager
            mSensorManager.registerListener(mTemperatureListener, mThermometerSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.i(TAG, "thermometer unavailable or already active");
        }
    }


    /**
     * this stops the barometer
     */
    public void stopThermometer() {
        if (standardThermometerAvailable()) {
            Log.d(TAG, "Stopping listener");
            try {
                mSensorManager.unregisterListener(mTemperatureListener);
            } catch (Exception e) {
                // probably already unregistered
            }
        }
    }

    public float getTemp() {return temp;}

    public long getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(long lastReportTime) {
        this.lastReportTime = lastReportTime;
    }
}
