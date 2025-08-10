package com.animated.cartoon.stickers.wasticker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.animated.cartoon.stickers.wasticker.Fav_c_Fragment;
import com.animated.cartoon.stickers.wasticker.MainActivity;
import com.animated.cartoon.stickers.wasticker.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Fav_c extends  RecyclerView.Adapter<Adapter_Fav_c.viewHolder> {

    public Adapter_Fav_c(ArrayList<HashMap<String, Object>> favourite, Context context, Fav_c_Fragment fav_c_fragment) {
        this.favourite = favourite;
        this.context = context;
        this.fav_c_fragment = fav_c_fragment;

    }

// FAV s fragment = pickup
ArrayList<HashMap<String, Object>> favourite ;

    Context context;
    Fav_c_Fragment fav_c_fragment;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_fav_c, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        Glide.with(context)
                .load(favourite.get( position).get("image").toString())
                .placeholder(R.drawable.stickersample
                ).into(holder.sample);
holder.textView1.setText(favourite.get( position).get("packname").toString()
+" - "+favourite.get( position).get("pack").toString() );
holder.textView2.setText(favourite.get( position).get("count").toString()+" Stickers");
        holder.linearchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!(((MainActivity) context).map.containsKey(favourite.get(position).get("id").toString()))) {
                    fav_c_fragment.refreshm(position);
                } else {
                    ((MainActivity) context).packname=favourite.get(position).get("packname").toString();
                    ((MainActivity) context).add_pickup(
                            favourite.get(position).get("id").toString(),
                            favourite.get(position).get("pack").toString(),
                            favourite.get(position).get("value").toString()
                    ); }
            }
        });
   holder.fav_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
             fav_c_fragment.refreshm(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return favourite.size();
    }

    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView sample;
        TextView textView1;
        TextView textView2;
        //SimpleDraweeView draweeWebpAnimated;
        LinearLayout linearchild;
        LinearLayout fav_card;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            // draweeWebpAnimated = itemView.findViewById(R.id.img_pickup);
            linearchild = itemView.findViewById(R.id.pickup_recycler);
            fav_card = itemView.findViewById(R.id.fav_card);
            sample = itemView.findViewById(R.id.img_child);
           textView1 = itemView.findViewById(R.id.textview1);
           textView2 = itemView.findViewById(R.id.textview2);

        }
    }
}
