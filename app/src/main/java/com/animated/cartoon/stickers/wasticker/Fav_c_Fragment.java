package com.animated.cartoon.stickers.wasticker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.animated.cartoon.stickers.wasticker.adapter.Adapter_Fav_c;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class Fav_c_Fragment extends Fragment {

    public Fav_c_Fragment() { }
    private RecyclerView recyclerView;
    private ArrayList<HashMap<String, Object>> favourite = new ArrayList<>();
    private SharedPreferences sp;
private LinearLayout textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fav_c_, container, false);
            textView = v.findViewById(R.id.textviewc);
            recyclerView = v.findViewById(R.id.recycler_favs);
            sp = Objects.requireNonNull(this.getActivity())
                    .getSharedPreferences("sp", Context.MODE_PRIVATE);

            refresh();



        return v;
    }



    public void refresh(){
        if (!((sp.getString("favourite", "").equals(""))||(sp.getString("favourite", "").equals("[]")))) {
            favourite = new Gson().fromJson(sp.getString("favourite", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            Adapter_Fav_c adapterMain = new Adapter_Fav_c(favourite, getActivity(), Fav_c_Fragment.this);
             recyclerView.setVisibility(View.VISIBLE);
             textView.setVisibility(View.GONE);
            LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
            recyclerView.setAdapter(adapterMain);
            recyclerView.setLayoutManager(layoutManager);
        }else{
           textView.setVisibility(View.VISIBLE);
           recyclerView.setVisibility(View.GONE);
        }

    }
    private void mtoast(String s){
        ((MainActivity)getActivity()).mtoast(s);
    }
    public void refreshm(int i){
        favourite.remove(i);
        sp.edit().putString("favourite", new Gson().toJson(favourite)).commit();
        mtoast("Category Removed");

        refresh();
    }
}