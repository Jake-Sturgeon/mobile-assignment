package com.team.macbook.mobileassigment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyRouteGalleryAdapter extends RecyclerView.Adapter<MyRouteGalleryAdapter.View_Holder> {
    private Context context;
    private static List<CompleteRoute> items;
    private MyViewModel myViewModel;
    private MyGalleryAdapter mAdapter;
    private LifecycleOwner activity;

    public MyRouteGalleryAdapter(List<CompleteRoute> items, MyViewModel myViewModel, LifecycleOwner activity) {
        this.items = items;
        this.myViewModel = myViewModel;
        this.activity = activity;
    }
    public MyRouteGalleryAdapter(Context cont, List<CompleteRoute> items, MyViewModel myViewModel, LifecycleOwner activity) {
        super();
        this.items = items;
        this.myViewModel = myViewModel;
        context = cont;
        this.activity = activity;
    }

    public void setItems(List<CompleteRoute> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_by_route_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }
    @Override
    public void onBindViewHolder(View_Holder holder, final int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {
            holder.title.setText(items.get(position).route.getTitle());
            holder.preview.setText(String.valueOf(new Date(items.get(position).route.getStartDate())));


            mAdapter = new MyGalleryAdapter(new ArrayList<CompleteRoute>(), myViewModel);
            int numberOfColumns = 4;
            holder.images_gallery.setLayoutManager(new GridLayoutManager(context, numberOfColumns));

            holder.images_gallery.setAdapter(mAdapter);

            myViewModel.getAllCompleteRoutes().observe(activity , new Observer<List<CompleteRoute>>(){
                @Override
                public void onChanged(@Nullable final List<CompleteRoute> newValue) {
                    if (newValue != null) {
                        Log.d("HomeFrag", "Setting Items len "+newValue.size()+"");
                        mAdapter.setItems(newValue);

                    }
                }});


            //holder.imageView.setImageResource(R.drawable.joe1);


        }
        //animate(holder);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public static List<CompleteRoute> getItems() {
        return items;
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView preview;
        RecyclerView images_gallery;
        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            preview = (TextView) itemView.findViewById(R.id.preview);
            images_gallery = (RecyclerView) itemView.findViewById(R.id.route_pictures);
        }
    }




// end of MyAdapter class
}
