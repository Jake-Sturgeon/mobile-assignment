package com.team.macbook.mobileassigment;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    myMapModel.generateNewEdge(myMapModel.retrieveRecentRouteId(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }
            }

        }
    }
}

