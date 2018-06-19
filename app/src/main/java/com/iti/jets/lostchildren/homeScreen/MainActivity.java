package com.iti.jets.lostchildren.homeScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.iti.jets.lostchildren.R;

import com.iti.jets.lostchildren.authorizing.HomeActivity;
import com.iti.jets.lostchildren.authorizing.SignUpFragment;
import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.profile.UserProfileFragment;

import org.w3c.dom.Text;

import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String userAddress;
    private String userPhone;
    private String userImgUrl;
    private SharedPreferences userSharedPref;
    private SharedPreferences.Editor sharedPrefEditor;
    private TextView txtViewNavUserEmail;
    private TextView txtViewNavUserName;
    private NavigationView navigationView;
    private LostAndFoundTabsFragment lostAndFoundTabs;

    public static final String LOGGED_IN_USER_JSON = "loggedInUserJson";
    public static final String USER_SHARED_PREF = "userSharedPref";
    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navigationView.getMenu().getItem(0).setChecked(true);

        txtViewNavUserEmail = headerView.findViewById(R.id.nav_user_email);
        txtViewNavUserName = headerView.findViewById(R.id.nav_user_name);

        currentUser = new Gson().fromJson(getIntent().getStringExtra(LOGGED_IN_USER_JSON), User.class);
        txtViewNavUserName.setText(currentUser.getFirstName().toString()
                + " " + currentUser.getLastName().toString());
        txtViewNavUserEmail.setText(currentUser.getEmail());

        userSharedPref = getSharedPreferences(USER_SHARED_PREF, Context.MODE_PRIVATE);

        if(userSharedPref.getString(SignUpFragment.FIRST_NAME, null) == null) {

            sharedPrefEditor = userSharedPref.edit();
            sharedPrefEditor.putString(SignUpFragment.FIRST_NAME, currentUser.getFirstName().toString());
            sharedPrefEditor.putString(SignUpFragment.LAST_NAME, currentUser.getLastName().toString());
            sharedPrefEditor.putString(SignUpFragment.EMAIL, currentUser.getEmail().toString());
            sharedPrefEditor.putString(SignUpFragment.PASSWORD, currentUser.getPassword().toString());
            sharedPrefEditor.putString(SignUpFragment.IMG, currentUser.getImageUrl().toString());

//            userAddress = currentUser.getAddress().toString();
            if (userAddress != null)
                sharedPrefEditor.putString(SignUpFragment.ADDRESS, userAddress);

            userPhone = currentUser.getPhone().toString();
            if (userPhone != null)
                sharedPrefEditor.putString(SignUpFragment.PHONE, currentUser.getPhone().toString());

            if (currentUser.getImageUrl() != null) {
                sharedPrefEditor.putString(SignUpFragment.IMG, currentUser.getImageUrl().toString());
            }

            sharedPrefEditor.commit();
        }

        lostAndFoundTabs = new LostAndFoundTabsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main_content, lostAndFoundTabs, LostAndFoundTabsFragment.LOST_AND_FOUND_TABS)
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the AuthFragmentsHome/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main_content, lostAndFoundTabs, LostAndFoundTabsFragment.LOST_AND_FOUND_TABS)
                    .commit();
            navigationView.getMenu().getItem(0).setChecked(true);

        } else if (id == R.id.reportLost) {
            redirectToMainActivity(HomeActivity.LOST_TAG);

        } else if (id == R.id.reportFound) {
            redirectToMainActivity(HomeActivity.FOUND_TAG);

        } else if (id == R.id.search) {
            //TODO: Redirect to Search
            redirectToMainActivity(HomeActivity.SEARCH_TAG);

        } else if (id == R.id.profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_main_content, new UserProfileFragment(), UserProfileFragment.USER_PROFILE)
                    .commit();
            navigationView.getMenu().getItem(4).setChecked(true);

        } else if (id == R.id.logout) {
            logOut(HomeActivity.SIGN_IN_TAG);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void redirectToMainActivity(String requiredFragment) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra(HomeActivity.REQUIRED_FREGMENT, requiredFragment);
        startActivity(i);
    }

    public void logOut(String requiredFragment) {
        userSharedPref.edit().clear().commit();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra(HomeActivity.REQUIRED_FREGMENT, HomeActivity.SIGN_IN_TAG);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}
