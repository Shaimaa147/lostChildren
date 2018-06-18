package com.iti.jets.lostchildren.homeScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.pojos.LostChild;

/**
 * Created by Shemo on 6/17/2018.
 */

public class ChildDetailsFragment extends Fragment {

    public static final String CHILD_JSON = "Child JSON";
    public static final String CHILD_TYPE = "Child Type";
    public static final String LOST_CHILD = "Lost Child";
    public static final String FOUND_CHILD = "Found Child";
    String childJson, childType;
    public LostChild currentChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            childJson = getArguments().getString(CHILD_JSON);
            childType = getArguments().getString(CHILD_TYPE);
            if(childType.equals(LOST_CHILD)) {
                currentChild = new Gson().fromJson(childJson, LostChild.class);
                Log.i("ChildDetails", "current child..............."+currentChild.getFirstName());
            }

        }
        else
            Log.i("ChildDetails", "getAruments = null ...............");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_details, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}
