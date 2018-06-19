package com.iti.jets.lostchildren.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.authorizing.SignUpFragment;
import com.iti.jets.lostchildren.homeScreen.MainActivity;
import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.service.LostChildServiceClient;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import static com.iti.jets.lostchildren.homeScreen.MainActivity.USER_SHARED_PREF;

/**
 * Created by Fadwa on 19/06/2018.
 */

public class UserProfileFragment extends Fragment {
    public static final String USER_PROFILE = "userProfile";
    private ImageView userImgView;
    private User currentUser;
    private SharedPreferences userSharedPref;
    private String userNullableData;
    private String baseUrl = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        baseUrl = "http://" + LostChildServiceClient.getServerIp() + ":8084/LostChildren/users_images/";
        userImgView = view.findViewById(R.id.userImg);
        userSharedPref = getActivity().getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);


        if (!userSharedPref.getString(SignUpFragment.FIRST_NAME, "123").equals("123")) {
            currentUser = new User();
            currentUser.setFirstName(userSharedPref.getString(SignUpFragment.FIRST_NAME, null));
            currentUser.setLastName(userSharedPref.getString(SignUpFragment.LAST_NAME, null));
            currentUser.setEmail(userSharedPref.getString(SignUpFragment.EMAIL, null));
            currentUser.setPassword(userSharedPref.getString(SignUpFragment.PASSWORD, null));

            userNullableData = userSharedPref.getString(SignUpFragment.IMG, null);
            if(userNullableData != null) {
                String[] paths = userNullableData.split(Pattern.quote("\\"));
                Log.i("PROFILE", paths[0]);
                currentUser.setImageUrl(userSharedPref.getString(SignUpFragment.IMG, null));
                Picasso.get().load(baseUrl + paths[1]).into(userImgView);
                Log.i("PROFILE", baseUrl + paths[1]);
            }
            else
                Log.i("PROFILE", "NULL");


            userNullableData = userSharedPref.getString(SignUpFragment.ADDRESS, null);
            if(userNullableData != null)
                currentUser.setAddress(userSharedPref.getString(SignUpFragment.ADDRESS, null));

            userNullableData = userSharedPref.getString(SignUpFragment.PHONE, null);
            if(userNullableData != null)
                currentUser.setPhone(userSharedPref.getString(SignUpFragment.PHONE, null));

        }

        return  view;
    }

}
