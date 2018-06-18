package com.iti.jets.lostchildren.authorizing;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.iti.jets.lostchildren.R;
import com.iti.jets.lostchildren.homeScreen.ChildDetailsFragment;
import com.iti.jets.lostchildren.reporting.FoundChildReportFragment;
import com.iti.jets.lostchildren.reporting.LostChildReportFragment;
import com.iti.jets.lostchildren.service.LostChildServiceClient;

/**
 * Created by Fadwa on 23/05/2018.
 */

public class HomeActivity extends AppCompatActivity {

    public static final String SIGN_IN_TAG = "SignIn";
    public static final String SIGN_UP_TAG = "SignUp";
    public static final String REQUIRED_FREGMENT = "requiredFragment";
    public static final String LOST_TAG = "Lost";
    public static final String FOUND_TAG = "Found";
    public static final String Details_TAG = "details";

    public static final int minPasswordLength = 6;
    SignInFragment signInFragment;
    Fragment currentFragment;
    String currentFragmentTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String fragmentTag = SIGN_IN_TAG;
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(REQUIRED_FREGMENT))
            fragmentTag = getIntent().getExtras().getString(REQUIRED_FREGMENT);
        switch (fragmentTag){
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
            case Details_TAG:
                currentFragment = new ChildDetailsFragment();
                currentFragmentTag = Details_TAG;
                break;
            default:
                currentFragment = new SignInFragment();
                currentFragmentTag = SIGN_IN_TAG;
                break;
        }
        if (savedInstanceState == null) {

//            signInFragment = new SignInFragment();
//            signInFragment.setArguments(getIntent().getExtras());
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, signInFragment, SIGN_IN_TAG).commit();

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
