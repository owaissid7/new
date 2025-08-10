package com.animated.cartoon.stickers.wasticker;

import android.app.AlertDialog;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.Color;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.animated.cartoon.stickers.wasticker.Extra.FileUtil;
import com.animated.cartoon.stickers.wasticker.Extra.ScrollHandler;
import com.animated.cartoon.stickers.wasticker.adapter.AdapterPickup;
import com.bumptech.glide.Glide;

import com.bumptech.glide.integration.webp.decoder.WebpDrawable;

import com.bumptech.glide.request.FutureTarget;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iammert.tileprogressview.TiledProgressView;


import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;

import java.util.Objects;
import java.util.concurrent.ExecutionException;


import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.saveBitmap;
import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.writeFile;

public class PickupFragment extends Fragment {
    private View v;
    private List list = new ArrayList();

    private stickerload stickerload = null;
    private TiledProgressView progressBar;
    private TextView dialog_tex;

    private String path;
    private ImageView whats_icon;
    private ImageView add_key;
    private SharedPreferences sp;
    private  boolean ifwifi;
    private ArrayList<HashMap<String, Object>> favourite = new ArrayList<>();
    ArrayList<String> keyboard =new ArrayList<>();

    private boolean isfav;
    private String mapid_from;
   // private ImageView fav_icon;
    LottieAnimationView lottie;
   // private gifload gifload;
    private  int i ;
    private  int f ;
    public ArrayList<Integer> item = new ArrayList();
    public AdapterPickup adapterMain;
    public ImageView imageView;
    private boolean iskey;
    private RecyclerView recyclerView;
    private String packname;
    public int c =0;
    private GridLayoutManager gridLayoutManager;
    private ImageView grid;
    SwipeRefreshLayout swipe;
    public PickupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void column(){


        if( c ==3){
            sp.edit().putString("column", "4").apply();
            grid.setImageResource(R.drawable.ic_triplle_grid);

            c =4;
            gridLayoutManager = new GridLayoutManager(getActivity(), 4);}
        else{
            grid.setImageResource(R.drawable.ic_four_grid);
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            sp.edit().putString("column", "3").apply();
            c=3;
        }
        recyclerView.setLayoutManager(gridLayoutManager);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v == null) {
            // Glide.with(getActivity()).resumeRequestsRecursive();
            v = inflater.inflate(R.layout.fragment_pickup, container, false);
            recyclerView = v.findViewById(R.id.recycler_pickup);
            if (((MainActivity) getActivity()).data != null) {
                list = Arrays.asList(((MainActivity) getActivity()).data.split(","));

            }
            sp = Objects.requireNonNull(this.getActivity()).getSharedPreferences("sp", Context.MODE_PRIVATE);

            mapid_from = ((MainActivity) getActivity()).mapid;
            adapterMain = new AdapterPickup(list, getActivity(), PickupFragment.this);
            LinearLayout add_whats = v.findViewById(R.id.l_addwhats);
            LinearLayout fav_card = v.findViewById(R.id.fav_card);
            grid = v.findViewById(R.id.img_grid);
            if (sp.getString("column", "").equals("")) {
                sp.edit().putString("column", "4").apply();
                new SimpleTooltip.Builder(getActivity())
                        .anchorView(fav_card).arrowColor(getResources().getColor(R.color.white))
                        .text("Add To Favourite List")
                        .gravity(Gravity.TOP).backgroundColor(getResources().getColor(R.color.white))
                        .animated(true)
                        .arrowHeight(150)
                        .transparentOverlay(false)
                        .build()
                        .show();
                new SimpleTooltip.Builder(getActivity())
                        .anchorView(grid).arrowColor(getResources().getColor(R.color.white))
                        .text("Change Grid Size")
                        .gravity(Gravity.START).backgroundColor(getResources().getColor(R.color.white))
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
                new SimpleTooltip.Builder(getActivity())
                        .anchorView(add_whats).arrowColor(getResources().getColor(R.color.white))
                        .text("Add To WhatsApp")
                        .gravity(Gravity.TOP).backgroundColor(getResources().getColor(R.color.white))
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
            c = Integer.parseInt(sp.getString("column", ""));
            gridLayoutManager = new GridLayoutManager(getActivity(), c);
            recyclerView.setAdapter(adapterMain);
            recyclerView.setLayoutManager(gridLayoutManager);
            TextView textcount = v.findViewById(R.id.textcount);

            whats_icon = v.findViewById(R.id.add_whats);
            lottie = v.findViewById(R.id.lottie_fav);
            //fav_icon = v.findViewById(R.id.imgheart);
            textcount.setText(list.size() + " Stickers");
            TextView title = v.findViewById(R.id.toptitle);


            LinearLayout l_add_key = v.findViewById(R.id.l_addkey);
            add_key = v.findViewById(R.id.add_key);
            path = getActivity().getExternalCacheDir()
                    + "/cache/" + mapid_from + "/";

            if (!(sp.getString("favourite", "").equals(""))) {

                favourite = new Gson().fromJson(sp.getString("favourite", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());

                for (i = 0; i < favourite.size(); i++) {
                    if (favourite.get(i).get("id").toString().equals(mapid_from)) {
                       lottie.setFrame(61);
                      //  fav_icon.setImageResource(R.drawable.favoritefull_24);
                        isfav = true;
                        break;
                    }
                }

            }

            if (!((sp.getString("keyboard", "").equals("")) || (sp.getString("keyboard", "").equals("[]")))) {

                keyboard = new Gson().fromJson(sp.getString("keyboard", ""), new TypeToken<ArrayList<String>>() {
                }.getType());
                for (f = 0; f < keyboard.size(); f++) {
                    if (keyboard.get(f).contains(mapid_from + ",")) {
                        add_key.setImageResource(R.drawable.checked);
                        iskey = true;
                        break;
                    }
                }
            }
            ImageView menu = v.findViewById(R.id.img_menu);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity())._drawer.openDrawer(GravityCompat.START);

                }
            });

             grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  column();

                }
            });
           add_whats.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        v.animate().scaleXBy(-0.05f).setDuration(100).start();
                        v.animate().scaleYBy(-0.05f).setDuration(100).start();
                        return false;

                    } else if (action == MotionEvent.ACTION_UP) {
                        v.animate().cancel();
                        v.animate().scaleX(1f).setDuration(100).start();
                        v.animate().scaleY(1f).setDuration(100).start();
                        return false;
                    }

                    return false;
                }
            });
            add_whats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                   do_whats();
                }
            });
            fav_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    if (isfav) {
                        favourite.remove(i);
                        lottie.setMinAndMaxProgress(0.0f, 0.1f);
                        lottie.playAnimation();
                      //  fav_icon.setImageResource(R.drawable.avorite_border_24);
                        toastx("Removed From Favourite");
                        isfav = false;
                    } else {
                        int n;
                        try {
                            n = Integer.parseInt(((MainActivity) getActivity())
                                    .value) - 1;
                            if (!(n < list.size())) {
                                n = 0;
                            }
                        } catch (NumberFormatException e) {

                            n = 0;
                        }

                        HashMap<String, Object> favouritemapv = new HashMap<>();
                        favouritemapv.put("id", mapid_from);
                        favouritemapv.put("image", list.get(n).toString().trim());
                        favouritemapv.put("value", String.valueOf(n + 1));
                        favouritemapv.put("packname", packname);
                        favouritemapv.put("pack", String.valueOf(((MainActivity) getActivity()).pack));
                        favouritemapv.put("count", String.valueOf(list.size()));
                        favourite.add(favouritemapv);
                        lottie.setMinAndMaxProgress(0.0f, 1.0f);
                        lottie.playAnimation();
                       // fav_icon.setImageResource(R.drawable.favoritefull_24);
                        isfav = true;
                        toastx("Added In Favourite");
                    }
                }
            });
           // final Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.bounce);
           l_add_key.setOnTouchListener(new View.OnTouchListener() {

               @Override
               public boolean onTouch(View v, MotionEvent motionEvent) {
                   int action = motionEvent.getAction();
                   if (action == MotionEvent.ACTION_DOWN) {
                       v.animate().scaleXBy(-0.05f).setDuration(100).start();
                       v.animate().scaleYBy(-0.05f).setDuration(100).start();
                       return false;

                   } else if (action == MotionEvent.ACTION_UP) {
                       v.animate().cancel();
                       v.animate().scaleX(1f).setDuration(100).start();
                       v.animate().scaleY(1f).setDuration(100).start();
                       return false;
                   }

                   return false;
               }
           });
            l_add_key.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View _view) {
                    if (iskey) {
                        keyboard.remove(f);
                        add_key.setImageResource(R.drawable.circle_outline_24);
                        toastx("Removed From KeyBoard");
                        iskey = false;
                    } else {
                        int n;
                        try {
                            n = Integer.parseInt(((MainActivity) getActivity())
                                    .value) - 1;
                            if (!(n < list.size())) {
                                n = 0;
                            }
                        } catch (NumberFormatException e) {

                            n = 0;
                        }

                        keyboard.add(mapid_from + "," + packname + " -" + ((MainActivity) getActivity()).pack +
                                "," + list.get(n).toString().trim());
                        add_key.setImageResource(R.drawable.checked);
                        iskey = true;
                        toastx("Added In KeyBoard");
                        if (Build.VERSION.SDK_INT < 25) {
                            AlertDialog.Builder d = new AlertDialog.Builder(getActivity());
                            d.setTitle("Notice");
                            d.setMessage("Sorry, You are using Older Version of Android.\nStickers KeyBoard Only Works on Android 7.1 (Nougat) or Higher. ");
                            d.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface _dialog, int _which) {

                                }
                            });
                            d.show();
                        } else {
                            InputMethodManager imeManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);

                            if (!(imeManager.getEnabledInputMethodList().toString().contains("com.animated.cartoon.stickers.wasticker"))) {
                                AlertDialog dialogf2 = new AlertDialog.Builder(getActivity()).create();
                                View inflate = getLayoutInflater().inflate(R.layout.dialog_ifkey, null);
                                dialogf2.setView(inflate);
                                dialogf2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                LinearLayout l_addkey = inflate.findViewById(R.id.l_addkey);
                                LinearLayout l_enablekey = inflate.findViewById(R.id.l_enablekey);
                                ImageView close = inflate.findViewById(R.id.close);
                                l_addkey.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new
                                                Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                                l_enablekey.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        InputMethodManager imeManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imeManager.showInputMethodPicker();

                                    }
                                });
                                close.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        dialogf2.dismiss();

                                    }
                                });
                                dialogf2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

                                dialogf2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogf2.dismiss();
                                    }
                                });
                                dialogf2.show();

                            }
                        }

                    }
                }
            });
            packname=((MainActivity)getActivity()).packname;
            title.setText(packname +"  "+ ((MainActivity) getActivity()).pack);

            if (!(sp.getString("switch", "").equals(""))) {
                LinearLayout top = v.findViewById(R.id.linearm);
               // FrameLayout toolbar = v.findViewById(R.id._toolbarz);
                LinearLayout bottom = v.findViewById(R.id.linearb);
                top.setBackgroundColor(Color.parseColor("#000000"));
                bottom.setBackgroundResource(R.drawable.black_round);
               // toolbar.setBackgroundColor(Color.parseColor("#000000"));

            }
            LinearLayout spaceNavigationView =  v.findViewById(R.id.linear_bottom);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) spaceNavigationView.getLayoutParams();
            layoutParams.setBehavior(new ScrollHandler());
            LinearLayout bottom_whats =  v.findViewById(R.id.b_addwhats);
           ImageView bottom_expand =  v.findViewById(R.id.add_expand);



            bottom_expand.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                   spaceNavigationView.animate().translationY(0);
                }
            });
           bottom_whats.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        v.animate().scaleXBy(-0.05f).setDuration(100).start();
                        v.animate().scaleYBy(-0.05f).setDuration(100).start();
                        return false;

                    } else if (action == MotionEvent.ACTION_UP) {
                        v.animate().cancel();
                        v.animate().scaleX(1f).setDuration(100).start();
                        v.animate().scaleY(1f).setDuration(100).start();
                        return false;
                    }

                    return false;
                }
            });
           bottom_whats.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
            do_whats();
                }
            });
           if(c==3) {
              grid.setImageResource(R.drawable.ic_four_grid);
           }else{
               grid.setImageResource(R.drawable.ic_triplle_grid);

           }
            swipe =v.findViewById(R.id.swipe);

            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipe.setRefreshing(false);

            if(getindex()){
                android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (!(networkInfo != null && networkInfo.isConnected())) {
                    wifi_extra();

                }else{
                    adapterMain.notifyDataSetChanged();toastx("Refreshing"); }
            }else{
                adapterMain.notifyDataSetChanged();

            }


                }
            });

        }
        return v;
    }

    private void do_whats() {
        if ((WhitelistCheck.isWhitelisted(getActivity(), mapid_from))) {

            toastx("Sticker Pack Is Already Added");
        } else {
            if (!((appInstalledOrNot(2))||(appInstalledOrNot(5)))) {

                AlertDialog.Builder d = new AlertDialog.Builder(getActivity());

                d.setTitle("Official WhatsApp Not Found");
                d.setMessage("Warning: Either WhatsApp is not Installed or Installed version is UnOfficial.\nYou can still try but it may FAILED to add Sticker in WhatsApp.");
                d.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });

                d.create().show();
            }
            stickerload = new stickerload();
            stickerload.execute();

        }
    }

    public Boolean  getindex(){
        boolean b=false;
        for(int i=0;i<list.size();i++){
            if(!(item.contains(i))){
                b=true;
                break;
            }
        }
        return b;

    }
