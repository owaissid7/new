package com.animated.cartoon.stickers.wasticker.Fragment;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.animated.cartoon.stickers.wasticker.MainActivity;
import com.animated.cartoon.stickers.wasticker.R;
import com.animated.cartoon.stickers.wasticker.adapter.AdapterMain;



public class Homeragment extends Fragment {
   public RecyclerView recyclerView;
    public AdapterMain adapterMain;



    public Homeragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
            recyclerView = v.findViewById(R.id.recycler_main);

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SwipeRefreshLayout swipe;
     adapterMain = new AdapterMain(((MainActivity)getActivity()).homelist , getActivity());
                recyclerView.setAdapter(adapterMain);
          swipe =v.findViewById(R.id.swipe);
          swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                  do_refresh();
                  swipe.setRefreshing(false);
                  ((MainActivity)getActivity()).enable(true);
              }
          });




            // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());


       //    Toast.makeText(getActivity(), ((MainActivity)getActivity()).homelist.toString(),Toast.LENGTH_SHORT).show();

       return v;
    }
    public  void do_refresh(){
        android.net.ConnectivityManager connMgr = (android.net.ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            adapterMain.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "Internet Not Available!", Toast.LENGTH_SHORT).show();
        }
    }

}