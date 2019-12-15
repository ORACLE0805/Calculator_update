package com.example.ylmusic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class Music_pager extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public Music_pager(FragmentManager manager, List<Fragment> fragmentList){
            super(manager);
            this.fragmentList=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
