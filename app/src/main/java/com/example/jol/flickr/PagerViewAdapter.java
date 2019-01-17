package com.example.jol.flickr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jol.flickr.TabFavouritesFragment;
import com.example.jol.flickr.TabSuchenFragment;

public class PagerViewAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    public PagerViewAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabSuchenFragment tabSuchenFragment = new TabSuchenFragment();
                return tabSuchenFragment;
            case 1:
                TabFavouritesFragment tabFavouritesFragment = new TabFavouritesFragment();
                return tabFavouritesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
