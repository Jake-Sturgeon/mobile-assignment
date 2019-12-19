package com.team.macbook.mobileassigment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyGalleryByRouteAdaptor extends RecyclerView.Adapter<MyGalleryByRouteAdaptor.View_Holder> {
    private Context context;
    private static List<CompleteRoute> items;
    private MyViewModel myViewModel;

    private RecyclerView.LayoutManager mLayoutManager;
    private MyGalleryAdapter mAdapter;

    public MyGalleryByRouteAdaptor(List<CompleteRoute> items, MyViewModel myViewModel) {

        this.items = items;
        this.myViewModel = myViewModel;
    }
    public MyGalleryByRouteAdaptor(Context cont, List<CompleteRoute> items, MyViewModel myViewModel) {
        super();
        this.items = items;
        this.myViewModel = myViewModel;
        context = cont;
    }

    public void setItems(List<CompleteRoute> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_route_gallery,
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


            mAdapter = new MyGalleryAdapter(new ArrayList<Node>(), myViewModel);
            int numberOfColumns = 4;
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));

            holder.mRecyclerView.setAdapter(mAdapter);


            List<Node> nodes = new ArrayList<>();
            for (Node node : items.get(position).nodes) {
                nodes.add(node);
            }

            mAdapter.setItems(nodes);



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
        RecyclerView mRecyclerView;
        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            preview = (TextView) itemView.findViewById(R.id.preview);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.route_pictures);
        }
    }




// end of MyAdapter class
}
