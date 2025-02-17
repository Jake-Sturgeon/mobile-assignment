package com.team.macbook.mobileassigment.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.R.drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.team.macbook.mobileassigment.activities.MapsActivity;
import com.team.macbook.mobileassigment.models.MyMapModel;
import com.team.macbook.mobileassigment.sensors.Barometer;
import com.team.macbook.mobileassigment.sensors.Thermometer;

import static android.content.ContentValues.TAG;

/**
 * Creates a ForegroundService service that provides a notification for the user. This allows the
 * user to remove the app from the apps tray and return to the correct route
 */
public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";


    private static AppCompatActivity activity;
    private static GoogleMap mMap;
    private static final int ACCESS_FINE_LOCATION = 123;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private PendingIntent mLocationPendingIntent;
    private static final float SMALLEST_DISPLACEMENT = 0.5F;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private String currentRoute;


    private Thermometer temp;
    private Barometer bar;



    private Location mLocation;
    private Polyline line;

    public MyMapModel getMyMapModel() {
        return myMapModel;
    }

    private MyMapModel myMapModel;
    private LocationCallback mLocationCallback;
    private NotificationManager mNotificationManager;
    private Handler mServiceHandler;


    /**
     * Create the fused listeneer
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        myMapModel = ViewModelProviders.of(MapsActivity.getActivity()).get(MyMapModel.class);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();
        requestLocationUpdates();


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


    }


    /**
     *
     * When the intent is called it builds the notification and a ForegroundService service
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MapsActivity.class);
        Log.d("asdasda----------",  myMapModel.getCurrentID());
        notificationIntent.putExtra("current_route", String.valueOf(myMapModel.getCurrentID()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Mobile Assignment")
                .setContentText(input)
                .setSmallIcon(drawable.ic_menu_myplaces)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    /**
     * Gets the last location given by the service
     */
    public void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }
    private static void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);



        // Notify anyone listening for broadcasts about the new location.

    }

    /**
     * Used to set up the pending intent for the location service
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");

        Intent intent = new Intent(this, LocationService.class);
        mLocationPendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Void> locationTask = mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationPendingIntent);
            if (locationTask != null) {
                locationTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            Log.w("MapsActivity", ((ApiException) e).getStatusMessage());
                        } else {
                            Log.w("MapsActivity", e.getMessage());
                        }
                    }
                });

                locationTask.addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("MapsActivity", "restarting gps successful!");
                    }
                });


            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Used to destroy the service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     * Binds the intent
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
