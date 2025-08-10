
package com.animated.cartoon.stickers.wasticker;


import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AppOpsManager;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.widget.BaseAdapter;
import android.widget.Toast;

import com.animated.cartoon.stickers.wasticker.Extra.AnimatedGifMaker;
import com.animated.cartoon.stickers.wasticker.Extra.FileUtil;
import com.animated.cartoon.stickers.wasticker.adapter.Adapter_key_main;
import com.animated.cartoon.stickers.wasticker.adapter.Adapter_key_sticker;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ImageKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private static final String TAG = "ImageKeyboard";

    private static final String MIME_TYPE_GIF = "image/gif";
    private static final String MIME_TYPE_PNG = "image/png";
   private static final String MIME_TYPE_WEBP = "image/webp";
    private File afile;

    private KeyboardView kv;
    private Keyboard keyboard;

    private  boolean isCaps = false;
    private boolean commit = false;
    private int n = 0;
   public int position = 0;
    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear3;
    private LinearLayout linear4;
    private ArrayList<HashMap<String, Object>> list1 = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> list2 = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> list3 = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> list4 = new ArrayList<>();
    private TextView caps;
    private ImageView back;
    private GridView grid1;
    private GridView grid2;
    private GridView grid3;
    private GridView grid4;
    private TextView change;
    private double c =0;
    private double coption =0;
    public LinearLayout keylayout;
    private SharedPreferences sp;
    private LinearLayout empty;
    private ArrayList<String> stickerl= new ArrayList<>();
    private ArrayList<String> key= new ArrayList<>();
    public Adapter_key_sticker adaptersticker;
    public TextView toptitle;
    private int start =0;
    public HashMap<String, Object> map = new HashMap<>();
    private RecyclerView recyclerView2;
    private  RecyclerView recyclerView;

    private ImageView img_s;

    private   LinearLayout linearr;
    File webpFile = null;
    boolean canshare = false;
    private boolean isCommitContentSupported(
            @Nullable EditorInfo editorInfo, @NonNull String mimeType) {
        if (editorInfo == null) {
            return false;
        }

        final InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            return false;
        }

        if (!validatePackageName(editorInfo)) {
            return false;
        }

        final String[] supportedMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        for (String supportedMimeType : supportedMimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, supportedMimeType)) {
                return true;
            }
        }
        return false;
    }

    private void doCommitContent(@NonNull String description, @NonNull String mimeType,
            @NonNull File file) {
        Log.d("ggg", "GcG");
        final EditorInfo editorInfo = getCurrentInputEditorInfo();

        // Validate packageName again just in case.
        if (!validatePackageName(editorInfo)) {
            return;
        }

        final Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".MyFileProvider", file);

        // As you as an IME author are most likely to have to implement your own content provider
        // to support CommitContent API, it is important to have a clear spec about what
        // applications are going to be allowed to access the content that your are going to share.
        final int flag;
        if (Build.VERSION.SDK_INT >= 25) {

            flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
        } else {
            flag = 0;
            try {
                // TODO: Use revokeUriPermission to revoke as needed.
                grantUriPermission(
                        editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e){
                Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
                        + " contentUri=" + contentUri, e);
            }
        }

        final InputContentInfoCompat inputContentInfoCompat = new InputContentInfoCompat(
                contentUri,
                new ClipDescription(description, new String[]{mimeType}),
                null /* linkUrl */);
        InputConnectionCompat.commitContent(
                getCurrentInputConnection(), getCurrentInputEditorInfo(), inputContentInfoCompat,
                flag, null);
    }

    private boolean validatePackageName(@Nullable EditorInfo editorInfo) {
        if (editorInfo == null) {
            return false;
        }
        final String packageName = editorInfo.packageName;
        if (packageName == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }

        final InputBinding inputBinding = getCurrentInputBinding();
        if (inputBinding == null) {
            // Due to b.android.com/225029, it is possible that getCurrentInputBinding() returns
            // null even after onStartInputView() is called.
            // TODO: Come up with a way to work around this bug....
            Log.e(TAG, "inputBinding should not be null here. "
                    + "You are likely to be hitting b.android.com/225029");
            return false;
        }
        final int packageUid = inputBinding.getUid();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final AppOpsManager appOpsManager =
                    (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            try {
                appOpsManager.checkPackage(packageUid, packageName);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        final PackageManager packageManager = getPackageManager();
        final String possiblePackageNames[] = packageManager.getPackagesForUid(packageUid);
        for (final String possiblePackageName : possiblePackageNames) {
            if (packageName.equals(possiblePackageName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
}
public void loadsticker(){

    map = new Gson().fromJson(sp.getString("map", ""),new TypeToken<HashMap<String, Object>>() {
    }.getType());
    if(position == 0) {
        if (!((sp.getString("mapitem", "").equals("")) || (sp.getString("mapitem", "").equals("[]")))) {
            stickerl = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {
            }.getType());
            adaptersticker = new Adapter_key_sticker(stickerl, key, map, getApplicationContext(), ImageKeyboard.this);

            GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 4);
            recyclerView2.setAdapter(adaptersticker);
            recyclerView2.setLayoutManager(layoutManager2);
            recyclerView2.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);


        } else {
            empty.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.GONE);
        }
    }else{

        stickerl = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        adaptersticker = new Adapter_key_sticker(stickerl, key, map, getApplicationContext(), ImageKeyboard.this);

        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView2.setAdapter(adaptersticker);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
    }
}
    @Override
    public void onFinishInputView(boolean finishingInput) {

       super.onFinishInputView(finishingInput);
       linearr.removeView(recyclerView);
       linearr.removeView(recyclerView2);
        canshare=false;

    }
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {

       start++;
   if(start>1){
    map = new Gson().fromJson(sp.getString("map", ""),new TypeToken<HashMap<String, Object>>() {
    }.getType());

    key = new Gson().fromJson(sp.getString("keyboard", ""), new TypeToken<ArrayList<String>>() {}.getType());
   if(position>key.size()){
       position=key.size();
   }
   if(position==0){
       toptitle.setText("Favourite List");
   }else{
       toptitle.setText(key.get((int) position - 1).split(",")[1]);
   }

    Adapter_key_main adapterMain = new Adapter_key_main(key, getApplicationContext(),ImageKeyboard.this);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
    recyclerView.setAdapter(adapterMain);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView2.setVisibility(View.VISIBLE);

if(position==0) {
    if (!((sp.getString("mapitem", "").equals("")) || (sp.getString("mapitem", "").equals("[]")))) {
        stickerl = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        adaptersticker = new Adapter_key_sticker(stickerl, key, map, getApplicationContext(), ImageKeyboard.this);

        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView2.setAdapter(adaptersticker);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);


    } else {
        empty.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.GONE);
    }
}else{
    stickerl = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {
    }.getType());
    adaptersticker = new Adapter_key_sticker(stickerl, key, map, getApplicationContext(), ImageKeyboard.this);

    GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(), 4);
    recyclerView2.setAdapter(adaptersticker);
    recyclerView2.setLayoutManager(layoutManager2);
    recyclerView2.setVisibility(View.VISIBLE);
    empty.setVisibility(View.GONE);
}
}
        n =0;
        list1();
        list2a();
        list3a();
        list4a();
        remove();
        change.setText("!#1");
        caps.setBackgroundResource(R.drawable.capsoff);
        coption =0;
        caps.setText("");

    }
    public int iscommitted(){


        if (isCommitContentSupported(getCurrentInputEditorInfo(), MIME_TYPE_GIF)) {
            return 1;
        }

        else if(isCommitContentSupported(getCurrentInputEditorInfo(), MIME_TYPE_PNG)) {
            return 2;}

        else{
           return 0;
        }

    }

    public void encodeGif(List<Bitmap> bitmaps, File outFile, int n) {
        try {
            // deleteFile(outFile.toString());
            FileOutputStream out = new FileOutputStream(outFile);
            AnimatedGifMaker gifs = new AnimatedGifMaker();
            gifs.start(out);
            gifs.setDelay(n);
            gifs.setRepeat(0);
            gifs.setTransparent(new Color());
            for (int i = 0; i < bitmaps.size(); i++) {
                gifs.addFrame(bitmaps.get(i));
            }
            gifs.finish();
        } catch (IOException err) {
        }
    }
    public void dopng(String s) {

            Glide.with(getApplicationContext()).asBitmap().load(s)
                    .placeholder(R.drawable.stickersample
                    ).addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Toast.makeText(getApplicationContext(), "Sticker Is Not Yet Loaded", Toast.LENGTH_SHORT).show();

                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    //Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();


                    afile = new File(getApplicationContext().getExternalCacheDir()+"/cache.png");
                    FileOutputStream outStream= null;
                    try {
                        outStream = new FileOutputStream(afile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    resource.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    try {
                        outStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (FileUtil.isExistFile(afile.toString())){
                        ImageKeyboard.this.doCommitContent("A waving flag", MIME_TYPE_PNG, afile);}

                    return false;
                }
            }).into(img_s);


    }

    public void dogif(String s){

//        if (gifload != null && gifload.getStatus() == AsyncTask.Status.RUNNING) {
//
//            Toast.makeText(getApplicationContext(), "Please Wait, Other Task Is Running...", Toast.LENGTH_SHORT).show();
//
//
//        }else {
            //img_sticker.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(s)
                    .placeholder(R.drawable.stickersample
                    ).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    Toast.makeText(getApplicationContext(), "Sticker Is Not Yet Loaded", Toast.LENGTH_SHORT).show();

                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                    Toast.makeText(getApplicationContext(), "Converting, Wait...", Toast.LENGTH_SHORT).show();

                    AsyncTask.execute(new Runnable() {
                        @Override public void run() {
                            canshare=true;
                            FutureTarget<File> future = Glide.with(getApplicationContext())
                                    .load(s).downloadOnly(512, 512);
                            try { webpFile = future.get();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace(); }

                            if (webpFile != null) {

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    protected void finalize() throws Throwable {
                                        super.finalize();
                                    }

                                    @Override
                                    public void run() {
                                        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                                        File fileout = new File(getApplicationContext().getExternalCacheDir()
                                                + "/cache.gif");
                                        byte[] data = new byte[(int) webpFile.length()];
                                        try {
                                            new FileInputStream(webpFile).read(data);
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

                                                bitmaps.add(b);
                                                webpDecoder.advance();
                                            }
                                            webpDecoder.advance();

                                            if (canshare) {
                                                encodeGif(bitmaps, fileout, webpDecoder.getDelay(1));


                                                if (canshare) {
                                                    afile = new File(getApplicationContext().getExternalCacheDir()+"/cache.gif");
                                                   canshare=false;
                                                    if (FileUtil.isExistFile(afile.toString())){
                                                        ImageKeyboard.this.doCommitContent("A waving flag", MIME_TYPE_GIF, afile);}

                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }});
                    return false;
                }
            }).into(img_s);
      //  }
    }
    @Override
    public View onCreateInputView() {

        n=0;
        LinearLayout KeyboardLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.mykeylayout, null);
        linear1 = (LinearLayout) KeyboardLayout.findViewById(R.id.linear1);
        linearr = (LinearLayout) KeyboardLayout.findViewById(R.id.lroot);
        linear2 = (LinearLayout) KeyboardLayout.findViewById(R.id.linear2);
        linear3 = (LinearLayout) KeyboardLayout.findViewById(R.id.linear3);
        linear4 = (LinearLayout) KeyboardLayout.findViewById(R.id.linear4);
        caps = (TextView) KeyboardLayout.findViewById (R.id.caps);
         back = (ImageView) KeyboardLayout.findViewById (R.id.back);
         change = (TextView)  KeyboardLayout.findViewById (R.id.change);
        TextView point = (TextView) KeyboardLayout.findViewById(R.id.point);
        TextView coma = (TextView) KeyboardLayout.findViewById(R.id.coma);
        TextView space = (TextView) KeyboardLayout.findViewById(R.id.space);
        ImageView enter = (ImageView) KeyboardLayout.findViewById(R.id.enter);
        keylayout = (LinearLayout) KeyboardLayout.findViewById(R.id.keylayout);
        img_s = KeyboardLayout.findViewById (R.id.img_s);
        ImageView choose = (ImageView) KeyboardLayout.findViewById(R.id.choose);
        LinearLayout lbottom = (LinearLayout) KeyboardLayout.findViewById(R.id.linearbottom);
      empty = (LinearLayout)  KeyboardLayout.findViewById (R.id.empty);
        _SetLinear(coma, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 10.0d);
        _SetLinear(change, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 8.0d);
        _SetLinear(enter, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 8.0d);
        _SetLinear(point, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 10.0d);
        _SetLinear(caps, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 9.0d);
        _SetLinear(back, getDisplayHeightPixels(getApplicationContext()) / 18.0d,getDisplayWidthPixels(getApplicationContext()) / 9.0d);
        _SetLinear(space, getDisplayHeightPixels(getApplicationContext()) / 18.0d);
       LinearLayout ltop1 = (LinearLayout) KeyboardLayout.findViewById(R.id.ltop1);
       LinearLayout ltop2 = (LinearLayout) KeyboardLayout.findViewById(R.id.ltop2);
       LinearLayout space1 = (LinearLayout) KeyboardLayout.findViewById(R.id.space1);
       LinearLayout space2 = (LinearLayout) KeyboardLayout.findViewById(R.id.space2);
        toptitle = KeyboardLayout.findViewById(R.id.toptitle);
        LinearLayout topback = KeyboardLayout.findViewById(R.id.topback);
        LinearLayout linearsticker = KeyboardLayout.findViewById(R.id.linearsticker);
        _SetLinear(linearsticker, getDisplayHeightPixels(getApplicationContext()) / 2.2d);
        TextView text_sticker = (TextView) KeyboardLayout.findViewById(R.id.sticker_text);

        sp = getSharedPreferences("sp", Activity.MODE_PRIVATE);

        if(sp.getString("keyboard","").equals("")){
            sp.edit().putString("keyboard", "[]").commit();
        }
        map = new Gson().fromJson(sp.getString("map", ""),new TypeToken<HashMap<String, Object>>() {
        }.getType());
        key = new Gson().fromJson(sp.getString("keyboard", ""), new TypeToken<ArrayList<String>>() {}.getType());
        Adapter_key_main adapterMain = new Adapter_key_main(key, getApplicationContext(),ImageKeyboard.this);
        recyclerView = KeyboardLayout.findViewById(R.id.recycler_cat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setAdapter(adapterMain);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2 = KeyboardLayout.findViewById(R.id.recycler_sticker);

        if (!((sp.getString("mapitem", "").equals(""))||(sp.getString("mapitem", "").equals("[]")))) {
            stickerl = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {}.getType());
            adaptersticker = new Adapter_key_sticker(stickerl,key, map, getApplicationContext(),ImageKeyboard.this);

            GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(),4);
            recyclerView2.setAdapter(adaptersticker);
            recyclerView2.setLayoutManager(layoutManager2);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
        if (!(sp.getString("switch", "").equals(""))) {
           recyclerView2.setBackgroundColor(Color.parseColor("#000000"));
        }
        ltop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               space1.setVisibility(View.VISIBLE);
               space2.setVisibility(View.GONE);
               topback.setVisibility(View.GONE);
                linearsticker.setVisibility(View.GONE);
                keylayout.setVisibility(View.VISIBLE);
                text_sticker.setVisibility(View.VISIBLE);


            }
        });

        ltop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                space2.setVisibility(View.VISIBLE);
                space1.setVisibility(View.GONE);
                topback.setVisibility(View.VISIBLE);
                linearsticker.setVisibility(View.VISIBLE);
                keylayout.setVisibility(View.GONE);
                text_sticker.setVisibility(View.GONE);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConnection ic = getCurrentInputConnection();

                CharSequence selectedText = ic.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    // no selection, so delete previous character
                    ic.deleteSurroundingText(1,0);

                } else {
                    // delete the selection
                    ic.commitText("", 1);
                }
                commit = true;


             //   InputConnection ic = getCurrentInputConnection();
                if (n==-1) {
                    n=0;
                    adapter();
                    caps.setBackgroundResource(R.drawable.capsoff);
                }
