/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<CurrentRoute> currentRoute;

    public MyViewModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);
        generateNewRoute("Initial");

        nodeDataToDisplay = mRepository.getNode();
        route = mRepository.getRouteFromId("1");
        currentRoute = new MutableLiveData<CurrentRoute>(new CurrentRoute("1"));
        rwN_list = mRepository.getRoutesWithNodes();
        barometer= new Barometer(application);
        accelerometer = new Accelerometer(application, barometer);
        thermometer = new Thermometer(application);
    }

    public void setCurrentRoute(String id) {
        currentRoute.postValue(new CurrentRoute(id));
    }

    public MutableLiveData<CurrentRoute> getCurrentRoute() {
        if (currentRoute == null) {
            currentRoute = new MutableLiveData<CurrentRoute>();
        }
        return currentRoute;
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

    LiveData<Route> getRouteFromId(){
        if (route == null) {
            route = new MutableLiveData<Route>();
        }
        Log.d("werwerwerw", route.toString());
        return route;
    }

    public void generateNewRoute(String title) {
        mRepository.generateNewRoute(title);
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


    public class CurrentRoute extends Observable {
        private LiveData<Route> route;
        private String id;

        public CurrentRoute(String id){
            this.id = id;
            this.route = mRepository.getRouteFromId(id);
        }

        public LiveData<Route> getRoute(){ return route; }
        public String getId(){ return id; }
        public void setId(String id){
            this.id = id;
            this.route = mRepository.getRouteFromId(id);
        }

    }
}
