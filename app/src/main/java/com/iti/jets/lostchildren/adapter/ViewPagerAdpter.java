package com.iti.jets.lostchildren.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Ali on 6/10/2018.
 */

public class ViewPagerAdpter extends FragmentPagerAdapter {
    private final List<Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitles = new ArrayList<>();
    public ViewPagerAdpter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return lstTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitles.get(position);
    }
    public  void addFragment(Fragment fragment , String titile){

        lstFragment.add(fragment);
        lstTitles.add(titile);
    }

}
