package com.animated.cartoon.stickers.wasticker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.animated.cartoon.stickers.wasticker.adapter.Adapter_Fav_s;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Objects;

public class Fav_s_Fragment extends Fragment {


    public Fav_s_Fragment() {
    }

    private RecyclerView recyclerView;
    private LinearLayout textView;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fav_s_, container, false);
        textView = v.findViewById(R.id.textviews);

        recyclerView = v.findViewById(R.id.recycler_favs);
        sp = Objects.requireNonNull(this.getActivity())
                .getSharedPreferences("sp", Context.MODE_PRIVATE);

        refresh();


        return v;
    }

    public void refresh() {
        if (!((sp.getString("mapitem", "").equals("")) || (sp.getString("mapitem", "").equals("[]")))) {
            ArrayList<String> strings = new Gson().fromJson(sp.getString("mapitem", ""), new TypeToken<ArrayList<String>>() {
            }.getType());
            Adapter_Fav_s adapterMain = new Adapter_Fav_s(strings, getActivity(), Fav_s_Fragment.this);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setAdapter(adapterMain);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }
}