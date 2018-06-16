package com.iti.jets.lostchildren;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iti.jets.lostchildren.authorizing.HomeActivity;
import com.iti.jets.lostchildren.pojos.User;
import com.iti.jets.lostchildren.reporting.LostChildReportFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tableLayout ;
    private ViewPager viewPager;
    private ViewPagerAdpter adpter;

    public static final String LOGGED_IN_USER_JSON = "loggedInUserJson";
    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tableLayout = findViewById(R.id.tableLayoutID);
        viewPager = findViewById(R.id.pagerID);
        adpter = new ViewPagerAdpter(getSupportFragmentManager());
        adpter.addFragment(new FragmentLost() , "Lost Children");
        adpter.addFragment(new FragmentFound(),"Found Children");
        viewPager.setAdapter(adpter);
        tableLayout.setupWithViewPager(viewPager);

        currentUser = new Gson().fromJson(getIntent().getStringExtra(LOGGED_IN_USER_JSON), User.class);
        //Toast.makeText(getApplicationContext(), currentUser.getEmail(), Toast.LENGTH_LONG).show();

        //TODO: Save to shared preferences

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
            // Handle the home action
        } else if (id == R.id.reportLost) {
            redirectToMainActivity(HomeActivity.LOST_TAG);
        } else if (id == R.id.reportFound) {
            redirectToMainActivity(HomeActivity.FOUND_TAG);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

}
