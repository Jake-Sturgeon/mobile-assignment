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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleImageFragment extends Fragment {
    private View view;
    private MyViewModel myViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter mAdapter;



    public SingleImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_single_image, container, false);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        mAdapter = new MyAdapter(new ArrayList<CompleteRoute>(), myViewModel);


        myViewModel.getViewItemSingle().observe(this, new Observer<CompleteRoute>(){
            @Override
            public void onChanged(@Nullable final CompleteRoute element) {
                if (element != null) {
                    System.out.println("HELLO JAKE");
                    ImageView imageView = (ImageView) view.findViewById(R.id.image);
                    TextView textView = (TextView) view.findViewById(R.id.singleImageTitle);

//                    if (element.nodes.get(0).getPicture_id() != -1) {
//
//                    }
                    imageView.setImageResource(R.drawable.joe1);
                    textView.setText(element.route.getTitle());

                }
            }});


        return view;



    }

}
