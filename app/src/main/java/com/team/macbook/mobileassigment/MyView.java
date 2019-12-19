/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.R.drawable;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;

import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;


public class MyView extends AppCompatActivity {
    private MyViewModel myViewModel;
    private DBFragment db_frag;
    private CurrentFragment current_frag;
    private HomeFragment home_frag;
    private GalleryFragment gallery_frag;
    private SingleImageFragment single_image_frag;




    public void switchViewDB(MenuItem item){
        if (db_frag == null){
            db_frag = new DBFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, db_frag);
        transaction.commit();
    }

    public void switchViewHome(MenuItem item){
        if (home_frag == null){
            home_frag = new HomeFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, home_frag);
        transaction.commit();
    }

    public void switchViewGallery(MenuItem item){
        if (gallery_frag == null){
            gallery_frag = new GalleryFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, gallery_frag);
        transaction.commit();
    }

    public void switchViewSingleImage(MenuItem item){
        if (single_image_frag == null){
            single_image_frag = new SingleImageFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, single_image_frag);
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
                transaction.commit();
            }
        });

//        myViewModel.getNodeToDisplay().observe(this, new Observer<Node>(){
//            public void onChanged(@Nullable final Node newValue) {
//                if (newValue != null) {
//                    TextView tv = findViewById(R.id.textView);
//                    tv.setText(newValue.getRoute_id() + "");
//                }
//            }});



        Fragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
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
                    transaction.commit();
                }
            }});
    }

    @Override
    protected void onPause(){
        super.onPause();
//        myViewModel.pauseAccelerometer();
//        myViewModel.pauseBarometer();
//        myViewModel.pauseThermometer();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        myViewModel.startAccelerometer();
//        myViewModel.startBarometer();
//        myViewModel.startThermometer();

    }
}