public  void no_wifi(){
    Toast.makeText(getActivity(), "Sticker Is Not Yet Loaded",Toast.LENGTH_SHORT).show();

    android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    if (!(networkInfo != null && networkInfo.isConnected())) {
        wifi_extra();

    }

}
private void wifi_extra(){
    AlertDialog dialogf2 = new AlertDialog.Builder(getActivity()).create();
    View inflate = getLayoutInflater().inflate(R.layout.nointernet, null);
    dialogf2.setView(inflate);
    dialogf2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    TextView l_retry = inflate.findViewById(R.id.l_retry);
    TextView l_close = inflate.findViewById(R.id.l_close);

    l_retry.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogf2.dismiss();
            adapterMain.notifyDataSetChanged();
        }
    });
    l_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialogf2.dismiss();
        }
    });
    dialogf2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

    dialogf2.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            dialogf2.dismiss();
        }
    });
    dialogf2.show();
}


    private class stickerload extends AsyncTask<Void, String, String> {
        AlertDialog dialogf;

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i < list.size(); i++) {
                FutureTarget<File> future = Glide.with(getActivity())
                        .load(list.get((int) i).toString().trim())
                        .downloadOnly(512, 512);

                File cacheFile = null;
                try {
                    cacheFile = future.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (cacheFile == null) {
                    stickerload.cancel(true);
                } else {
                    FileUtil.copyFile(cacheFile.getAbsolutePath(), path + "stick_" + (long) (i + 1) + ".webp");
                    publishProgress(String.valueOf(i + 1));
                    if (isCancelled()) {
                        break;
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            FileUtil.deleteFile(getActivity().getExternalCacheDir() + "/cache");
            write();
            ifwifi=false;
            dialogf = new AlertDialog.Builder(getActivity()).create();
            View inflate = getLayoutInflater().inflate(R.layout.adload, null);
            dialogf.setView(inflate);
            dialogf.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressBar = inflate.findViewById(R.id.progressbar);
       //     float
          //  progressBar.setMax(list.size());
            progressBar.setProgress(0);
           progressBar.setLoadingColorRes(R.color.colorPrimaryDark);
            dialog_tex = inflate.findViewById(R.id.textprog);
            TextView dialog_close = inflate.findViewById(R.id.textclose);
            dialog_close.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    stickerload.cancel(true);
                    ifwifi = true;
                    dialogf.dismiss();
                }
            });
            dialogf.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialogf.setCanceledOnTouchOutside(false);
            dialogf.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    adapterMain.notifyDataSetChanged();
                    if(!ifwifi) {
                       if( isCancelled() ){
                            android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                            if (!(networkInfo != null && networkInfo.isConnected())) {
                                AlertDialog dialogf2 = new AlertDialog.Builder(getActivity()).create();
                                View inflate = getLayoutInflater().inflate(R.layout.nointernet, null);
                                dialogf2.setView(inflate);
                                dialogf2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                TextView l_retry = inflate.findViewById(R.id.l_retry);
                                TextView l_close = inflate.findViewById(R.id.l_close);

                                l_retry.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        stickerload = new stickerload();
                                        stickerload.execute();
                                        dialogf2.dismiss();
                                    }
                                });
                                l_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogf2.dismiss();
                                    }
                                });
                                dialogf2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

                                dialogf2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        dialogf2.dismiss();
                                    }
                                });
                                dialogf2.show();

                            }else{
                                Toast.makeText(getActivity(), "Sorry, There is some error. Try after some time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            dialogf.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogf.dismiss();
                }
            });
            dialogf.show();
        }

        @Override
        protected void onProgressUpdate(String... strings) {
            super.onProgressUpdate(strings);
            progressBar.setProgress((float)Double.parseDouble(strings[0])*100/list.size());
           // progressBar.setProgress(Integer.parseInt(strings[0]));
            dialog_tex.setText(strings[0] + " / " + list.size());

        }
        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                  WebpDrawable drawable;
                    int i;
                  try{
                   i=Integer.parseInt(((MainActivity) getActivity())
                          .value)-1;
                      if(!(i<list.size())){
                          i=0;
                      }
                  } catch (NumberFormatException e) {
                      i=0;
                  }


                    try {

                        drawable = (WebpDrawable) Glide.with(getActivity()).load(list.get((int) i).toString().trim()).submit().get();

                        Bitmap bitmap = drawable.getFirstFrame();
                    Bitmap bitmap2 =Bitmap.createScaledBitmap(bitmap,96, 96, true);
                        saveBitmap(bitmap2, path + "stick_image.png");
                        ((MainActivity) getActivity()).set(packname + " - " +((MainActivity) getActivity()).pack);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });


            dialogf.dismiss();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            dialogf.dismiss();
           // toastx("cancelled");
        }


    }

    private void write() {
        String json = "{\"android_play_store_link\":\"https://play.google.com/store/apps/details?id=com.animated.cartoon.stickers.wasticker\",\n" +
                "        \"ios_app_store_link\": \"\",\n" +
                "        \"sticker_packs\": [\n" +
                "          {\n" +
                "            \"publisher\": \"OWAIS SID\",\n" +
                "            \"tray_image_file\": \"stick_image.png\",\n" +
                "            \"image_data_version\": \"1\",\n" +
                "            \"avoid_cache\": false,\n" +
                "            \"publisher_email\": \"\",\n" +
                "            \"publisher_website\": \"\",\n" +
                "            \"privacy_policy_website\": \"\",\n" +
                "            \"license_agreement_website\": \"\",\n" +
                "            \"animated_sticker_pack\": true,\n" +
                "            \"identifier\": \"idx\",\n" +
                "            \"name\":\"namex\",\n" +
                "            \"stickers\": ";
        String mjson =
                json.replace("idx",
                        mapid_from).replace("namex",
                        packname + " " + ((MainActivity) getActivity()).pack);

        StringBuilder datax = new StringBuilder();
        datax.append("[");
        for (int i = 1; i < list.size() + 1; i++) {
            if (list.size() == i) {
                String x = "{\n          \"image_file\": \"stick_".concat
                        (String.valueOf(i).concat(".webp\",\n          \"emojis\": [\n            \"☕\",\n            \"🙂\"\n          ]\n        }"));
                datax.append(x);
            } else {
                String x = "{\n          \"image_file\": \"stick_".concat
                        (String.valueOf(i).concat(".webp\",\n          \"emojis\": [\n            \"☕\",\n            \"🙂\"\n          ]\n        },"));
                datax.append(x);
            }
        }
        writeFile(getActivity().getExternalCacheDir()
                + "/caches", mjson + datax + "]}]}");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WhitelistCheck.isWhitelisted(getActivity(), mapid_from)) {
            whats_icon.setImageResource(R.drawable.checked);

        } else {
            whats_icon.setImageResource(R.drawable.circle_outline_24);
        }

    }







    @Override
    public void onStop() {
        super.onStop();
        sp.edit().putString("favourite", new Gson().toJson(favourite)).commit();
        sp.edit().putString("keyboard", new Gson().toJson(keyboard)).commit();
      //  toastx("destroy");
        if (stickerload != null) {
            if (stickerload.getStatus() == AsyncTask.Status.RUNNING) {
                stickerload.cancel(true);
            }
        }
    // stopgifload();
      //  Glide.with(getActivity()).pauseAllRequestsRecursive();
      // Glide.with(getActivity()).onStart();
        //adapterMain.kill();
        recyclerView.setAdapter(adapterMain);

}







    private boolean appInstalledOrNot(int i) {
        String uri;
   switch (i) {
       case 3 : uri="com.instagram.android";
       break;
       case 4 : uri="com.facebook.orca";
       break;
       case 5 : uri="com.whatsapp.w4b";
       break;



       default:
           uri="com.whatsapp";
   }
        android.content.pm.PackageManager pm = getActivity().getPackageManager();
        try {

            pm.getPackageInfo(uri, android.content.pm.PackageManager.GET_ACTIVITIES); return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) { }

        return false;
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
//            main_share(5);
//
//        } else {
//            toastx("Storage Permission Required!");
//        }
//    }

    private void toastx(String s){

        ((MainActivity)getActivity()).mtoast(s);
    }

}


