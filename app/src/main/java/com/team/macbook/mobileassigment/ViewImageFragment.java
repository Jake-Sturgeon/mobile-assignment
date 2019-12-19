package com.team.macbook.mobileassigment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImageFragment extends Fragment {
    private View view;
    private MyViewModel myViewModel;

    public ViewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image, container, false);
        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);


        myViewModel.getViewImage().observe(this, new Observer<String>(){
            @Override
            public void onChanged(@Nullable final String element) {
                if (element != null) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.image);

                    Bitmap myBitmap = BitmapFactory.decodeFile(element);
                    imageView.setImageBitmap(myBitmap);

                }
            }});


        return view;



    }

}
