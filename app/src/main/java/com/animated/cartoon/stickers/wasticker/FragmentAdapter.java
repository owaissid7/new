package com.animated.cartoon.stickers.wasticker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList= new ArrayList<>();
    private final List<String>fragmenttitlelist= new ArrayList<>();


    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

   return fragmentList.get(position);

    }

    @Override
    public int getCount() {
       return fragmenttitlelist.size();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmenttitlelist.get(position);

    }
    public  void  addfagment(Fragment fragment, String title){
        fragmentList.add(fragment);
fragmenttitlelist.add(title);
    }


}