//                ic.deleteSurroundingText(1,0);
//                commit = true;


            }
        });
        back.setOnLongClickListener(new View.OnLongClickListener()  {

            private Handler mHandler;

            @Override
            public boolean onLongClick(View view) {
                final Runnable mAction = new Runnable() {
                    @Override
                    public void run() {
                        InputConnection ic = getCurrentInputConnection();
                        if (n==-1) {
                            n=0;
                            adapter();
                            caps.setBackgroundResource(R.drawable.capsoff);
                        }
                        ic.deleteSurroundingText(1,0);
                        commit = true;
                        mHandler.postDelayed(this, 50);
                    }
                };
                mHandler = new Handler();
                mHandler.postDelayed(mAction, 0);
                back.setOnTouchListener(new View.OnTouchListener() {

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_CANCEL:
                            case MotionEvent.ACTION_MOVE:
                            case MotionEvent.ACTION_UP:
                                if (mHandler == null) return true;
                                mHandler.removeCallbacks(mAction);
                                mHandler = null;
                                back.setOnTouchListener(null);
                                return false;
                        }
                        return false;
                    }

                });

                return true;
            }
        });
        caps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if (c == 1){
    if (coption == 0) {
        coption =1;
        caps.setText("2/2");
        list2c();
        list3c();
        list4c();

    }
    else{
        list2b();
        list3b();
        list4b();
    coption =0;
        caps.setText("1/2");

  }
remove();
                caps.setBackgroundResource(R.drawable.white_round2);}
