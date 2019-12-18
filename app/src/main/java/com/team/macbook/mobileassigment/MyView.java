/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.app.Activity;
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

import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;


public class MyView extends AppCompatActivity {
    private MyViewModel myViewModel;
    private DBFragment db_frag;
    private CurrentFragment current_frag;
    private HomeFragment home_frag;

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





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
                    fab.setImageResource(drawable.ic_menu_camera);
                    MenuItem i = ((BottomNavigationView)findViewById(R.id.bottom_navigation)).getMenu().getItem(0);
                    i.setIcon(drawable.ic_menu_compass);
                    i.setTitle(R.string.menu_item_1_alt);
                    if (current_frag == null){
                        current_frag = new CurrentFragment();
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           EasyImage.openCamera(current_frag, 0);
                       }
                   });

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, current_frag);
                    transaction.commit();
                } else {
                    Log.d("Route Tracking", "Inactive");
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_fab);
                    fab.setImageResource(drawable.ic_input_add);
                    MenuItem i = ((BottomNavigationView)findViewById(R.id.bottom_navigation)).getMenu().getItem(0);
                    i.setIcon(drawable.ic_menu_add);
                    i.setTitle(R.string.menu_item_1);
                    if (home_frag == null){
                        home_frag = new HomeFragment();
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, home_frag);
                    transaction.commit();
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
                }
            }
        });
        initEasyImage();
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

