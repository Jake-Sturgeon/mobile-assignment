/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.macbook.mobileassigment.R;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.fragments.CurrentFragment;
import com.team.macbook.mobileassigment.fragments.DBFragment;
import com.team.macbook.mobileassigment.fragments.GalleryByRouteFragment;
import com.team.macbook.mobileassigment.fragments.GalleryFragment;
import com.team.macbook.mobileassigment.fragments.HomeFragment;
import com.team.macbook.mobileassigment.fragments.SingleImageFragment;
import com.team.macbook.mobileassigment.fragments.SingleRouteFragment;
import com.team.macbook.mobileassigment.fragments.ViewImageFragment;
import com.team.macbook.mobileassigment.models.MyViewModel;
import com.team.macbook.mobileassigment.services.ForegroundService;

import java.util.Stack;


public class MyView extends AppCompatActivity {
    private MyViewModel myViewModel;
    private DBFragment db_frag;
    private CurrentFragment current_frag;
    private HomeFragment home_frag;
    private GalleryFragment gallery_frag;
    private SingleImageFragment single_image_frag;
    private SingleRouteFragment single_route_frag;
    private ViewImageFragment view_image_frag;
    private GalleryByRouteFragment gallery_by_route_frag;

    private Stack<Fragment> previous_frags = new Stack<>();
    private Fragment this_frag;


    /**
     * Switches to the create fragment
     *
     * @param item
     */
    public void switchViewDB(MenuItem item){
        if (db_frag == null){
            db_frag = new DBFragment();
        }
        previous_frags = new Stack<>();
        previous_frags.add(home_frag);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, db_frag);
        this_frag = db_frag;
        transaction.commit();
    }

    /**
     * Shows the gallery sort by route
     *
     * @param item
     */
    public void switchViewRouteGallery(MenuItem item){
        if (gallery_by_route_frag == null){
            gallery_by_route_frag = new GalleryByRouteFragment();
        }
        previous_frags = new Stack<>();
        previous_frags.add(home_frag);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, gallery_by_route_frag);
        this_frag = gallery_by_route_frag;
        transaction.commit();
    }

    /**
     * Shows list of routes
     * @param item
     */
    public void switchViewHome(MenuItem item){
        if (home_frag == null){
            home_frag = new HomeFragment();
        }

        previous_frags = new Stack<>();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, home_frag);
        this_frag = home_frag;
        transaction.commit();
    }


    /**
     * Switches to the gallery that provides all the images
     *
     * @param item
     */
    public void switchViewGallery(MenuItem item){
        if (gallery_frag == null){
            gallery_frag = new GalleryFragment();
        }

        previous_frags = new Stack<>();
        previous_frags.add(home_frag);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, gallery_frag);

        this_frag = gallery_frag;
        transaction.commit();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates all the buttons, history, all the fragments, view models
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isMyServiceRunning(ForegroundService.class)){
            Intent intent = new Intent(this, ForegroundService.class);
            stopService(intent);
        };


        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_home);

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db_frag == null){
                    db_frag = new DBFragment();
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, db_frag);
                previous_frags.add(this_frag);
                this_frag = db_frag;
                transaction.commit();
            }
        });





        Fragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        this_frag = home_frag;
        transaction.commit();

        myViewModel.getRoute_active().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newValue) {
                Log.d("MyView", "ACTIVE");
                if (newValue) {
                    Log.d("Route Tracking", "Active");

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);

                } else {
                    Log.d("Route Tracking", "Inactive");
                    if (home_frag == null){
                        home_frag = new HomeFragment();
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, home_frag);
                    previous_frags.add(this_frag);
                    this_frag = home_frag;
                    transaction.commit();

                }
            }
        });
        myViewModel.getViewItemSingle().observe(this, new Observer<Node>(){
            @Override
            public void onChanged(@Nullable final Node element) {
                if (element != null) {
                    if (single_image_frag == null){
                        single_image_frag = new SingleImageFragment();
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, single_image_frag);
                    previous_frags.add(this_frag);
                    this_frag = single_image_frag;
                    transaction.commit();
                }
            }});

        myViewModel.getViewRouteSingle().observe(this, new Observer<CompleteRoute>(){
            @Override
            public void onChanged(@Nullable final CompleteRoute element) {
                if (element != null) {
                    if (single_route_frag == null){
                        single_route_frag = new SingleRouteFragment();
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, single_route_frag);
                    previous_frags.add(this_frag);
                    this_frag = single_route_frag;
                    transaction.commit();
                }
            }});

        myViewModel.getViewImage().observe(this, new Observer<String>(){
            @Override
            public void onChanged(@Nullable final String element) {
                if (element != null) {
                    if (view_image_frag == null){
                        view_image_frag = new ViewImageFragment();
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, view_image_frag);
                    previous_frags.add(this_frag);
                    this_frag = view_image_frag;
                    transaction.commit();
                }
            }});
    }

    /**
     * Pauses activity
     */
    @Override
    protected void onPause(){
        super.onPause();
    }

    /**
     * resumes the activity
     */
    @Override
    protected void onResume(){
        super.onResume();

    }

    /**
     * Handles the backpress by poping the history stack
     */
    @Override
    public void onBackPressed() {
        if (previous_frags.empty()) {
            moveTaskToBack(true);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            this_frag = previous_frags.pop();
            transaction.replace(R.id.fragment_container, this_frag);
            transaction.commit();
        }


    }

}

