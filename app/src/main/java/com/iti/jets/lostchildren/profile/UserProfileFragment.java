package com.iti.jets.lostchildren.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iti.jets.lostchildren.R;

/**
 * Created by Fadwa on 19/06/2018.
 */

public class UserProfileFragment extends Fragment {
    public static final String USER_PROFILE = "userProfile";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        return  view;
    }

}
