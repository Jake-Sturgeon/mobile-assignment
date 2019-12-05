/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.RouteWithNodes;

import java.util.List;


public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<Node> nodeDataToDisplay;
    LiveData<List<RouteWithNodes>> rwN_list;
    private Barometer barometer;
    private Accelerometer accelerometer;
    private Thermometer thermometer;
    private boolean started = false;
    private MutableLiveData<String> text;


    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        generateNewRoute();

        nodeDataToDisplay = mRepository.getNode();
        rwN_list = mRepository.getRoutesWithNodes();
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
    LiveData<Node> getNodeToDisplay() {
        if (nodeDataToDisplay == null) {
            nodeDataToDisplay = new MutableLiveData<Node>();
        }
        return nodeDataToDisplay;
    }

    LiveData<List<RouteWithNodes>> getListRwN(){
        if (rwN_list == null) {
            rwN_list = new MutableLiveData<List<RouteWithNodes>>();
        }
        return rwN_list;
    }

    public void generateNewRoute() {
        mRepository.generateNewRoute();
    }

    public void generateNewNode() {
        mRepository.generateNewNode();
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
