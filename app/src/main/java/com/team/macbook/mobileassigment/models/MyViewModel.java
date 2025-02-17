/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.models;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.team.macbook.mobileassigment.R;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.database.RouteWithNodes;
import com.team.macbook.mobileassigment.sensors.Barometer;
import com.team.macbook.mobileassigment.sensors.Thermometer;

import java.util.List;


public class MyViewModel extends AndroidViewModel {
    private final MyRepository mRepository;

    LiveData<Node> nodeDataToDisplay;
    LiveData<List<RouteWithNodes>> rwN_list;

    LiveData<Route> route;
    private Barometer barometer;

    private Thermometer thermometer;
    private boolean started = false;
    private MutableLiveData<String> text;
    private MutableLiveData<CompleteRoute> cr = new MutableLiveData<>();
    private MutableLiveData<Node> currentViewed = new MutableLiveData<>();
    private MutableLiveData<CompleteRoute> currentViewedRoute = new MutableLiveData<>();
    private MutableLiveData<String> currentViewedImage = new MutableLiveData<>();
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


    /**
     * getter for the live data
     * @return
     */


    public void generateNewRoute(String id, String title) {

        mRepository.generateNewRoute(id, title);
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

    public LiveData<Node> getViewItemSingle() {
        if (currentViewed == null) {
            currentViewed = new MutableLiveData<Node>();
        }
        return currentViewed;
    }

    public void setViewItemSingle(Node node) {
        currentViewed.postValue(node);
    }

    public LiveData<CompleteRoute> getViewRouteSingle() {
        if (currentViewedRoute == null) {
            currentViewedRoute = new MutableLiveData<CompleteRoute>();
        }
        return currentViewedRoute;
    }

    public void setViewImage(String image) {
        currentViewedImage.postValue(image);
    }

    public LiveData<String> getViewImage() {
        if (currentViewedImage == null) {
            currentViewedImage = new MutableLiveData<String>();
        }
        return currentViewedImage;
    }


    public void setViewRouteSingle(CompleteRoute route) {
        currentViewedRoute.postValue(route);
    }

    public LiveData<Route> getRouteFromId(String id){
        return mRepository.getRouteFromId(id);

    }

    public LiveData<CompleteRoute> getCompleteRouteFromId(String id){
        return mRepository.getCompleteRouteFromId(id);

    }

}
