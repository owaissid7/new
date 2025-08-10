package com.animated.cartoon.stickers.wasticker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.animated.cartoon.stickers.wasticker.R;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterMain extends  RecyclerView.Adapter<AdapterMain.viewHolder> {

    public AdapterMain(ArrayList<HashMap<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;

    }


    ArrayList<HashMap<String, Object>> list;
Context context;
SharedPreferences sp;





    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view= LayoutInflater.from(context).inflate(R.layout.adapter_main,parent,false);
        sp = context.getSharedPreferences("sp", Activity.MODE_PRIVATE);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
holder.textView1.setText(list.get((int)position).get("name").toString());
//holder.textView1.setText(list.get((int)position).get("map").toString());
        ArrayList<HashMap<String, Object>> glist = new ArrayList<>();

        String str = (new Gson()).toJson(list.get((int)position).get("map"),
        new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());

        glist = new Gson().fromJson(str, new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
       final Adapterchild adapterchild = new Adapterchild(glist, context, list.get((int)position).get("name").toString()
             //  ,position+1
       );
      // holder.textindex.setText(String.valueOf(position+1));

       holder. recyclerView1.setAdapter(adapterchild);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.recyclerView1.setLayoutManager(gridLayoutManager);
        if (!(sp.getString("switch", "").equals(""))) {
           holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
           holder.textView1.setTextColor(Color.parseColor("#F8f8ff"));
        }



    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {

       TextView textView1;
      // TextView textindex;
      CardView cardView;
        RecyclerView recyclerView1;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textview1);
           // textindex = itemView.findViewById(R.id.textindex);
            recyclerView1 = itemView.findViewById(R.id.child_recycler);
          cardView  = itemView.findViewById(R.id.cardview1);

        }
    }
}
