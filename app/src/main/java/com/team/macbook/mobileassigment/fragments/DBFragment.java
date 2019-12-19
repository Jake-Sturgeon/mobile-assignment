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

    /**
     * Required empty constructor
     */
    public DBFragment() {
        // Required empty public constructor
    }


    /**
     *
     * Creates view, view model, buttons
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_db, container, false);


        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        // Add an observer on the LiveData. The onChanged() method fires
        // when the observed data changes and the activity is
        // in the foreground.

        final LifecycleOwner tracker = this;
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
            }
        });


        return view;
    }

}