else{
                    switch (n) {
                        case -1:

                            caps.setBackgroundResource(R.drawable.capson);
                            n = 2;
                            break;
                        case 2:
                            caps.setBackgroundResource(R.drawable.capsoff);
                            n = 0;
                            break;
                        case 0:
                            caps.setBackgroundResource(R.drawable.capst);
                            n = -1;

                            break;
                    }
                    adapter();
                }

            }
        });
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(new ComponentName("com.animated.cartoon.stickers.wasticker", "com.animated.cartoon.stickers.wasticker.MainActivity"));
                    startActivity(intent);
                }catch (Exception c){
                    Toast.makeText(getApplicationContext(),c.toString(),  Toast.LENGTH_SHORT).show();
                }
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imeManager = (InputMethodManager)getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();}
        });


        coma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConnection ic = getCurrentInputConnection();
                ic.commitText((","),1);
            }
        });
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConnection ic = getCurrentInputConnection();
                ic.commitText(("."),1);
            }
        });


        space.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                InputMethodManager imeManager = (InputMethodManager)getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();

                return false;
            }
        });


        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConnection ic = getCurrentInputConnection();
                ic.commitText((" "),1);
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputConnection ic = getCurrentInputConnection();
                try {
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }

        });

       change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c ==1){
                    c =0;
                    change.setText("!#1");
                    caps.setText("");
                    caps.setBackgroundResource(R.drawable.capsoff);
                    list2a();
                    list3a();
                    list4a();
                    caps.setBackgroundResource(R.drawable.capsoff);
                    coption =0;
                    caps.setText("");
                    n =0;
                }
                else
                {
                c=1;
                    list2b();
                    list3b();
                    list4b();

                caps.setText("1/2");
                caps.setBackgroundResource(R.drawable.white_round2);
                change.setText("Abc");}
                remove();
          }
        });

            return KeyboardLayout;

        }


    @Override
    public boolean onEvaluateFullscreenMode() {

        // In full-screen mode the inserted content is likely to be hidden by the IME. Hence in this
        // sample we simply disable full-screen mode.
        return false;
    }







    @Override
    public void onPress(int i) {
    }

    @Override
    public void onRelease(int i) {
    }

    @Override
    public void onKey(int i, int[] ints) {

    }

