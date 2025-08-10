package com.animated.cartoon.stickers.wasticker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.animated.cartoon.stickers.wasticker.Fav_s_Fragment;
import com.animated.cartoon.stickers.wasticker.MainActivity;
import com.animated.cartoon.stickers.wasticker.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;



public class Adapter_Fav_s extends  RecyclerView.Adapter<Adapter_Fav_s.viewHolder> {

    public Adapter_Fav_s(ArrayList<String> list, Context context, Fav_s_Fragment fav_s_fragment) {
        this.list = list;
        this.context = context;
        this.fav_s_fragment = fav_s_fragment;

    }

// FAV s fragment = pickup
    ArrayList<String> list;
    Context context;
    Fav_s_Fragment fav_s_fragment;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.recycler_pickup, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        Glide.with(context)
                .load(list.get((int) position)).error(R.drawable.ic_round_error_24)
                .placeholder(R.drawable.stickersample
                ).into(holder.sample);

        holder.linearchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Glide.with(context).asBitmap().load(list.get((int) position))
                        .placeholder(R.drawable.stickersample
                        ).addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Toast.makeText(context, "Sticker Is Not Yet Loaded", Toast.LENGTH_SHORT).show();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        ((MainActivity)context).show_emoji(list.get((int) position),fav_s_fragment);

                        return false;
                    }
                }).into(holder.sample);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView sample;

        //SimpleDraweeView draweeWebpAnimated;
        LinearLayout linearchild;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            // draweeWebpAnimated = itemView.findViewById(R.id.img_pickup);
            linearchild = itemView.findViewById(R.id.pickup_recycler);
            sample = itemView.findViewById(R.id.img_pickup);

        }
    }
}