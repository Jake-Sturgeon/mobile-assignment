package com.team.macbook.mobileassigment.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.team.macbook.mobileassigment.activities.MapsActivity;
import com.team.macbook.mobileassigment.models.MyViewModel;
import com.team.macbook.mobileassigment.R;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class DBFragment extends Fragment {
    View view;
    private MyViewModel myViewModel;

    public DBFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_db, container, false);


        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        // Add an observer on the LiveData. The onChanged() method fires
        // when the observed data changes and the activity is
        // in the foreground.

        final LifecycleOwner tracker = this;

//        myViewModel.getCR().observe(this, new Observer<CompleteRoute>(){
//            @Override
//            public void onChanged(@Nullable final CompleteRoute newValue) {
//                if (newValue != null) {
//                    Log.d("Current route title", String.valueOf(newValue.route.getTitle()));
//                    TextView title = view.findViewById(R.id.routeTitle);
//                    title.setText(newValue.route.getTitle());
//                    TextView node_number = view.findViewById(R.id.node_number);
//                    node_number.setText(String.valueOf(newValue.nodes.size()));
//                    TextView edge_number = view.findViewById(R.id.edge_number);
//                    edge_number.setText(String.valueOf(newValue.edges.size()));
//                }
//            }});


        // it generates a request to generate a new random number
//        Button button = view.findViewById(R.id.getButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText num = view.findViewById(R.id.routeNumber);
//                myViewModel.setCR(num.getText().toString() ,tracker);
//                Log.d("Setting route", num.getText().toString());
//
////                myViewModel.toggle();
////                startActivity(new Intent(MyView.this, MapsActivity.class));
//            }
//        });
        Button new_button = view.findViewById(R.id.submit);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText title = view.findViewById(R.id.inputTitle);
                if (!(title.getText().length() > 0)) {
                    Toast.makeText(getActivity(), "Route Must Have A Title", Toast.LENGTH_LONG).show();
                } else {
                    String id = UUID.randomUUID().toString();
                    Log.i("BEGIN", title.getText().toString());
                    myViewModel.generateNewRoute(id, title.getText().toString());
                    Log.d("Creating route", id);
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    intent.putExtra("current_route", id);
                    startActivity(intent);
                }
//                myViewModel.setRoute_active(true);
//                myViewModel.toggle();
//                startActivity(new Intent(MyView.this, MapsActivity.class));
            }
        });


        return view;
    }

}
