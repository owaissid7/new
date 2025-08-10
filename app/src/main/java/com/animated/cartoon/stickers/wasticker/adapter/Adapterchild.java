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


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.animated.cartoon.stickers.wasticker.MainActivity;
import com.animated.cartoon.stickers.wasticker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapterchild extends  RecyclerView.Adapter<Adapterchild.viewHolder> {


    public Adapterchild(ArrayList<HashMap<String, Object>> list, Context context, String packname
           // , int i)
    ) {
        this.list = list;
        this.context = context;
        this.packname= packname;
        //this.i= i;
    }

    ArrayList<HashMap<String, Object>> list;
    Context context;
    String packname;
    SharedPreferences sp;
  // int i;
//Fav_s_Fragment fav_s_fragment;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.child_recycler, parent, false);
        sp = context.getSharedPreferences("sp", Activity.MODE_PRIVATE);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
        holder.textView1.setText(list.get((int) position).get("pack").toString());


//        ControllerListener listener = new BaseControllerListener<ImageInfo>() {
//
//            @Override
//            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
//                //Toast.makeText(getApplicationContext(), "lllll",Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//                //   Toast.makeText(getApplicationContext(), "fffff",Toast.LENGTH_SHORT).show();
//
//            }
//
//        };
//
//
//        BaseControllerListener baseControllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//
//
//                        if (animatable != null && AnimatedDrawable2.class.isInstance(animatable)) {
//                            final AnimatedDrawable2 animatedDrawable2 = (AnimatedDrawable2) animatable;
//                            final int totalCnt = animatedDrawable2.getFrameCount();
//                            animatedDrawable2.setAnimationListener(new BaseAnimationListener() {
//                                private int lastFrame; //Prevent infinite loop and exit the animation in time
//
//                                @Override
//                                public void onAnimationFrame(AnimatedDrawable2 drawable, int frameNumber) {
//
//                                    if (!(lastFrame == 0 && totalCnt <= 1) && lastFrame <= frameNumber) {
//                                        lastFrame = frameNumber;
//                                    } else {
//                                        animatedDrawable2.stop();
//                                    }
//                                }
//
//                                @Override
//                                public void onAnimationStart(AnimatedDrawable2 drawable) {
//                                    lastFrame = -1;
//                                }
//
//                                @Override
//                                public void onAnimationStop(AnimatedDrawable2 drawable) {
//
//                                }
//                            });
//                        }
//
//
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//                //   Log.e("test", throwable.getMessage());
//            }
//        };
//
//        holder.draweeWebpAnimated.setController(
//                Fresco.newDraweeControllerBuilder()
//                        //   setImageRequest(request)
//
//                        .setAutoPlayAnimations(true)
//                        .setControllerListener(baseControllerListener)
//                        .setOldController(holder.draweeWebpAnimated.getController())
//                        .setUri(list.get((int) position).get("img").toString())
//                        .build());
        Glide.with(context)
               .load(list.get((int) position).get("img").toString())
//                .load(context.getExternalCacheDir() +"/"+i +"_"+
//                        (position + 1) +".gif")
                .placeholder(R.drawable.stickersample
                )

                .into(holder.img_child);

        if (!(list.get((int) position).get("new").toString().equals("0"))) {
            holder.imagenew.setVisibility(View.VISIBLE);
        }

        holder.linearchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

               // ((MainActivity) context).setpickup(list.get((int) position).get("mapid").toString());
               // ((MainActivity) context).pack = list.get((int) position).get("pack").toString();
                //  ((MainActivity) context).value= list.get((int) position).get("value").toString();
//

                ((MainActivity) context).packname= packname;
                ((MainActivity) context).add_pickup(list.get((int) position).get("mapid").toString(),
                        list.get((int) position).get("pack").toString(),
                        list.get((int) position).get("value").toString()) ;
//                ((MainActivity)context).getgif(holder.img_child);
//                ((MainActivity)context).name=i +"_"+
//                        (position + 1) +".gif";
//                ((MainActivity)context).show_emoji(context.getExternalCacheDir() +"/cache"+i +"_"+
//                        (position + 1) +".webp", fav_s_fragment);
            }
        });
        if (!(sp.getString("switch", "").equals(""))) {
            holder.textView1.setTextColor(Color.parseColor("#F8f8ff"));
           // holder.cardView.setCardBackgroundColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        //SimpleDraweeView draweeWebpAnimated;
        LinearLayout linearchild;
        ImageView imagenew;
        ImageView img_child;
      //  CardView cardView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textview1);
           // draweeWebpAnimated = itemView.findViewById(R.id.img_child);
            imagenew = itemView.findViewById(R.id.newicon);
            img_child = itemView.findViewById(R.id.img_child);
            linearchild = itemView.findViewById(R.id.child);
        //    cardView  = itemView.findViewById(R.id.cardview1);

        }
    }

}