package com.animated.cartoon.stickers.wasticker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.animated.cartoon.stickers.wasticker.ImageKeyboard;
import com.animated.cartoon.stickers.wasticker.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;


public class Adapter_key_main extends  RecyclerView.Adapter<Adapter_key_main.viewHolder> {

    public Adapter_key_main(ArrayList<String> key, Context context, ImageKeyboard imageKeyboard) {
        this.key = key;
        this.context = context;
        this.imageKeyboard= imageKeyboard;

    }

// FAV s fragment = pickup
    //ArrayList<String> stickerl;
    ArrayList<String> key;
    Context context;
    ImageKeyboard imageKeyboard;

    SharedPreferences sp;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.recycler_keyboard, parent, false);
        sp = context.getSharedPreferences("sp", Activity.MODE_PRIVATE);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        if(position ==imageKeyboard.position){
            holder.space.setVisibility(View.VISIBLE);
        }else{
            holder.space.setVisibility(View.GONE);
        }
        if(position==0){
            holder.textView.setText("Favourite");
            Glide.with(context)
                    .load(R.drawable.favoritefull_24).error(R.drawable.ic_round_error_24)
                    .placeholder(R.drawable.stickersample).into(holder.sample);

            holder.linearchild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    imageKeyboard.position=position;
                    notifyDataSetChanged();
                    imageKeyboard.loadsticker();
                    imageKeyboard.toptitle.setText("Favourite List");
                }
            });
        }
        else {
            holder.textView.setText(key.get((int) position - 1).split(",")[1]);
            Glide.with(context)
                    .load(key.get((int) position - 1).split(",")[2]).error(R.drawable.ic_round_error_24)
                    .placeholder(R.drawable.stickersample).into(holder.sample);

            holder.linearchild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    imageKeyboard.position=position;
                    notifyDataSetChanged();
                    imageKeyboard.loadsticker();
                    imageKeyboard.toptitle.setText(key.get((int) position - 1).split(",")[1]);

                }
            });

        }
        if (!(sp.getString("switch", "").equals(""))) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
            holder.textView.setTextColor(Color.parseColor("#F8f8ff"));
        }

    }

    @Override
    public int getItemCount() {
        return key.size()+1;
    }

    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView sample;

        TextView textView;
        LinearLayout linearchild;
        LinearLayout space;
        CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textView= itemView.findViewById(R.id.text_cat);
            linearchild = itemView.findViewById(R.id.pickup_recycler);
            sample = itemView.findViewById(R.id.img_pickup);
            space = itemView.findViewById(R.id.space);
            cardView  = itemView.findViewById(R.id.cardview1);

        }
    }
}