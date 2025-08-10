package com.animated.cartoon.stickers.wasticker;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.animated.cartoon.stickers.wasticker.Extra.AnimatedGifMaker;
import com.animated.cartoon.stickers.wasticker.Extra.FileUtil;
import com.animated.cartoon.stickers.wasticker.Fragment.FavFragment;
import com.animated.cartoon.stickers.wasticker.Fragment.Homeragment;
import com.animated.cartoon.stickers.wasticker.Fragment.SettingFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.WebpImage;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpFrameCacheStrategy;
import com.bumptech.glide.integration.webp.decoder.WebpFrameLoader;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.copyFile;
import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.copyFilenew;
import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.copyFileuri;
import static com.animated.cartoon.stickers.wasticker.Extra.FileUtil.isExistFile;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;



import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout _drawer;
    private LinearLayout linear_main;
    public FrameLayout frameLayout;
    private FragmentManager manager;
    private PickupFragment pickupFragment;
    public HashMap<String, Object> map = new HashMap<>();
    public String data;
    public String pack;
    public String value;
    public String mapid;
    private AlertDialog dialogf2 = null;
    private ImageView img_main;
    private  boolean canshare= false;
    private boolean saved;

    private SharedPreferences sp;
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private  FragmentAdapter fragmentAdapter;
    private DatabaseReference firebase = _firebase.getReference("mine");
    public ArrayList<HashMap<String, Object>> homelist = new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
    private ViewPager ViewPager;
    private boolean run = false;

    private TimerTask t;
    public  String packname ="";

    TabLayout tabLayout;
    String uri = null;
    private FirebaseAnalytics mFirebaseAnalytics;
private String AES = "AES";



 //public native static void main();
 //public native String HelloJNI();


    public void enable(Boolean b) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.setExpanded(b, true);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = this.getSharedPreferences("sp", Context.MODE_PRIVATE);

        initialize(savedInstanceState);


        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

         ViewPager = findViewById(R.id.viewpaer_main);

        tabLayout = findViewById(R.id.tab);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        fragmentAdapter.addfagment(new Homeragment(), "Category");
        fragmentAdapter.addfagment(new FavFragment(), "Favourite");
        fragmentAdapter.addfagment(new SettingFragment(), "KeyBoard");
        ViewPager.setAdapter(fragmentAdapter);
        ViewPager.setSaveFromParentEnabled(false);
        tabLayout.setupWithViewPager(ViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.category2);
        tabLayout.getTabAt(1).setIcon(R.drawable.heart_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.keyboardsetting);


        //     Transformation<Bitmap> circleCrop = new CircleCrop();
//        Glide.with(getApplicationContext())
//                .load("https://firebasestorage.googleapis.com/v0/b/animated-cartoon-stick.appspot.com/o/image%2Fdoremon_1_18.webp?alt=media&token=f5f1ab9b-0064-4629-83dc-45003e060dd4")
//               .into(samle);
        manager = getSupportFragmentManager();

        setStatusBarGradiant(this);
