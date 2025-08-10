package com.animated.cartoon.stickers.wasticker.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.animated.cartoon.stickers.wasticker.R;

import java.util.Objects;



public class SettingFragment extends Fragment {
private LinearLayout l_enablekey;
private ImageView add_key;
    public SettingFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View v =inflater.inflate(R.layout.fragment_setting, container, false);

      l_enablekey  = v.findViewById(R.id.l_enablekey);
        LinearLayout l_addkey = v.findViewById(R.id.l_addkey);
        add_key = v.findViewById(R.id.add_key);
        l_addkey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

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
                }else {
                    Intent intent = new
                            Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });

        l_enablekey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                InputMethodManager imeManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();

            }
        });

        return v;
    }


    public void onStart() {
        InputMethodManager imeManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imeManager.getEnabledInputMethodList().toString().contains("com.animated.cartoon.stickers.wasticker")) {
            l_enablekey.setVisibility(View.VISIBLE);
            add_key.setImageResource(R.drawable.checked);

        } else {
            l_enablekey.setVisibility(View.GONE);
            add_key.setImageResource(R.drawable.circle_outline_24);

        }

        super.onStart();
    }

}