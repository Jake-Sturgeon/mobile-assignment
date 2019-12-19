package com.team.macbook.mobileassigment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.View_Holder> {
    private Context context;
    private static List<Integer> items;
    private MyViewModel myViewModel;
    private HashMap<Integer, Node> nodes = new HashMap<>();
    private HashMap<Integer, String> images = new HashMap<>();

    public MyGalleryAdapter(List<Node> items, MyViewModel myViewModel) {
        for (Node node: items){
            nodes.put(node.getId(), node);
            images.put(node.getId(), node.getIcon_id());
        }
        this.items = new ArrayList<>(nodes.keySet());
        this.myViewModel = myViewModel;
    }
    public MyGalleryAdapter(Context cont, List<Node> items, MyViewModel myViewModel) {
        super();
        for (Node node: items){
            nodes.put(node.getId(), node);
            images.put(node.getId(), node.getIcon_id());
        }
        this.items = new ArrayList<>(nodes.keySet());
        this.myViewModel = myViewModel;
        context = cont;
    }


    public void setItems(List<Node> items) {
        for (Node node: items){
            nodes.put(node.getId(), node);
            images.put(node.getId(), node.getIcon_id());
        }
        this.items = new ArrayList<>(nodes.keySet());
        notifyDataSetChanged();
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_image,
                parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, final int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the
        // current row on the RecyclerView
        if (holder!=null && items.get(position)!=null) {
            Log.d("Printing", "akushbaks");
            //holder.title.setText(items.get(position).route.getTitle());
            //holder.preview.setText(String.valueOf(new Date(items.get(position).route.getStartDate())));

            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(images.get(items.get(position))));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myViewModel.setViewItemSingle(nodes.get(items.get(position)));


                }
            });
        }
        //animate(holder);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }





    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View_Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
        }
    }



// end of MyAdapter class
}
