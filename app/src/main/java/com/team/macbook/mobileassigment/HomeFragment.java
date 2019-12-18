package com.team.macbook.mobileassigment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.macbook.mobileassigment.database.CompleteRoute;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        mAdapter = new MyAdapter(new ArrayList<CompleteRoute>());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        myViewModel.getAllCompleteRoutes().observe(this, new Observer<List<CompleteRoute>>(){
            @Override
            public void onChanged(@Nullable final List<CompleteRoute> newValue) {
                if (newValue != null) {
                    Log.d("HomeFrag", "Setting Items len "+newValue.size()+"");
                    mAdapter.setItems(newValue);

                }
            }});

        return view;
    }

}
