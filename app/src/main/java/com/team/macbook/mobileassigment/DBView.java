/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.database.RouteWithNodes;

import java.util.List;


public class DBView extends AppCompatActivity {
    private MyViewModel myViewModel;
    private String id = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        // Get a new or existing ViewModel from the ViewModelProvider.
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        // Add an observer on the LiveData. The onChanged() method fires
        // when the observed data changes and the activity is
        // in the foreground.

        final LifecycleOwner tracker = this;
        myViewModel.getCurrentRoute().observe(this, new Observer<MyViewModel.CurrentRoute>(){
            @Override
            public void onChanged(@Nullable final MyViewModel.CurrentRoute newValue) {
                Log.d("Changed", "changed");
                if (newValue != null) {
                    newValue.getRoute().observe(tracker, new Observer<Route>(){
                        @Override
                        public void onChanged(@Nullable final Route newValue) {
                            if (newValue != null) {
                                Log.d("Current route title", String.valueOf(newValue.getTitle()));
                                TextView title = findViewById(R.id.routeTitle);
                                title.setText(newValue.getTitle());
                            }
                        }});
                }
            }});





        // it generates a request to generate a new random number
        Button button = findViewById(R.id.getButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText num = findViewById(R.id.routeNumber);
                myViewModel.setCurrentRoute(num.getText().toString());
                Log.d("Setting route", num.getText().toString());

//                myViewModel.toggle();
//                startActivity(new Intent(MyView.this, MapsActivity.class));
            }
        });
        Button new_button = findViewById(R.id.newButton);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = findViewById(R.id.inputTitle);
                myViewModel.generateNewRoute(title.getText().toString());
                Log.d("Creating route", title.getText().toString());

//                myViewModel.toggle();
//                startActivity(new Intent(MyView.this, MapsActivity.class));
            }
        });
//        myViewModel.startBarometer();
//        myViewModel.startAccelerometer();
//        myViewModel.startThermometer();
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

