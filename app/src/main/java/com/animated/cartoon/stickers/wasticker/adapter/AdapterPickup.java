package com.animated.cartoon.stickers.wasticker.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.animated.cartoon.stickers.wasticker.Fav_s_Fragment;
import com.animated.cartoon.stickers.wasticker.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.animated.cartoon.stickers.wasticker.PickupFragment;
import com.animated.cartoon.stickers.wasticker.R;

import java.util.List;
public class AdapterPickup extends  RecyclerView.Adapter<AdapterPickup.viewHolder> {



    public AdapterPickup(List list, Context context, PickupFragment pickupFragment) {
        this.list = list;
        this.context = context;
        this.pickupFragment = pickupFragment;
    }

    List list;
Context context;
PickupFragment pickupFragment;
// for optional
    Fav_s_Fragment fav_s_fragment;



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_pickup,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

//
//        ControllerListener listener = new BaseControllerListener<ImageInfo>() {
//
//            @Override
//            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
//           //Toast.makeText(getApplicationContext(), "lllll",Toast.LENGTH_SHORT).show();
//
//
//            }
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//             //   Toast.makeText(getApplicationContext(), "fffff",Toast.LENGTH_SHORT).show();
//
//            }
//
//        };
//        final ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(Uri.parse(list.get((int)position).toString().trim()))
//
//                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//
//                // .setResizeOptions(new ResizeOptions(512, 512))
//                .build();

 //        BaseControllerListener baseControllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                if (animatable != null && AnimatedDrawable2.class.isInstance(animatable)) {
//                    final AnimatedDrawable2 animatedDrawable2 = (AnimatedDrawable2) animatable;
//                    final int totalCnt = animatedDrawable2.getFrameCount();
//
//                    animatedDrawable2.setAnimationListener(new BaseAnimationListener() {
//                        private int lastFrame; //Prevent infinite loop and exit the animation in time
//                        boolean repeat;
//
//                        @Override
//                        public void onAnimationFrame(AnimatedDrawable2 drawable, int frameNumber) {
//
//
//
//
//                            if (!(lastFrame == 0 && totalCnt <= 1) && lastFrame <= frameNumber) {
//                                lastFrame = frameNumber;
//
//
//                            } else {
//                                animatedDrawable2.stop();
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onAnimationStart(AnimatedDrawable2 drawable) {
//                            lastFrame = -1;
//                        }
//
//                        @Override
//                        public void onAnimationStop(AnimatedDrawable2 drawable) {
//                            if (!pickupFragment.repeat) {
//                                if (!repeat) {
//                                    animatedDrawable2.start();
//                                    repeat = true;
//                                } else {
//                                    animatedDrawable2.stop();
//                                }
//                            } else {
//                                animatedDrawable2.stop();
//
//                            }
//                        }
//                    });
//                }
//                if (!(isExistFile(holder.path + position + 1 + ".webp"))){
//                    CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(request, null);
//                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
//               // File file = ((FileBinaryResource) resource).getFile();
//              //  FileUtil.copyFile(file.toString(), holder.path +"stick_"+ (long)(position + 1) + ".webp");
//            }
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//
//            }
//       };

//        holder.draweeWebpAnimated.setController(
//                Fresco.newDraweeControllerBuilder()
//                          .setImageRequest(request)
//                        .setAutoPlayAnimations(false)
//                        .setControllerListener(baseControllerListener)
//                        .setOldController(holder.draweeWebpAnimated.getController())
//                      //  .setUri(list.get((int)position).toString().trim())
//                        .build());


        Glide.with(context)
                .load(list.get(position).toString().trim())
               .placeholder(R.drawable.stickersample
                ).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                pickupFragment.item.add(position);
                return false;

            }

        })
                .into(holder.sample);
      pickupFragment.imageView = holder.sample;
       holder.linearchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if((pickupFragment.item.contains(position))){
                 //   pickupFragment.show_emoji(list.get( position).toString().trim());
                    ((MainActivity)context).show_emoji(list.get( position).toString().trim(),fav_s_fragment);

                }else{
                    pickupFragment.no_wifi();


                }

//                AsyncTask.execute(new Runnable() {
//                @Override
//               public void run() {
//                    try {
//                        Drawable drawable = (WebpDrawable) Glide.with(context).load(list.get((int)
//                                Integer.parseInt(((MainActivity) context)
//                                        .value)).toString().trim()).onlyRetrieveFromCache(true).submit().get();
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                            }
//                        });
//
//                    } catch (ExecutionException | InterruptedException e) {
//                    }
//
//                }});
            }
        });

    }


    @Override
    public void onViewDetachedFromWindow(@NonNull viewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(((MainActivity)context).frameLayout.getVisibility()==View.GONE) {
            Glide.with(context).clear(holder.sample);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
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
