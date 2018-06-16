package com.iti.jets.lostchildren;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ahmed Ali on 6/10/2018.
 */

public class FragmentFound extends android.support.v4.app.Fragment {
    View v;
    public FragmentFound(){


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.found_fragment,container,false);
        return  v;
    }
}
