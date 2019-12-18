/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.database.RouteWithNodes;

import java.util.List;
import java.util.Observable;


public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<Node> nodeDataToDisplay;
    LiveData<List<RouteWithNodes>> rwN_list;

    LiveData<Route> route;
    private Barometer barometer;
    private Accelerometer accelerometer;
    private Thermometer thermometer;
    private boolean started = false;
    private MutableLiveData<String> text;
    private MutableLiveData<CompleteRoute> cr = new MutableLiveData<>();
    private LiveData<List<CompleteRoute>> a_cr;

    private MutableLiveData<Boolean> route_active = new MutableLiveData<Boolean>(false);

    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);

        nodeDataToDisplay = mRepository.getNode();
        route = mRepository.getRouteFromId("1");
        rwN_list = mRepository.getRoutesWithNodes();
        barometer= new Barometer(application);
        accelerometer = new Accelerometer(application, barometer);
        thermometer = new Thermometer(application);
        a_cr = mRepository.getAllCompleteRoutes();

    }

    public LiveData<Boolean> getRoute_active(){
        return route_active;
    }

    public void setRoute_active(boolean b){
        route_active.postValue(b);
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


    public void generateNewRoute(String title) {
        mRepository.generateNewRoute(title);
    }

    public void generateNewNode() {
        mRepository.generateNewNode(1, 1, 1);
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



    public void setCR(String id, LifecycleOwner thread) {
        LiveData<CompleteRoute> temp = mRepository.getCompleteRouteFromId(id);
        temp.observe(thread, new Observer<CompleteRoute>() {
            @Override
            public void onChanged(@Nullable final CompleteRoute newValue) {
                cr.postValue(newValue);
            }
        });

    }

    public MutableLiveData<CompleteRoute> getCR() {
        if (cr == null) {
            cr = new MutableLiveData<CompleteRoute>();
        }
        return cr;
    }

    public LiveData<List<CompleteRoute>> getAllCompleteRoutes() {
        if (a_cr == null) {
            a_cr = new MutableLiveData<List<CompleteRoute>>();
        }
        return a_cr;
    }

}
