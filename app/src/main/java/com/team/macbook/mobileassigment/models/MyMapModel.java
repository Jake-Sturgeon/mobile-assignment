package com.team.macbook.mobileassigment.models;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Route;

/**
 * MapModel viewmodel
 */
public class MyMapModel extends AndroidViewModel {
    private final MyRepository mRepository;
    private String current;

    private MutableLiveData<CompleteRoute> cr = new MutableLiveData<>();

    /**
     *
     * Creates the viewmodel
     *
     * @param application
     */
    public MyMapModel (Application application) {
        super(application);
        // creation and connection to the Repository
        mRepository = new MyRepository(application);

    }

    /**
     * sets the current route observer
     *
     * @param id
     * @param thread
     */
    public void setCR(String id, LifecycleOwner thread) {
        LiveData<CompleteRoute> temp = mRepository.getCompleteRouteFromId(id);
        temp.observe(thread, new Observer<CompleteRoute>() {
            @Override
            public void onChanged(@Nullable final CompleteRoute newValue) {
                cr.postValue(newValue);
            }
        });

    }

    /**
     *
     * Generates a new node
     *
     *
     * @param currentRoute
     * @param lat
     * @param lon
     * @param picture
     * @param icon
     * @param temp
     * @param bar
     */
    public void generateNewNode(String currentRoute, double lat, double lon, String picture, String icon, float temp, float bar) {
        mRepository.generateNewNode(currentRoute, lat, lon, picture, icon, temp, bar);
    }

    /**
     *
     * Generates a new edge
     *
     * @param currentRoute
     * @param lat
     * @param lon
     */
    public void generateNewEdge(String currentRoute ,double lat, double lon) {
        mRepository.generateNewEdge(currentRoute, lat, lon);
    }

    public LiveData<CompleteRoute> getCRID() {
        if (cr == null) {
            cr =  new MutableLiveData<CompleteRoute>();
        }
        return cr;
    }

    /**
     * get current id
     *
     * @return
     */
    public String getCurrentID(){
        return current;
    }

    public void setCurrent(String c){
        this.current = c;
    }

    /**
     *
     * Returns the most resent route
     *
     * @return
     */
    public String retrieveRecentRouteId() {
        return mRepository.retrieveRecentRouteId();
    }

    /**
     *
     * Gets route from a given id
     *
     * @param id
     * @return
     */
    public LiveData<Route> getRouteFromId(String id){
        return mRepository.getRouteFromId(id);


    }

}