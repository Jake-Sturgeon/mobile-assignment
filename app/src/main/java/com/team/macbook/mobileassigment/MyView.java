/*
 * Copyright (c) 2018. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package com.team.macbook.mobileassigment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;
import com.team.macbook.mobileassigment.database.RouteWithNodes;

import java.util.List;


public class MyView extends AppCompatActivity {
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a new or existing ViewModel from the ViewModelProvider.
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        // Add an observer on the LiveData. The onChanged() method fires
        // when the observed data changes and the activity is
        // in the foreground.


//        myViewModel.getNodeToDisplay().observe(this, new Observer<Node>(){
//            public void onChanged(@Nullable final Node newValue) {
//                if (newValue != null) {
//                    TextView tv = findViewById(R.id.textView);
//                    tv.setText(newValue.getRoute_id() + "");
//                }
//            }});




        // it generates a request to generate a new random number
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.generateNewNode();

//                myViewModel.toggle();
//                startActivity(new Intent(MyView.this, MapsActivity.class));
            }
        });

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

