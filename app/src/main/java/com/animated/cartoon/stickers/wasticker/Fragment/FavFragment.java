package com.animated.cartoon.stickers.wasticker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.animated.cartoon.stickers.wasticker.Fav_c_Fragment;
import com.animated.cartoon.stickers.wasticker.Fav_s_Fragment;
import com.animated.cartoon.stickers.wasticker.R;
import com.google.android.material.tabs.TabLayout;


public class FavFragment extends Fragment {

    private  Fav_c_Fragment fav_c_fragment;
   private Fav_s_Fragment fav_s_fragment;
    //private FragmentAdapter fragmentAdapter;
    public FavFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fav, container, false);
            // ViewPager = v.findViewById(R.id.viewpaer_fav);
        TabLayout tabLayout = v.findViewById(R.id.tablayoutfav);
        tabLayout.addTab(tabLayout.newTab().setText("Stickers"));
        tabLayout.addTab(tabLayout.newTab().setText("Category"));
        fav_s_fragment = new Fav_s_Fragment();
        fav_c_fragment = new Fav_c_Fragment();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               if(tab.getPosition()==0) {
                   getChildFragmentManager().beginTransaction().replace(R.id.fragment_fav, fav_s_fragment).commit();

               }else{
                   getChildFragmentManager().beginTransaction().replace(R.id.fragment_fav, fav_c_fragment).commit();

               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//            fragmentAdapter = new FragmentAdapter(this.getChildFragmentManager(),
//                    FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//            fragmentAdapter.addfagment(new Fav_s_Fragment(), "Stickers");
//            fragmentAdapter.addfagment(new Fav_c_Fragment(), "Category");
         //  onStartx();

        return v;
    }


    public void onStartx() {
        super.onStart();

        if (getChildFragmentManager().getFragments().contains(fav_s_fragment)) {
            fav_s_fragment.refresh();
        }else if (getChildFragmentManager().getFragments().contains(fav_c_fragment)){
            fav_c_fragment.refresh();
        }else{
            getChildFragmentManager().beginTransaction().add(R.id.fragment_fav, fav_s_fragment).commit();

        }
//
//       if(!(getChildFragmentManager().getFragments().contains(fav_s_fragment)||
//               getChildFragmentManager().getFragments().contains(fav_c_fragment))) {
//           getChildFragmentManager().beginTransaction().add(R.id.fragment_fav, fav_s_fragment).commit();
//       }
//        ViewPager.setAdapter(fragmentAdapter);
//        tabLayout.setupWithViewPager(ViewPager);
    }


}