/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team.macbook.mobileassigment.database.NumberData;


public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<NumberData> numberDataToDisplay;
    private Barometer barometer;
    private Accelerometer accelerometer;
    private Thermometer thermometer;
    private boolean started = false;
    private MutableLiveData<String> text;


    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        numberDataToDisplay = mRepository.getNumberData();
        barometer= new Barometer(application);
        accelerometer = new Accelerometer(application, barometer);
        thermometer = new Thermometer(application);
    }

    public LiveData<String> getStartStop() {
        if (text == null) {
            text = new MutableLiveData<String>(getApplication().getString(R.string.stopped));
        }
        return text;
    }

    public void toggle(){
        if (started){
            pauseThermometer();
            pauseAccelerometer();
            pauseBarometer();
            started = false;
            text.setValue(getApplication().getString(R.string.stopped));
        } else {
            startThermometer();
            startAccelerometer();
            startBarometer();
            started = true;
            text.setValue(getApplication().getString(R.string.started));
        }
    }

    /**
     * getter for the live data
     * @return
     */
    LiveData<NumberData> getNumberDataToDisplay() {
        if (numberDataToDisplay == null) {
            numberDataToDisplay = new MutableLiveData<NumberData>();
        }
        return numberDataToDisplay;
    }



    /**
     * request by the UI to generate a new random number
     */
    public void generateNewNumber() {
        mRepository.generateNewNumber();
    }

    public void pauseBarometer(){
        barometer.stopBarometer();
    }

    public void startBarometer(){
        barometer.startSensingPressure(accelerometer);
    }

    public void pauseAccelerometer(){
        accelerometer.stopAccelerometer();
    }

    public void startThermometer(){
        thermometer.startThermometerRecording();
    }

    public void pauseThermometer(){
        thermometer.stopThermometer();
    }

    public void startAccelerometer(){
        accelerometer.startAccelerometerRecording();
    }
}
