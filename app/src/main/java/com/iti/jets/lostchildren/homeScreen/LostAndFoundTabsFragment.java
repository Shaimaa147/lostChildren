package com.iti.jets.lostchildren.homeScreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.adapter.ViewPagerAdpter;

/**
 * Created by Ahmed Ali on 19/06/2018.
 */

public class LostAndFoundTabsFragment extends Fragment {

    public static final String LOST_AND_FOUND_TABS = "lostAndFound";
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ViewPagerAdpter adpter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found_tabs, container, false);
        tableLayout = view.findViewById(R.id.tableLayoutID);
        viewPager = view.findViewById(R.id.pagerID);

        adpter = new ViewPagerAdpter(getChildFragmentManager());
        adpter.addFragment(new FragmentLost(), "Lost Children");
        adpter.addFragment(new FragmentFound(), "Found Children");
        viewPager.setAdapter(adpter);
        tableLayout.setupWithViewPager(viewPager);


        return view;
    }

}
