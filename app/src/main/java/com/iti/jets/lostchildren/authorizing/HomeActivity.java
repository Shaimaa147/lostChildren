package com.iti.jets.lostchildren.authorizing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.homeScreen.ChildDetailsFragment;
import com.iti.jets.lostchildren.homeScreen.MainActivity;
import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.reporting.FoundChildReportFragment;
import com.iti.jets.lostchildren.reporting.LostChildReportFragment;
import com.iti.jets.lostchildren.searchScreen.SearchFragment;
import com.iti.jets.lostchildren.service.LostChildServiceClient;

import static android.widget.Toast.LENGTH_LONG;
import static com.iti.jets.lostchildren.homeScreen.MainActivity.USER_SHARED_PREF;
import static com.iti.jets.lostchildren.homeScreen.MainActivity.currentUser;
import static java.security.AccessController.getContext;

/**
 * Created by Fadwa on 23/05/2018.
 */

public class HomeActivity extends AppCompatActivity {

    public static final String SIGN_IN_TAG = "SignIn";
    public static final String SIGN_UP_TAG = "SignUp";
    public static final String REQUIRED_FREGMENT = "requiredFragment";
    public static final String LOST_TAG = "Lost";
    public static final String FOUND_TAG = "Found";
    public static final String SEARCH_TAG = "Search";
    public static final String Details_TAG = "details";
    private SignInFragment signInFragment;
    private Fragment currentFragment;
    private String currentFragmentTag;
    private SharedPreferences userSharedPref;
    private User currentUser;
    private String userNullableData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userSharedPref = getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);

        String fragmentTag = SIGN_IN_TAG;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(REQUIRED_FREGMENT))
            fragmentTag = getIntent().getExtras().getString(REQUIRED_FREGMENT);
        switch (fragmentTag) {
            case LOST_TAG:
                currentFragment = new LostChildReportFragment();
                LostChildServiceClient.getInstance().setLostChildReportFragment((LostChildReportFragment) currentFragment);
                currentFragmentTag = LOST_TAG;
                break;
            case FOUND_TAG:
                currentFragment = new FoundChildReportFragment();
                LostChildServiceClient.getInstance().setFoundChildReportFragment((FoundChildReportFragment) currentFragment);
                currentFragmentTag = FOUND_TAG;
                break;
            case SEARCH_TAG:
                currentFragment = new SearchFragment();
                currentFragmentTag = SEARCH_TAG;
                break;
            case Details_TAG:
                currentFragment = new ChildDetailsFragment();
                currentFragmentTag = Details_TAG;
                break;
            default:
                currentFragment = new SignInFragment();
                currentFragmentTag = SIGN_IN_TAG;
                break;
        }

        if (currentFragmentTag.equals(SIGN_IN_TAG) || currentFragmentTag.equals(SIGN_UP_TAG)) {
            if (!userSharedPref.getString(SignUpFragment.FIRST_NAME, "123").equals("123")) {
                currentUser = new User();
                currentUser.setFirstName(userSharedPref.getString(SignUpFragment.FIRST_NAME, null));
                currentUser.setLastName(userSharedPref.getString(SignUpFragment.LAST_NAME, null));
                currentUser.setEmail(userSharedPref.getString(SignUpFragment.EMAIL, null));
                currentUser.setPassword(userSharedPref.getString(SignUpFragment.PASSWORD, null));

                userNullableData = userSharedPref.getString(SignUpFragment.IMG, null);
                if(userNullableData != null)
                    currentUser.setImageUrl(userSharedPref.getString(SignUpFragment.IMG, null));

                userNullableData = userSharedPref.getString(SignUpFragment.ADDRESS, null);
                if(userNullableData != null)
                    currentUser.setAddress(userSharedPref.getString(SignUpFragment.ADDRESS, null));

                userNullableData = userSharedPref.getString(SignUpFragment.PHONE, null);
                if(userNullableData != null)
                    currentUser.setPhone(userSharedPref.getString(SignUpFragment.PHONE, null));

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra(MainActivity.LOGGED_IN_USER_JSON, new Gson().toJson(currentUser));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

        if (savedInstanceState == null) {
            currentFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, currentFragment, currentFragmentTag).commit();
        } else {
            signInFragment = (SignInFragment) getSupportFragmentManager().findFragmentByTag(currentFragmentTag);
        }
    }


    private void replaceFragment(AppCompatActivity activity, android.support.v4.app.Fragment fragment, @IdRes int container,
                                 boolean isNeedToAddToStack,
                                 String fragmentTag) {

        FragmentManager manager = activity.getSupportFragmentManager();
        boolean isInStack = manager.popBackStackImmediate(fragmentTag, 0);
        FragmentTransaction ft = manager.beginTransaction();

        if (isInStack) {
            fragment = manager.findFragmentByTag(fragmentTag);
        }

        ft.replace(container, fragment, fragmentTag);
        if (!isInStack && isNeedToAddToStack) {
            ft.addToBackStack(fragmentTag);
        }

        ft.commit();
    }
}
