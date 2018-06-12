package com.iti.jets.lostchildren.authorizing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iti.jets.lostchildren.R;

/**
 * Created by Fadwa on 23/05/2018.
 */

public class HomeActivity extends AppCompatActivity {

    public static final String SIGN_IN_TAG = "SignIn";
    public static final String SIGN_UP_TAG = "SignUp";
    public static final int minPasswordLength = 6;
    SignInFragment signInFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            signInFragment = new SignInFragment();
            signInFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, signInFragment, SIGN_IN_TAG).commit();
        } else {
            signInFragment = (SignInFragment) getSupportFragmentManager().findFragmentByTag(SIGN_IN_TAG);
        }
    }

}
