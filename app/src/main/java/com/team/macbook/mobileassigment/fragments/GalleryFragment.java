package com.team.macbook.mobileassigment.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.adapters.MyGalleryAdapter;
import com.team.macbook.mobileassigment.models.MyViewModel;
import com.team.macbook.mobileassigment.R;
import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyGalleryAdapter mAdapter;


    /**
     * Required empty contructor
     */
    public GalleryFragment() {
        // Required empty public constructor
    }


    /**
     *
     * Creates views, viewers, buttons, viewmodels
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
        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        mAdapter = new MyGalleryAdapter(new ArrayList<Node>(), myViewModel);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_list);
        // set up the RecyclerView
        int numberOfColumns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        mRecyclerView.setAdapter(mAdapter);

        myViewModel.getAllCompleteRoutes().observe(this, new Observer<List<CompleteRoute>>(){
            @Override
            public void onChanged(@Nullable final List<CompleteRoute> newValue) {
                if (newValue != null) {
                    Log.d("HomeFrag", "Setting Items len "+newValue.size()+"");
                    HashMap<String, CompleteRoute> map = new HashMap<>();
                    List<Node> nodes = new ArrayList<>();
                    for (CompleteRoute cr: newValue){
                        map.put(cr.route.getRouteId(), cr);
                        for (Node node: cr.nodes){
                            nodes.add(node);
                        }
                    }
                    mAdapter.setItems(nodes);

                }
            }});

        return view;
    }

}
