package com.team.macbook.mobileassigment.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team.macbook.mobileassigment.adapters.MyAdapter;
import com.team.macbook.mobileassigment.models.MyViewModel;
import com.team.macbook.mobileassigment.R;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Edge;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleImageFragment extends Fragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter  {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LiveData<Node> currentNode = new MutableLiveData<>();
    private Map<Marker, Node> nodesGetter = new HashMap<>();


    /**
     * Required empty constructor
     */
    public SingleImageFragment() {
        // Required empty public constructor
    }


    /**
     *
     * Creates views, viewmodels, adaptors, and buttons
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_single_image, container, false);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        mAdapter = new MyAdapter(new ArrayList<CompleteRoute>(), myViewModel);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        currentNode = myViewModel.getViewItemSingle();

        currentNode.observe(this, new Observer<Node>(){
            @Override
            public void onChanged(@Nullable final Node element) {
                if (element != null) {

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
                    Bitmap myBitmap = BitmapFactory.decodeFile(element.getPicture_id());
                    imageView.setImageBitmap(myBitmap);
                    myViewModel.getRouteFromId(element.getRoute_id()).observe(getActivity(), new Observer<Route>() {
                        @Override
                        public void onChanged(Route s) {
                            TextView titleTextView = (TextView) view.findViewById(R.id.singleImageTitle);
                            titleTextView.setText(s.getTitle());
                        }
                    });

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myViewModel.setViewImage(element.getPicture_id());
                        }});


                }
            }});


        return view;



    }

    /**
     * Resumes activity
     */
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    private BitmapDescriptor getMarker(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Focuses the camera on the images route
     */
    private void focusCamera(){
        final Node cn = currentNode.getValue();

        myViewModel.getCompleteRouteFromId(cn.getRoute_id()).observe(getActivity(), new Observer<CompleteRoute>() {
            @Override
            public void onChanged(CompleteRoute newValue) {
                if (getContext() != null){
                    nodesGetter.clear();
                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
                    for (Edge edge : newValue.edges) {
                        LatLng point = new LatLng(edge.longitude, edge.latitude);
                        options.add(point);
                    }

                    for (Node node : newValue.nodes) {
                        LatLng point = new LatLng(node.getLatitude(), node.getLongitude());
                        Marker marker;
                        if (node.getId() == cn.getId()) {
                            marker = gmap.addMarker(new MarkerOptions().title(" ").position(point).snippet("Temp: " + node.getTemp() + "\nPressure: " + node.getBar()));
                        } else {
                            marker = gmap.addMarker(new MarkerOptions().title(" ").position(point).icon(getMarker(getResources().getDrawable(R.drawable.alt_other_marker))).snippet("Temp: " + node.getTemp() + "\nPressure: " + node.getBar()));
                        }
                        nodesGetter.put(marker, node);


                    }
                    gmap.addPolyline(options);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cn.getLatitude(), cn.getLongitude()), 15.0f));

                }

            }
        });

    }

    /**
     * Google maps callback function
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setInfoWindowAdapter(this);
        if (currentNode.getValue() != null){
            focusCamera();
        } else {
            currentNode.observe(this, new Observer<Node>() {
                @Override
                public void onChanged(@Nullable final Node element) {
                    focusCamera();
                }
            });
        }

    }


    /**
     * Needed for infocontents
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     *
     * Added Info to the Marker popup
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        try{
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
            myViewModel.getRouteFromId(element.getRoute_id()).observe(getActivity(), new Observer<Route>() {
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
