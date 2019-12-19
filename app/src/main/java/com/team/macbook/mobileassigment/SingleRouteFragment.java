package com.team.macbook.mobileassigment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleRouteFragment extends Fragment {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyGalleryAdapter mAdapter;


    public SingleRouteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_single_route, container, false);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        myViewModel.getViewRouteSingle().observe(this, new Observer<CompleteRoute>() {
            @Override
            public void onChanged(@Nullable final CompleteRoute element) {
                if (element != null) {
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView date = (TextView) view.findViewById(R.id.preview);
                    title.setText(element.route.getTitle());
                    date.setText(String.valueOf(new Date(element.route.getStartDate())));


                    mAdapter = new MyGalleryAdapter(new ArrayList<Node>(), myViewModel);
                    mRecyclerView = (RecyclerView) view.findViewById(R.id.route_pictures);
                    int numberOfColumns = 4;
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

                    mRecyclerView.setAdapter(mAdapter);


                    List<Node> nodes = new ArrayList<>();
                    for (Node node : element.nodes) {
                        nodes.add(node);
                    }

                    mAdapter.setItems(nodes);






                }
            }
        });

        return view;
    }
}
