package com.team.macbook.mobileassigment.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.macbook.mobileassigment.models.MyViewModel;
import com.team.macbook.mobileassigment.R;

import android.R.drawable;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MyViewModel myMapModel;

    private static GoogleMap mMap;
    private FragmentActivity myContext;
    private MapView mapView;

    /**
     * Attaches a context to the fragment activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }


    /**
     * Required empty constructor
     */
    public CurrentFragment() {
        // Required empty public constructor
    }


    /**
     *
     * Creates the View, modelviews, and buttons
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
        view = inflater.inflate(R.layout.fragment_current, container, false);
        myMapModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.finish_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to stop ?");
                builder.setIcon(drawable.ic_dialog_alert);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        myMapModel.setRoute_active(false);    // stop chronometer here

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        return view;
    }

    /**
     * Google map callback
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myMapModel.setCR("1",getActivity());
        Log.i("modle", myMapModel.getCR().toString());
    }
}
