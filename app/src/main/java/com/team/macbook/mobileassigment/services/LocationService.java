package com.team.macbook.mobileassigment.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.LocationResult;
import com.team.macbook.mobileassigment.activities.MapsActivity;
import com.team.macbook.mobileassigment.models.MyMapModel;

import java.text.DateFormat;
import java.util.Date;

public class LocationService extends IntentService {
    private static final String TAG = "INTENTS";
    private Location mCurrentLocation;
    private String mLastUpdateTime;

    private MyMapModel myMapModel;


    @Override
    public void onCreate() {
        super.onCreate();
        myMapModel = ViewModelProviders.of(MapsActivity.getActivity()).get(MyMapModel.class);
    }

    public LocationService() {
        super("Location Intent");
    }

    /**
     * called when a location is recognised
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locResults = LocationResult.extractResult(intent);
            if (locResults != null) {
                for (Location location : locResults.getLocations()) {
                    if (location == null) continue;
                    //do something with the location


                    Log.i("New Location", "Current location: " + location);
                    String currentRoute = myMapModel.retrieveRecentRouteId();
                    Log.i("New Location", "Current Route: " + currentRoute);
                    mCurrentLocation = location;
                    mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                    Log.i("MAP", "new location Intent " + mCurrentLocation.toString());
                    Log.i(LocationService.class.getName(),"this is working");

                    Intent in = new Intent(LocationService.class.getName());
                    Bundle extras = new Bundle();
                    extras.putDouble("lat", mCurrentLocation.getLatitude());
                    extras.putDouble("long", mCurrentLocation.getLongitude());
                    in.putExtras(extras);
                    getApplicationContext().sendBroadcast(in);
                    myMapModel.generateNewEdge(myMapModel.retrieveRecentRouteId(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }
            }

        }
    }
}