private void _grid(final ArrayList<HashMap<String, Object>> _data, View layout){

        grid1 = new GridView(this);
        grid1.setNumColumns(_data.size());
        grid1.setColumnWidth(GridView.AUTO_FIT);
        grid1.setVerticalSpacing(0);
        grid1.setHorizontalSpacing(0);
        grid1.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid1.setAdapter(new Listview1Adapter(_data));
        ((BaseAdapter) grid1.getAdapter()).notifyDataSetChanged();
        ((LinearLayout) layout).addView(grid1);


}
    private void _grid2(final ArrayList<HashMap<String, Object>> _data, View layout){

        grid2 = new GridView(this);
        grid2.setNumColumns(_data.size());
        grid2.setColumnWidth(GridView.AUTO_FIT);
        grid2.setVerticalSpacing(0);
        grid2.setHorizontalSpacing(0);
        grid2.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid2.setAdapter(new Listview1Adapter(_data));
        ((BaseAdapter)grid2.getAdapter()).notifyDataSetChanged();

            ((LinearLayout) layout).addView(grid2);

    }
    private void _grid3(final ArrayList<HashMap<String, Object>> _data, View layout){

        grid3 = new GridView(this);
        grid3.setNumColumns(_data.size());
      grid3.setColumnWidth(GridView.AUTO_FIT);
        grid3.setVerticalSpacing(0);
        grid3.setHorizontalSpacing(0);
        grid3.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid3.setAdapter(new Listview1Adapter(_data));
        ((BaseAdapter)grid3.getAdapter()).notifyDataSetChanged();
        ((LinearLayout)layout).addView(grid3);


    }
    private void _grid4(final ArrayList<HashMap<String, Object>> _data, View layout){

        grid4 = new GridView(this);
        grid4.setNumColumns(_data.size());
        grid4.setColumnWidth(GridView.AUTO_FIT);
        grid4.setVerticalSpacing(0);
        grid4.setHorizontalSpacing(0);
        grid4.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid4.setAdapter(new Listview1Adapter(_data));
        ((BaseAdapter)grid4.getAdapter()).notifyDataSetChanged();
        ((LinearLayout)layout).addView(grid4);
    }



    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;
        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }
        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.line1, null);
            }

            final TextView textview1u = (TextView) _v.findViewById(R.id.textview1u);
            final LinearLayout linearup = (LinearLayout) _v.findViewById(R.id.linearup);
            final LinearLayout linearu = (LinearLayout) _v.findViewById(R.id.linearu);
            _SetLinear(linearup, getDisplayHeightPixels(getApplicationContext()) / 17.0d);


            switch (n){
    case 0:
        textview1u.setText(_data.get((int)_position).get("text").toString());
        break;
    default:
        textview1u.setText(_data.get((int)_position).get("text").toString().toUpperCase());
}


            linearu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    InputConnection ic = getCurrentInputConnection();
                    switch (n){ case 0:
                        ic.commitText((_data.get((int)_position).get("text").toString()),1);


                    break;
                        case -1:
                            ic.commitText((_data.get((int)_position).get("text").toString().toUpperCase()),1);
n=0;
adapter();


                    break;
                        case 2:
                            ic.commitText((_data.get((int)_position).get("text").toString().toUpperCase()),1);

                            break; }



                }

            });

            return _v;
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
    private void adapter(){

        ((BaseAdapter)grid1.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)grid2.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)grid3.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)grid4.getAdapter()).notifyDataSetChanged();

    }
    private void remove(){
        linear1.removeView(grid1);
        linear4.removeView(grid4);
        linear2.removeView(grid2);
        linear3.removeView(grid3);
        _allgrid();
        adapter();

    }
    private void _allgrid(){
        _grid(list1,linear1);
        _grid2(list2,linear2);
        _grid3(list3,linear3);
        _grid4(list4,linear4);
    }
    public static int getDisplayWidthPixels(Context _context) {
        return _context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayHeightPixels(Context _context) {
        return _context.getResources().getDisplayMetrics().heightPixels;
    }

    private void list1(){
        list1.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "1");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "2");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "3");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "4");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "5");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "6");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "7");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "8");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "9");
            list1.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "0");
            list1.add(_item); }



    }
    private void list2a(){
        list2.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "q");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "w");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "e");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "r");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "t");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "y");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "u");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "i");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "o");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "p");
            list2.add(_item); }

    }
    private void list2b(){
        list2.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "+");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "×");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "÷");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "=");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "/");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "_");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "€");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "£");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "₹");
            list2.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "₩");
            list2.add(_item); }


    }
    private void list2c() {
        list2.clear();
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "`");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "~");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "\\");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "|");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "<");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", ">");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "{");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "}");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "[");
            list2.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "]");
            list2.add(_item);
        }
    }

    private void list3a(){
        list3.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "a");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "s");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "d");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "f");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "g");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "h");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "j");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "k");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "l");
            list3.add(_item); }


    }
    private void list3b() {
        list3.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "!");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "@");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "#");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "$");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "%");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "^");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "&");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "*");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "(");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", ")");
            list3.add(_item); }


    }
    private void list3c(){
        list3.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "°");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "•");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "○");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "●");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "□");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "■");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "♤");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "♡");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "◇");
            list3.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "♧");
            list3.add(_item); }

    }
    private void list4a(){
        list4.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "z");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "x");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "c");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "v");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "b");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "n");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "m");
            list4.add(_item); }



    }
    private void list4b(){
        list4.clear();
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "-");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "'");
            list4.add(_item); }

        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "\"");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", ":");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", ";");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", ",");
            list4.add(_item); }
        { HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "?");
            list4.add(_item); }



    }
    private void list4c() {
        list4.clear();
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "☆");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "▪");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "¤");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "《");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "》");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "¡");
            list4.add(_item);
        }
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("text", "¿");
            list4.add(_item);

        }
    }

    private void _SetLinear (final View _v, final double _h) {
        _v.getLayoutParams().height = (int)_h;
        _v.requestLayout();
    }
    private void _SetLinear (final View _v, final double _h, final double _w) {
        _v.getLayoutParams().height = (int)_h;
        _v.getLayoutParams().width = (int)_w;
        _v.requestLayout();
    }
    @Override
    public void onFinishInput() {

        super.onFinishInput();

    }
}
