package com.team.macbook.mobileassigment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;
import com.team.macbook.mobileassigment.database.Node;
import com.team.macbook.mobileassigment.database.Route;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.View_Holder> {
    private Context context;
    private static List<Node> items;
    private MyViewModel myViewModel;

    public MyGalleryAdapter(List<Node> items, MyViewModel myViewModel) {
        this.items = items;
        this.myViewModel = myViewModel;
    }
    public MyGalleryAdapter(Context cont, List<Node> items, MyViewModel myViewModel) {
        super();
        this.items = items;
        this.myViewModel = myViewModel;
        context = cont;
    }


    public void setItems(List<Node> items) {
        this.items = items;
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
            //holder.title.setText(items.get(position).route.getTitle());
            //holder.preview.setText(String.valueOf(new Date(items.get(position).route.getStartDate())));
            Bitmap myBitmap = BitmapFactory.decodeFile(items.get(position).getPicture_id());
            holder.imageView.setImageBitmap(myBitmap);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myViewModel.setViewItemSingle(items.get(position));


                }
            });
        }
        //animate(holder);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public static List<Node> getItems() {
        return items;
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
