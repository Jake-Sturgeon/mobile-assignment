package com.team.macbook.mobileassigment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Edge;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private static AppCompatActivity activity;
    private static GoogleMap mMap;
    private static final int ACCESS_FINE_LOCATION = 123;
    private static final int CAMERA_REQUEST_CODE = 7500;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private PendingIntent mLocationPendingIntent;
    private static final float SMALLEST_DISPLACEMENT = 0.5F;


    private Thermometer temp;
    private Barometer bar;
    private Accelerometer acc;


    private String currentRoute;
    private Polyline line;

    private MyMapModel myMapModel;
    private Map<Marker, Node> nodesGetter = new HashMap<>();



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
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());


                                            float t = temp.getTemp();
                                            float b = bar.getPressureValue();
                                            Log.i("Location", "Image loc: " + loc.toString());
                                            Log.i("Location", "Temp: " + t);
                                            Log.i("Location", "Bar: " + b);


//                                            mMap.addMarker(new MarkerOptions().position(loc)
//                                                    .title("Sensor Reading").snippet("Temp: " + t + "\nPressure: " + b));
                                            myMapModel.generateNewNode(currentRoute, location.getLatitude(), location.getLongitude(), imageS, newPath, t, b);
                                        }
                                    }
                                });

                    }
                });
    }


    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setActivity(AppCompatActivity activity) {
        MapsActivity.activity = activity;
    }

    public static GoogleMap getMap() {
        return mMap;
    }

    private void initEasyImage() {
        EasyImage.configuration(this)
                .setImagesFolderName("EasyImage sample")
// it adds new pictures to the gallery
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
// probably unnecessary
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
// it allows to select multiple pictures in the gallery
                .setAllowMultiplePickInGallery(true);
    }


    public void hidePopup(){
        ConstraintLayout layout = findViewById(R.id.mapsLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.TOP, R.id.mapsLayout,ConstraintSet.BOTTOM,0);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.BOTTOM,R.id.mapsLayout,ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(layout);
    }

    public void takePhoto() {
        if (haveCamera()) {
            EasyImage.openCamera(this, 0);
        } else {
            requestCamera();
        }


    }

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


    public void openCreateMoment(MenuItem i) {
        ConstraintLayout layout = findViewById(R.id.mapsLayout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.BOTTOM,R.id.bottom_navigation,ConstraintSet.TOP,0);
        constraintSet.connect(R.id.myRectangleView,ConstraintSet.TOP,R.id.mapsLayout,ConstraintSet.TOP,0);
        constraintSet.applyTo(layout);

    }

    public void enableUpdates(MenuItem i) {
        startLocationUpdates(getApplicationContext());
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

    public void disableUpdates(MenuItem i) {
        acc.stopAccelerometer();
        bar.stopBarometer();
        temp.stopThermometer();
        stopLocationUpdates();
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

        acc = new Accelerometer(getApplicationContext(), bar);


        initLocations();

        LiveData<FusedLocationProviderClient> ld = new MutableLiveData<FusedLocationProviderClient>(mFusedLocationClient);

        ld.observe(this, new Observer<FusedLocationProviderClient>() {
            @Override
            public void onChanged(@Nullable final FusedLocationProviderClient newValue) {
                if (newValue != null) {
                    startLocationUpdates(getApplicationContext());
                }
            }
        });


//
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
        }
    }

    public void startLocationUpdates(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        mLocationPendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    /**
     * it stops the location updates
     */
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mFusedLocationClient.removeLocationUpdates(mLocationPendingIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();


        bar.startSensingPressure();
        temp.startThermometerRecording();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    @Override
    protected void onPause() {
        super.onPause();
    }


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
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, null /* Looper */);
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

    @Override
    public void onBackPressed() {
        stopLocationUpdates();
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


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
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
//                    if (element.nodes.get(0).getPicture_id() != -1) {
//
//                    }
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
    }
}