//        ViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                if(run) {
//                    if (position == 1) {
////                        FavFragment fragment = (FavFragment) ViewPager.getAdapter().instantiateItem(ViewPager, position);
////                        fragment.onStart();
//                    }
////                    if (position == 1) { // 0 = the first fragment in the ViewPager, in this case, the fragment i want to refresh its UI
////                        FavFragment fragment = (FavFragment) ViewPager.getAdapter().instantiateItem(ViewPager, position);
////                        fragment.onStart(); // here i call the onResume of the fragment, where i have the method updateUI() to update its UI
////                        //   ViewPager.getAdapter().notifyDataSetChanged();
////                    }
//                }
//            }
//        });

  // main();
      //  toastx(HelloJNI());
        fire();
                        File webpFile = new File(getApplicationContext().getExternalCacheDir()
                                + "/cache.webp");
                        byte[] data = new byte[(int) webpFile.length()];
                        try {
                            new FileInputStream(webpFile).read(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//        WebPEncoder encoder = new WebPEncoder();
//
//        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
//                        WebpImage webp = WebpImage.create(data);
//                        int sampleSize = 1;
//                        WebpFrameCacheStrategy cacheStrategy = (WebpFrameCacheStrategy) (new Options()).get(WebpFrameLoader.FRAME_CACHE_STRATEGY);
//                        MemorySizeCalculator memorySizeCalculator = (new MemorySizeCalculator.Builder(getApplicationContext())).build();
//                        // Intrinsics.checkNotNullExpressionValue(memorySizeCalculator, "memorySizeCalculator");
//                        long bitmapPoolSize = (long) memorySizeCalculator.getBitmapPoolSize();
//                        BitmapPool bitmapPool = bitmapPoolSize > 0L ? (BitmapPool) (new LruBitmapPool(bitmapPoolSize)) : (BitmapPool) (new BitmapPoolAdapter());
//                        LruArrayPool arrayPool = new LruArrayPool(memorySizeCalculator.getArrayPoolSizeInBytes());
//                        GifBitmapProvider gifBitmapProvider = new GifBitmapProvider(bitmapPool, (ArrayPool) arrayPool);
//                        WebpDecoder webpDecoder = new WebpDecoder((com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider) gifBitmapProvider, webp, byteBuffer, sampleSize, cacheStrategy);
//                        int frameCount = webpDecoder.getFrameCount();
//                        webpDecoder.advance();
//
//
//                            for (int i = 0; i < frameCount + 1; i++) {
//                                Bitmap b = Bitmap.createScaledBitmap(webpDecoder.getNextFrame(), 512, 512, true);
////                    saveBitmap(webpDecoder.getNextFrame(),
////                            getApplicationContext().getExternalCacheDir()
////                                    + "/cache" + i + ".png");
//                                encoder.addFrame(b, 0,0, 90);
//
//                                webpDecoder.advance();
//                            }
//
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                final byte[] ret = encoder.build();
//                File File = new File(getApplicationContext().getExternalCacheDir()
//                        + "/XXXXX.webp");        FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(File);
//                    fos.write(ret);
//                    fos.flush();
//                    fos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//

//    });

        if (!(sp.getString("switch", "").equals(""))) {

           linear_main.setBackgroundColor(Color.parseColor("#131313"));
        }
        LinearLayout Linear_bottom =  findViewById(R.id.linear_bottom);
        Linear_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Homeragment fragment = (Homeragment) ViewPager.getAdapter().instantiateItem(ViewPager, 0);

                fragment.recyclerView.smoothScrollToPosition(0);
                enable(true);
            }
        });
        if ((sp.getString("v6", "").equals(""))) {
            sp.edit().putString("v6","x").apply();

            new SimpleTooltip.Builder(this)
                    .anchorView(tabLayout).arrowColor(getResources().getColor(R.color.white))
                    .text("Explore Saved Favourite Stickers")
                    .gravity(Gravity.BOTTOM).backgroundColor(getResources().getColor(R.color.white))
                    .animated(true).transparentOverlay(false)
                    .build().show();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        try {
//            keys();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

//    private void keys() throws Exception {
//        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] bytes = "owais".getBytes(StandardCharsets.UTF_8);
//        digest.update(bytes);
//        byte[] keys = digest.digest();
//        SecretKeySpec secretKeySpec = new SecretKeySpec(keys,"AES");
//        Cipher c = Cipher.getInstance(AES);
//        c.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//        byte[] encVal = c.doFinal(FileUtil.loadContentFromFile(getApplicationContext(), "cache/c").getBytes());
//    FileUtil.writeFile(getApplicationContext().getExternalCacheDir() + "/cache/c.txt", Base64.encodeToString(encVal, Base64.DEFAULT));
//    decryypt(Base64.encodeToString(encVal, Base64.DEFAULT));
//    }
//
    private String decryypt(String s) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = "owais".getBytes(StandardCharsets.UTF_8);
        digest.update(bytes);
        byte[] keys = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keys,"AES");
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] dec = Base64.decode(s, Base64.DEFAULT);
        byte [] decode = c.doFinal(dec);
        String value = new String(decode);
      //  FileUtil.writeFile(getApplicationContext().getExternalCacheDir() + "/cache/c_d.txt", value);
  return value;
    }

    public void fire(){

    android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected()) {
        firebase.child("root").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!run) {
                    if (snapshot.getChildrenCount() > 0) {

                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                        };
                        //  final String _childKey = snapshot.getKey();


                        final HashMap<String, Object> _childValue = snapshot.getValue(_ind);

                        if(_childValue.containsKey("map")&&_childValue.containsKey("json")) {
                            run = true;
                            String str = (new Gson()).toJson(_childValue.get("map"),
                                    new TypeToken<HashMap<String, Object>>() {
                                    }.getType());
                            String str2 = (new Gson()).toJson(_childValue.get("json"),
                                    new TypeToken<ArrayList<HashMap<String, Object>>>() {
                                    }.getType());
                            map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>() {
                            }.getType());

                            //


                            homelist = new Gson().fromJson(str2, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                            }.getType());
                            sp.edit().putString("map", new Gson().toJson(map)).commit();
                            sp.edit().putString("json", new Gson().toJson(homelist)).commit();




                            ViewPager.setVisibility(View.VISIBLE);


                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                           Timer _timer = new Timer();
                            t = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override public void run() {
                                            failed();
                                            t.cancel();
                                        }
                                    }); }};

                            _timer.schedule(t, (int)(200));

                        }
                        else{
                            failed();
                        }
                    } else{
                    failed();}
                }else{
                    failed();}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
              failed();
            }
        });

        Timer _timer = new Timer();
        t = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                 if(!run){
                     failed();
                 }
                        t.cancel();
                    }
                }); }};

        _timer.schedule(t, (int)(6000));

    }else{
        failed();
    }

}



    public void failed() {
        Log.d("ggg", "xxxx");
        run = true;
        if (sp.getString("map", "").equals("")) {
            try {
                sp.edit().putString("map", decryypt(FileUtil.loadContentFromFile(getApplicationContext(), "cache/m"))).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sp.getString("json", "").equals("")) {
            try {
            sp.edit().putString("json", decryypt(FileUtil.loadContentFromFile(getApplicationContext(), "cache/c"))).commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        homelist = new Gson().fromJson(sp.getString("json", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        map = new Gson().fromJson(sp.getString("map", ""), new TypeToken<HashMap<String, Object>>() {
        }.getType());
//    fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
//            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
//    fragmentAdapter.addfagment(new Homeragment(), "Category");
//    fragmentAdapter.addfagment(new FavFragment(), "Favourite");
//    fragmentAdapter.addfagment(new SettingFragment(), "KeyBoard");
        ViewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(ViewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.category2);
        tabLayout.getTabAt(1).setIcon(R.drawable.heart_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.keyboardsetting);
        ViewPager.setCurrentItem(0);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        ViewPager.setVisibility(View.VISIBLE);
        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if(tab.getPosition()==1) {
                        FavFragment fragment = (FavFragment) ViewPager.getAdapter().instantiateItem(ViewPager, 1);
                        fragment.onStartx();
                    }
                    int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
                    if (tab != null) {
                        try {
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        }catch (Exception ignored){

                        }
                        }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                    if (tab != null) {
                        try {
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                    }catch(Exception ignored){

                    }
                }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }


    public void set(String s) {
     //   Toast.makeText(getApplicationContext(), "Please Wait...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", mapid);
        intent.putExtra("sticker_pack_authority",
                "com.animated.cartoon.stickers.wasticker.stickercontentprovider");
        intent.putExtra("sticker_pack_name", s);
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {

          //  Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();


        }

    }



    public void add_pickup( String mapid, String pack, String value) {

        data = map.get(mapid).toString();
        this.mapid = mapid;
        this.pack=pack;
        this.value=value;

        pickupFragment = new PickupFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, pickupFragment).commit();

        frameLayout.setVisibility(View.VISIBLE);
        linear_main.setVisibility(View.GONE);



    }
    public void onBackPressedx() {
        _drawer.closeDrawer(GravityCompat.START);
        if (linear_main.getVisibility()==View.GONE) {
            manager.beginTransaction().remove(pickupFragment).commit();
            linear_main.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            FileUtil.deleteFile(getApplicationContext().getExternalCacheDir() + "/cache");
        }
    }
    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
         else {
            if (frameLayout.getVisibility() == View.VISIBLE) {

                manager.beginTransaction().remove(pickupFragment).commit();
                linear_main.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);

                FileUtil.deleteFile(getApplicationContext().getExternalCacheDir() + "/cache");


            }else{

                   AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
                    d.setTitle("Close App");
                    d.setMessage("Do You Want to Exit?");
                    d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {


                        }
                    });

                    d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            finishAffinity();
                            int pid = android.os.Process.myPid();
                            android.os.Process.killProcess(pid);
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                        }
                    });
                    d.create().show();
                }



        }

    }
    private void _Copy() {
        ClipboardManager clipboard =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); ClipData clip = ClipData.newPlainText("Copied Text", "mohdowaissid7@gmail.com");
            clipboard.setPrimaryClip(clip);
    }

    private void _shareText () {
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Download this Amazing Animated Cartoon Stickers App Contain More than 1500 Animated Cartoon Stickers (1500+) which can be added in WhatsApp at :-\nhttps://play.google.com/store/apps/details?id=com.animated.cartoon.stickers.wasticker\nInstall Now");

        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);

        startActivity(shareIntent);
    }

    private void initialize(Bundle savedInstanceState) {



        linear_main = findViewById(R.id.layout_main);
        frameLayout = findViewById(R.id.fragment_main);

        _drawer = (DrawerLayout) findViewById(R.id._drawer);

        ImageView menu = findViewById(R.id.img_menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _drawer.openDrawer(GravityCompat.START);

            }
        });

        LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);

      LinearLayout  linearhome = (LinearLayout) _nav_view.findViewById(R.id.linearhome);
      LinearLayout  linearfav = (LinearLayout) _nav_view.findViewById(R.id.linearfav);
      LinearLayout  linearkey = (LinearLayout) _nav_view.findViewById(R.id.linearkeyboard);
      LinearLayout  linearcon = (LinearLayout) _nav_view.findViewById(R.id.linearcontact);
      LinearLayout  linearshare = (LinearLayout) _nav_view.findViewById(R.id.linearshared);
      LinearLayout  lineardark = (LinearLayout) _nav_view.findViewById(R.id.lineardark);
      LinearLayout  linearcache = (LinearLayout) _nav_view.findViewById(R.id.linearcache);
      LinearLayout  linearm = (LinearLayout) _nav_view.findViewById(R.id.linearm);
      LinearLayout  linearpolicy = (LinearLayout) _nav_view.findViewById(R.id.linearpolicy);
      Switch switch1 = _nav_view.findViewById(R.id.switch1);
        if (sp.getString("switch", "").equals("")) {
          switch1.setChecked(false);
        }else{
            switch1.setChecked(true);

        }
        linearcache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(getApplicationContext()
                        .getCacheDir() + "/image_manager_disk_cache");
                long size = 0;
                if (f.listFiles() !=null) {
                    for (File file : f.listFiles()) {
                        size += file.length();

                    }
                    size =  size / 1048576;

                }

                AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
                d.setTitle("Clear Cache");
                d.setMessage("Do You Want to Clear Cache?");

                d.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d.setPositiveButton("Clear "+size+"mb", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                              Glide.get(getApplicationContext()).clearDiskCache();
                            }
                        });
                        toastx( "Cleared");
                    }
                });
                d.create().show();
            }
        });
      linearhome.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              ViewPager.setCurrentItem(0);

              onBackPressedx();

          }
      });
      linearfav.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
          ViewPager.setCurrentItem(1);
              onBackPressedx();
          }
      });
        linearkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewPager.setCurrentItem(2);
                onBackPressedx();
            }
        });
        linearcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               _drawer.closeDrawer(GravityCompat.START);
                AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
                d.setTitle("Contact Us - Send Feedback");
                d.setMessage("You can share your honorable opinion, problem you find in this app and your feedbacks to help us so that we can improves our App \nat Email:\nmohdowaissid7@gmail.com\n\nThankyou");
                d.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                    }
                });
                d.setNeutralButton("Copy Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _Copy();
                        Toast.makeText(MainActivity.this, "Email Copied in Clipboard", Toast.LENGTH_SHORT).show();
                    }
                });
                d.create().show();


            }
        });
        linearpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent();
                in.setAction(Intent.ACTION_VIEW);
                in.setData(Uri.parse("https://emojisprivacy.blogspot.com/2021/05/animated-cartoon-sticker-for-whatsapp.html"));
                startActivity(in);


            }
        });
        linearshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _shareText();
            }
        });       _drawer.closeDrawer(GravityCompat.START);
 linearm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        lineardark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(switch1.isChecked()){
                switch1.setChecked(false);
            }else{
                switch1.setChecked(true);

            }
            }
        });
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sp.edit().putString("switch", "1").apply();
                }else{
                    sp.edit().putString("switch", "").apply();
                }
                finish();
                startActivity(getIntent());
            }
        });
        linearm.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent motionEvent) {
                int action = motionEvent.getAction();if (action == MotionEvent.ACTION_DOWN) {
                    v.animate().scaleXBy(-0.06f).setDuration(100).start();
                    v.animate().scaleYBy(-0.06f).setDuration(100).start();
                    return false;
                } else if (action == MotionEvent.ACTION_UP) { v.animate().cancel();
                    v.animate().scaleX(1f).setDuration(100).start();
                    v.animate().scaleY(1f).setDuration(100).start();
                    return false;
                }return false; }});

    }

    public static void setStatusBarGradiant(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_tab);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            ((Window) window).setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);

        }
    }
    private void webp2gif(int i) {
        final File[] webpFile = {null};

            if (uri != null) {
                toastx("Converting, Wait...");
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        FutureTarget<File> future = Glide.with(getApplicationContext())
                                .load(uri).downloadOnly(512, 512);
                        try { webpFile[0] = future.get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace(); }
                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                        File fileout = new File(getApplicationContext().getExternalCacheDir()
                                + "/cache.gif");
                        byte[] data = new byte[(int) webpFile[0].length()];
                        try {
                            new FileInputStream(webpFile[0]).read(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                        WebpImage webp = WebpImage.create(data);
                        int sampleSize = 1;
                        WebpFrameCacheStrategy cacheStrategy = (WebpFrameCacheStrategy) (new Options()).get(WebpFrameLoader.FRAME_CACHE_STRATEGY);
                        MemorySizeCalculator memorySizeCalculator = (new MemorySizeCalculator.Builder(getApplicationContext())).build();
                        // Intrinsics.checkNotNullExpressionValue(memorySizeCalculator, "memorySizeCalculator");
                        long bitmapPoolSize = (long) memorySizeCalculator.getBitmapPoolSize();
                        BitmapPool bitmapPool = bitmapPoolSize > 0L ? (BitmapPool) (new LruBitmapPool(bitmapPoolSize)) : (BitmapPool) (new BitmapPoolAdapter());
                        LruArrayPool arrayPool = new LruArrayPool(memorySizeCalculator.getArrayPoolSizeInBytes());
                        GifBitmapProvider gifBitmapProvider = new GifBitmapProvider(bitmapPool, (ArrayPool) arrayPool);
                        WebpDecoder webpDecoder = new WebpDecoder((com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider) gifBitmapProvider, webp, byteBuffer, sampleSize, cacheStrategy);
                        int frameCount = webpDecoder.getFrameCount();
                        webpDecoder.advance();
                        if (canshare) {

                            for (int i = 0; i < frameCount + 1; i++) {
                                Bitmap b = Bitmap.createScaledBitmap(webpDecoder.getNextFrame(), 350, 350, true);
//                    saveBitmap(webpDecoder.getNextFrame(),
//                            getApplicationContext().getExternalCacheDir()
//                                    + "/cache" + i + ".png");
                                bitmaps.add(b);
                                webpDecoder.advance();
                            }

                            webpDecoder.advance();

                            if (canshare) {
                                encodeGif(bitmaps, fileout, webpDecoder.getDelay(1));
                                saved = true;

                                if (canshare) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            share(i);
                                        }
                                    });

                                }
                            }
                        }
                    }
                });
            }

    }
    public void show_emoji(String uri, Fav_s_Fragment fav_s_fragment) {
        if (dialogf2 == null){
            this.uri= uri;
            img_main = null;
            // private String uri;
            saved= false;
            canshare=true;
            final boolean[] dorefresh = {false};
            dialogf2 = new AlertDialog.Builder(MainActivity.this).create();

            final View[] inflate = {getLayoutInflater().inflate(R.layout.sticker_dialog, null)};
            Window window = dialogf2.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            dialogf2.setView(inflate[0]);
            dialogf2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //SwitchMaterial switch2 = inflate[0].findViewById(R.id.switch2);

            ImageView img_main = inflate[0].findViewById(R.id.img_sticker);
            ImageView img_cancel = inflate[0].findViewById(R.id.img_cancel);
            LinearLayout l_whatsapp = inflate[0].findViewById(R.id.l_whatsapp);
            LinearLayout l_save = inflate[0].findViewById(R.id.l_gallery);
            LinearLayout l_share = inflate[0].findViewById(R.id.l_share);
            LinearLayout l_fav = inflate[0].findViewById(R.id.l_fav_s);
            LottieAnimationView lottie = inflate[0].findViewById(R.id.lottie_fav);
            //
            LinearLayout share_whats = inflate[0].findViewById(R.id.share_whats);
            LinearLayout share_instagram = inflate[0].findViewById(R.id.instagram);
            LinearLayout share_messenger = inflate[0].findViewById(R.id.messenger);
            final boolean[] isfav = {false};
            LinearLayout linear_share = inflate[0].findViewById(R.id.linear_share);
//            switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                   //xx isSwitch= b;
//
//                }
//            });
            Glide.with(getApplicationContext())
                    .load(uri)
                    .placeholder(R.drawable.stickersample
                    ).error(R.drawable.ic_round_error_24)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                       //     gifload = new gifload();
                          //  gifload.execute(img_main);
                            return false;
                        }})
                    .into(img_main);
            ArrayList<String> mapitem =new ArrayList<>();
            if (!(sp.getString("mapitem", "").equals(""))) {
                mapitem = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {}.getType());
                if (mapitem.contains(uri)) {
                    lottie.setFrame(61);
                    isfav[0] = true;
                }
            }

            img_cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialogf2.cancel();
                    dialogf2.dismiss();
                }
            });
            l_whatsapp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    linear_share.setVisibility(View.VISIBLE);
                }
            });
            ArrayList<String> finalMapitem = mapitem;
            l_fav.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dorefresh[0] = true;
                    if (isfav[0]) {

                        finalMapitem.remove(uri);
                        lottie.setMinAndMaxProgress(0.0f, 0.1f);
                        lottie.playAnimation();

                        sp.edit().putString("mapitem", new Gson().toJson(finalMapitem)).commit();
                        toastx("Removed from Favourite");
                        isfav[0] = false;
                    } else {
                        finalMapitem.add(uri);
                        lottie.setMinAndMaxProgress(0.0f, 1.0f);
                        lottie.playAnimation();
                        sp.edit().putString("mapitem", new Gson().toJson(finalMapitem)).commit();

                        isfav[0] = true;
                        toastx("Added In Favourite Sticker List");
                    }
                }
            });

            l_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // ((MainActivity)getApplicationContext()).getpermission();
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                            || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        toastx("Allow Storage Permission");
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    }else{
                        main_share(5);
                    }

                }
            });
            l_share.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    main_share(1);
                }
            });
            share_whats.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(appInstalledOrNot(2)){
                        main_share(2);
                    }
                    else{
                        toastx("Sorry, App Not Found");
                    }
                }
            });
            share_instagram.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(appInstalledOrNot(3)){
                        main_share(3);
                    }
                    else{
                        toastx("Sorry, App Not Found");
                    }

                }
            });
            share_messenger.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(appInstalledOrNot(4)){
                        main_share(4);
                    }
                    else{
                        toastx("Sorry, App Not Found");
                    }

                }
            });

            dialogf2.setCanceledOnTouchOutside(true);
            dialogf2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            dialogf2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogf2 = null;
                    canshare=false;

                    if (linear_main.getVisibility() == View.VISIBLE) {
                        if (dorefresh[0]) {
                            fav_s_fragment.refresh();
                        }
                    }
                }
            });
            dialogf2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogf2.dismiss();
                }
            });
            if (!(sp.getString("switch", "").equals(""))) {
                LinearLayout back = inflate[0].findViewById(R.id.linear_back);

                back.setBackgroundResource(R.drawable.black_round);

            }
            dialogf2.show();
        }
    }
    private void toastx(String s) {
        Toast toast=  Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();    }


    public void encodeGif(List<Bitmap> bitmaps, File outFile, int count) {
        try {
            //deleteFile(outFile.toString());
            FileOutputStream out = new FileOutputStream(outFile);
            AnimatedGifMaker gifs = new AnimatedGifMaker();
            gifs.start(out);
            if(count== 0){
            gifs.setDelay(250);}
            else{
                gifs.setDelay(count);
            }
            gifs.setRepeat(0);
            gifs.setTransparent(new Color());
            for (int i = 0; i < bitmaps.size(); i++) {
                gifs.addFrame(bitmaps.get(i));
            }
            gifs.finish();
        } catch (IOException err) {
        }
    }

    private void main_share(int i) {

        if (saved) {
            share(i);
        } else {
            webp2gif(i);
        }


    }

    private void share(int i) {
          File  cacheFile = new File(getApplicationContext().getExternalCacheDir() + "/cache.gif");
            if (isExistFile(cacheFile.toString())){
            if(i==5){
                Random ran= new Random();
                   File  output_path = new File(FileUtil.getExternalStorageDir()+"/DCIM/Animated Sticker","sticker_"+ ran.nextInt(1000) +".gif");
                File path= new File(FileUtil.getExternalStorageDir()+"/DCIM/Animated Sticker");
                if (!FileUtil.isDirectory(path.toString())) {
                    path.mkdirs(); }
               copyFile(cacheFile.toString(),output_path.toString());

                if(isExistFile(output_path.toString())){
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{output_path.getAbsolutePath()},null ,
                            new  MediaScannerConnection.OnScanCompletedListener(){
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                }});
                    toastx("Saved in " + output_path.toString());}
                else{ toastx("file not saved !");
                }
            }else {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".MyFileProvider", cacheFile);
                sharingIntent.setType("image/gif");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent chooserIntent = Intent.createChooser(sharingIntent, "Share using");
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                switch (i) {
                    case 1:
                        break;
                    case 2:
                        sharingIntent.setPackage("com.whatsapp");
                        break;
                    case 3:
                        sharingIntent.setPackage("com.instagram.android");
                        break;
                    default:
                        sharingIntent.setPackage("com.facebook.orca");
                        break;
                }
                startActivity(chooserIntent);
                toastx("Sharing");
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (Boolean.parseBoolean("true")) {
                        //error should be shown to developer only, not users.
                    }
                    if(validationError.contains("supported")){
                        AlertDialog dialogf2 = new AlertDialog.Builder(MainActivity.this).create();
                        View inflate = getLayoutInflater().inflate(R.layout.dialog_up_whats, null);
                        dialogf2.setView(inflate);
                        dialogf2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        LinearLayout l_addkey = inflate.findViewById(R.id.l_addkey);

                        ImageView close = inflate.findViewById(R.id.close);
                        l_addkey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent ina = new Intent();
                                ina.setAction(Intent.ACTION_VIEW);
                                ina.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
                                startActivity(ina);

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
                        dialogf2.setCancelable(false);
                        dialogf2.show();
                    }
                    Toast.makeText(getApplicationContext(), validationError, Toast.LENGTH_SHORT).show();
                    Log.d("TAGxy", "Validation failed:" + validationError);
                }
            }
        }


    }

