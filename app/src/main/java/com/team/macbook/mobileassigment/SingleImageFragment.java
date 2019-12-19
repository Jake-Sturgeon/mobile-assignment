package com.team.macbook.mobileassigment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Edge;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleImageFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private LiveData<Node> currentNode = new MutableLiveData<>();



    public SingleImageFragment() {
        // Required empty public constructor
    }


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

    private void focusCamera(){
        final Node cn = currentNode.getValue();

        myViewModel.getCompleteRouteFromId(cn.getRoute_id()).observe(getActivity(), new Observer<CompleteRoute>() {
            @Override
            public void onChanged(CompleteRoute newValue) {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
                for (Edge edge : newValue.edges) {
                    LatLng point = new LatLng(edge.longitude, edge.latitude);
                    options.add(point);
                }
                for (Node node : newValue.nodes) {
                    LatLng point = new LatLng(node.getLatitude(), node.getLongitude());
                    if (node.getId() == cn.getId()){
                        Marker marker = gmap.addMarker(new MarkerOptions().title(" ").position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).snippet("Temp: " + node.getTemp() + "\nPressure: " + node.getBar()));
                    } else {
                        Marker marker = gmap.addMarker(new MarkerOptions().title(" ").position(point).icon(getMarker(getResources().getDrawable(R.drawable.alt_other_marker))).snippet("Temp: " + node.getTemp() + "\nPressure: " + node.getBar()));
                    }


                }
                gmap.addPolyline(options);
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cn.getLatitude(), cn.getLongitude()), 15.0f));
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
//        gmap.setMinZoomPreference(12);
//        LatLng ny = new LatLng(40.7143528, -74.0059731);
//        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny))
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

}
