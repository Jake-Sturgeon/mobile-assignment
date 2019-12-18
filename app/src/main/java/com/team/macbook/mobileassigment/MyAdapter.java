package com.team.macbook.mobileassigment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.team.macbook.mobileassigment.database.CompleteRoute;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.View_Holder> {
    private Context context;
    private static List<CompleteRoute> items;
    private MyViewModel myViewModel;
    public MyAdapter(List<CompleteRoute> items, MyViewModel myViewModel) {
        this.items = items;
        this.myViewModel = myViewModel;
    }
    public MyAdapter(Context cont, List<CompleteRoute> items, MyViewModel myViewModel) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image,
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

            if (items.get(position).nodes.size() == 0) {
                holder.imageView.setImageResource(R.drawable.joe1);
            } else {
                Bitmap myBitmap = BitmapFactory.decodeFile(items.get(position).nodes.get(0).getPicture_id());
                holder.imageView.setImageBitmap(myBitmap);
            }

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
        ImageView imageView;
        View_Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            preview = (TextView) itemView.findViewById(R.id.preview);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
        }
    }




// end of MyAdapter class
}