//    private void uritofile(Uri uri) throws FileNotFoundException {
//        ContentResolver resolver = getContentResolver();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".png");
//        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
//                Environment.DIRECTORY_PICTURES+ File.separator+"WebPTool");
//        Uri finaluri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        OutputStream fileOutputStream =
//                resolver.openOutputStream(Objects.requireNonNull(finaluri));
//        InputStream inputStream = getContentResolver().openInputStream(uri);
//copyFileuri(inputStream,fileOutputStream);
//    }
//
//    private void alterDocument() {
//        try {
//            File  cacheFile = new File(getApplicationContext().getExternalCacheDir() + "/cache.png");
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                ContentResolver resolver = getContentResolver();
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".png");
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
//                        Environment.DIRECTORY_PICTURES+ File.separator+"WebPTool");
//                Uri finaluri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//               OutputStream fileOutputStream =
//                resolver.openOutputStream(Objects.requireNonNull(finaluri));
//               toastx(fileOutputStream.toString());
//                if (fileOutputStream != null) {
//                    copyFilenew(cacheFile,fileOutputStream);}
//
//
////              Bitmap bitmap=  BitmapFactory.decodeResource(getApplicationContext().getResources(),
////                        R.drawable.share);
////                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//
//            Objects.requireNonNull(fileOutputStream);
//
//            }
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//            private void createFile(Uri pickerInitialUri) {
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/webp");
//        intent.putExtra(Intent.EXTRA_TITLE, "invoice.webp");
//
//        // Optionally, specify a URI for the directory that should be opened in
//        // the system file picker when your app creates the document.
//      //  intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//
//        startActivityForResult(intent, 1);
//    }
    private boolean appInstalledOrNot(int i) {
        String uri;
        switch (i) {
            case 3 : uri="com.instagram.android";
                break;
            case 4 : uri="com.facebook.orca";
                break;
            default:
                uri="com.whatsapp";
        }
        PackageManager pm = getApplicationContext().getPackageManager();
        try {

            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES); return true;
        } catch (PackageManager.NameNotFoundException e) { }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
           // if(linear_main.getVisibility()==View.VISIBLE){
            main_share(5);
        //}

        } else {
            Toast.makeText(getApplicationContext(), "Storage Permission Required!", Toast.LENGTH_SHORT).show();
        }
    }

    public void mtoast(String s){
      Toast toast=  Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }
//    private File getNextFilePath() {
//        String env = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
//        String path = String.format("%s/webtest-%d.webp", env, System.currentTimeMillis());
//      //  Log.d(TAG, path);
//        return new File(path);
//    }

//    public void getgif (ImageView img_mainx){
//        gifload = new gifload();  gifload.execute(img_mainx); }
}