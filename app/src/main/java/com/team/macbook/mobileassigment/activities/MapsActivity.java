package com.team.macbook.mobileassigment.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.macbook.mobileassigment.R;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Edge;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.models.MyMapModel;
import com.team.macbook.mobileassigment.sensors.Barometer;
import com.team.macbook.mobileassigment.sensors.Thermometer;
import com.team.macbook.mobileassigment.services.ForegroundService;
import com.team.macbook.mobileassigment.services.LocationService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Maps activity runs the map activity
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private static AppCompatActivity activity;
    private static GoogleMap mMap;
    private static final int ACCESS_FINE_LOCATION = 123;
    private static final int CAMERA_REQUEST_CODE = 7500;

    private Thermometer temp;
    private Barometer bar;


    private String currentRoute;
    private Polyline line;

    private MyMapModel myMapModel;

    private BroadcastReceiver receiver;

    private Map<Marker, Node> nodesGetter = new HashMap<>();


    /**
     *
     * When a moment is taken this function is called.
     *
     * This function generates a new node, which contains the sensor readings, locations, and
     * images
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this,
                new DefaultCallback() {
                    @Override
                    public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source,
                                               int type) {
                        hidePopup();
                        Log.d("Images", "Image picked: "+imageFiles.get(0).toString());

                        new createIcon().execute(imageFiles.get(0));

                        String apath = imageFiles.get(0).getAbsolutePath();

                        final String newPath = apath.substring(0,apath.length()-2)+"_icon.jpg";

                        final String imageS = imageFiles.get(0).toString();

                        // Got last known location. In some rare situations this can be null.
                        if (lat != 100 && longi != 200) {
                            LatLng loc = new LatLng(lat, longi);


                            float t = temp.getTemp();
                            float b = bar.getPressureValue();
                            Log.i("Location", "Image loc: " + loc.toString());
                            Log.i("Location", "Temp: " + t);
                            Log.i("Location", "Bar: " + b);

                            myMapModel.generateNewNode(currentRoute, lat, longi, imageS, newPath, t, b);
                        }


                    }
                });
    }


    /**
     * @return AppCompatActivity activity
     */
    public static AppCompatActivity getActivity() {
        return activity;
    }

    /**
     * @param activity
     */
    public static void setActivity(AppCompatActivity activity) {
        MapsActivity.activity = activity;
    }

    /**
     * @return GoogleMap mMap
     */
    public static GoogleMap getMap() {
        return mMap;
    }

    private void initEasyImage() {
        EasyImage.configuration(this)
                .setImagesFolderName("EasyImage sample")
                // it adds new pictures to the gallery
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                // it allows to select multiple pictures in the gallery
                .setAllowMultiplePickInGallery(true);
    }


    /**
     * Removes take moment popup
     */
    public void hidePopup(){
        ConstraintLayout layout = findViewById(R.id.mapsLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.TOP, R.id.mapsLayout,ConstraintSet.BOTTOM,0);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.BOTTOM,R.id.mapsLayout,ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(layout);
    }

    /**
     * Opens the camera if available otherwise the library
     */
    public void takePhoto() {
        if (haveCamera()) {
            EasyImage.openCamera(this, 0);
        } else {
            requestCamera();
        }


    }

    /**
     * opens the gallery
     */
    public void choosePhoto() {
        EasyImage.openGallery(this, 0);
    }

    private boolean haveCamera() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(MapsActivity.this, permissions, CAMERA_REQUEST_CODE);
    }


    /**
     * opens the create moment tray
     *
     * @param i
     */
    public void openCreateMoment(MenuItem i) {
        ConstraintLayout layout = findViewById(R.id.mapsLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.BOTTOM,R.id.bottom_navigation,ConstraintSet.TOP,0);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.TOP,R.id.mapsLayout,ConstraintSet.TOP,0);
        constraintSet.applyTo(layout);

    }

    private double lat = 100;
    private double longi = 200;

    /**
     * Starts location tracking and changes buttons
     *
     * @param i
     */
    public void enableUpdates(MenuItem i) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Mobile Assignment is tracking your location for your current route.");
        ContextCompat.startForegroundService(this, serviceIntent);
        receiver = new BroadcastReceiver() {
            private static final String TAG = "MyBroadcastReceiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                lat = intent.getDoubleExtra("lat", lat);
                longi = intent.getDoubleExtra("long", longi);
                Log.d("Broadcast Listener", "Updated lat long");
            }
        };

        this.registerReceiver(receiver, new IntentFilter(LocationService.class.getName()));

        i.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        i.setTitle(R.string.map_menu_2);
        i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                disableUpdates(item);
                return true;
            }
        });
    }

    /**
     * Starts location tracking
     */
    public void enableUpdates() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Mobile Assignment is tracking your location for your current route.");
        ContextCompat.startForegroundService(this, serviceIntent);
        receiver = new BroadcastReceiver() {
            private static final String TAG = "MyBroadcastReceiver";

            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                lat = intent.getDoubleExtra("lat", lat);
                longi = intent.getDoubleExtra("long", longi);
                Log.d("Broadcast Listener", "Updated lat long");
            }
        };

        this.registerReceiver(receiver, new IntentFilter(LocationService.class.getName()));
    }

    /**
     *
     * Disables tracking and sensors
     *
     * @param i
     */
    public void disableUpdates(MenuItem i) {

        bar.stopBarometer();
        temp.stopThermometer();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        i.setIcon(android.R.drawable.ic_menu_compass);
        i.setTitle(R.string.map_menu_1);
        i.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                enableUpdates(item);
                return true;
            }
        });
    }

    /**
     * Disables tracking and sensors
     */
    public void disableUpdates() {

        bar.stopBarometer();
        temp.stopThermometer();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    /**
     *
     * Creates all the observers, buttons, sensors, and map listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        Intent intent = getIntent();
        currentRoute = intent.getStringExtra("current_route");
        myMapModel = ViewModelProviders.of(this).get(MyMapModel.class);
        myMapModel.setCurrent(currentRoute);
        Log.d("Current_Route", currentRoute);

        initEasyImage();
        setContentView(R.layout.activity_maps);
        setActivity(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab_photo = (FloatingActionButton) findViewById(R.id.photo);
        fab_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        FloatingActionButton fab_gal = (FloatingActionButton) findViewById(R.id.gallery);
        fab_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        temp = new Thermometer(getApplicationContext());

        bar = new Barometer(getApplicationContext());

        initLocations();

        myMapModel.getCRID().observe(this, new Observer<CompleteRoute>() {
            @Override
            public void onChanged(@Nullable final CompleteRoute newValue) {
                if (newValue != null) {
                    nodesGetter.clear();
                    Log.i("EDGE M", newValue.toString());
                    Log.i("EDGE M", newValue.edges.toString());

                    Log.i("NODES", String.valueOf(newValue.nodes.size()));
                    for(Edge edge : newValue.edges){
                        Log.i("EDGE M", edge.latitude+ " " + edge.longitude);
                    }
                    if (line != null) {
                        mMap.clear();
                    }
                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
                    for (Edge edge : newValue.edges) {
                        LatLng point = new LatLng(edge.longitude, edge.latitude);
                        options.add(point);
                    }
                    for (Node node : newValue.nodes) {
                        LatLng point = new LatLng(node.getLatitude(), node.getLongitude());
                        Marker marker = mMap.addMarker(new MarkerOptions().title(" ").position(point).snippet("Temp: " + node.getTemp() + "\nPressure: " + node.getBar()));
                        nodesGetter.put(marker, node);
                    }
                    line = mMap.addPolyline(options);
                    if (options.getPoints().size() > 0) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(options.getPoints().get(options.getPoints().size() - 1), 15.0f));
                    }
                }
            }
        });
        myMapModel.setCR(currentRoute, getActivity());


    }


    private void initLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
        } else {
            enableUpdates();
        }
    }


    /**
     * Finishes the activity
     *
     * @param i
     */
    public void finishActivity(MenuItem i){
        disableUpdates();
        finish();
    }

    /**
     * Resumes activity
     */
    @Override
    protected void onResume() {
        super.onResume();


        bar.startSensingPressure();
        temp.startThermometerRecording();


    }


    private Location mCurrentLocation;
    private String mLastUpdateTime;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.i("MAP", "new location " + mCurrentLocation.toString());
        }
    };

    /**
     * Pauses activity
     */
    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     *
     * Get permissions from user
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    enableUpdates();
//                    startLocationUpdates(getApplicationContext());
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openCamera(this, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "No Camera Found", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
    }

    /**
     * Save route if back button is pressed
     */
    @Override
    public void onBackPressed() {
        disableUpdates();
        finish();
    }


    private static class createIcon extends AsyncTask<File, Void, Void> {
        createIcon() {
        }
        @Override
        protected Void doInBackground(final File... params) {

            Bitmap myBitmap = BitmapFactory.decodeFile(params[0].toString());

            int width = myBitmap.getWidth();
            int height = myBitmap.getHeight();

            double scale;


            scale = 300.0/height;

            Log.d("Width", ""+width);
            Log.d("Height", ""+height);
            Log.d("Width", ""+width*scale);
            Log.d("Height", ""+height*scale);

            Bitmap icon = Bitmap.createScaledBitmap(myBitmap, (int)(myBitmap.getWidth()*scale), (int)(myBitmap.getHeight()*scale), true);

            String apath = params[0].getAbsolutePath();

            final String newPath = apath.substring(0,apath.length()-2)+"_icon.jpg";

            File file = new File(newPath);

            try{
                FileOutputStream outputStream = new FileOutputStream(file);
                icon.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            } catch (FileNotFoundException e){
                Log.d("1", "No file found");
            }
            return null;
        }
    }



    /**
     *
     * Required for Marker popups
     *
     * @param marker
     * @return null
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     *
     * Set content for each Marker
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        try {
            Node element = nodesGetter.get(marker);
            final View view = (getActivity()).getLayoutInflater()
                    .inflate(R.layout.fragment_single_image, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);

            TextView dateTextView = (TextView)  view.findViewById(R.id.singleImageDate);
            dateTextView.setText(String.valueOf(new Date(element.getTime())));
            TextView pressTextView = (TextView)  view.findViewById(R.id.singleImagePressure);
            pressTextView.setText(element.getBar()+"");
            TextView tempTextView = (TextView)  view.findViewById(R.id.singleImageTemp);
            tempTextView.setText(element.getTemp()+"");

            Bitmap myBitmap = BitmapFactory.decodeFile(element.getIcon_id());
            imageView.setImageBitmap(myBitmap);
            myMapModel.getRouteFromId(element.getRoute_id()).observe(getActivity(), new Observer<Route>() {
                @Override
                public void onChanged(Route s) {
                    TextView titleTextView = (TextView) view.findViewById(R.id.singleImageTitle);
                    titleTextView.setText(s.getTitle());
                }
            });
            return view;
        } catch (NullPointerException e) {
            final View view = (getActivity()).getLayoutInflater()
                    .inflate(R.layout.fragment_single_image, null);
            return view;
        }

    }
}
