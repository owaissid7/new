package com.animated.cartoon.stickers.wasticker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.animated.cartoon.stickers.wasticker.ImageKeyboard;
import com.animated.cartoon.stickers.wasticker.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Adapter_key_sticker extends  RecyclerView.Adapter<Adapter_key_sticker.viewHolder> {

    public Adapter_key_sticker(ArrayList<String> list,ArrayList<String> key,  HashMap<String, Object> map, Context context, ImageKeyboard imageKeyboard) {
        this.list = list;
        this.key = key;
        this.context = context;
        this.imageKeyboard= imageKeyboard;
        this.map= map;
    }


    ArrayList<String> list;
    ArrayList<String> key;
    Context context;
    ImageKeyboard imageKeyboard;
     HashMap<String, Object> map ;
    List list2 = new ArrayList();

    SharedPreferences sp;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.recycler_adapter_key, parent, false);
        sp = context.getSharedPreferences("sp", Activity.MODE_PRIVATE);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        if(imageKeyboard.position==0){

            Glide.with(context)
                    .load(list.get((int) position)).error(R.drawable.ic_round_error_24)
                    .placeholder(R.drawable.stickersample
                    ).into(holder.sample);

            holder.linearchild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if ((Build.VERSION.SDK_INT >= 25)) {
                        switch (imageKeyboard.iscommitted()){
                            case 1:
                                imageKeyboard.dogif(list.get((int) position));
                                break;
                            case 2:
                          imageKeyboard.dopng(list.get((int) position));
                                break;
                            default:
                                Toast.makeText(context, "Stickers Is Not Allowed Here...", Toast.LENGTH_SHORT).show();
                                break;
                        }



                    } else {
                        Toast.makeText(context, "Sorry, Android 7.1 (Nougat) or Higher is Required!", Toast.LENGTH_LONG).show();
                    }







                }
            });

        }
        else {

            Glide.with(context)
                    .load(list2.get((int)position).toString().trim()).error(R.drawable.ic_round_error_24)
                    .placeholder(R.drawable.stickersample
                    ).into(holder.sample);

            holder.linearchild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if ((Build.VERSION.SDK_INT >= 25)) {
                        switch (imageKeyboard.iscommitted()){
                            case 1:
                                imageKeyboard.dogif(list2.get((int)position).toString().trim());

                                break;
                            case 2:
                                imageKeyboard.dopng(list2.get((int)position).toString().trim());

                                break;
                            default:
                                Toast.makeText(context, "Stickers Is Not Allowed Here...", Toast.LENGTH_SHORT).show();
                                break;
                        }


                        } else {
                            Toast.makeText(context, "Sorry, Android 7.1 (Nougat) or Higher is Required!", Toast.LENGTH_LONG).show();
                        }



                }
            });

        }
        if (!(sp.getString("switch", "").equals(""))) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        if(!(imageKeyboard.position==0)){
         if(map.containsKey(key.get(imageKeyboard.position-1).split(",")[0])){
            String data = map.get(key.get(imageKeyboard.position-1).split(",")[0]).toString();
            list2 = Arrays.asList(data.split(","));}
         else{
             key.remove(imageKeyboard.position-1);
             SharedPreferences sp = Objects.requireNonNull(this.context).getSharedPreferences("sp", Context.MODE_PRIVATE);
             sp.edit().putString("keyboard", new Gson().toJson(key)).commit();
             imageKeyboard.position=0;
             imageKeyboard.loadsticker();
             Toast.makeText(context, "ff", Toast.LENGTH_SHORT).show();
         }


        }

        if(imageKeyboard.position==0){
        return list.size();
        }
        else{
            return list2.size();
        }

    }



    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView sample;

        LinearLayout linearchild;
        CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);


            linearchild = itemView.findViewById(R.id.pickup_recycler);
            sample = itemView.findViewById(R.id.img_pickup);
            cardView  = itemView.findViewById(R.id.cardview1);


        }
    }




}