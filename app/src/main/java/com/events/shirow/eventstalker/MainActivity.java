package com.events.shirow.eventstalker;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eventstalker.handler.SQLiteHandler;
import com.eventstalker.handler.SessionManager;
import com.outer.fragments.drawer.CarHire_Fragment;
import com.outer.fragments.drawer.Home_fragment;
import com.outer.fragments.drawer.Mychats_Fragment;
import com.outer.fragments.drawer.MyticketsFragment;
import com.outer.fragments.drawer.Search_Fragment;
import com.outer.fragments.drawer.Settings_Fragment;
import com.outer.fragments.drawer.Share_Fragment;
import com.outer.fragments.drawer.Taxi_Fragment;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteHandler db;
    private SessionManager session;


    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View headerLayout = navigationView.getHeaderView(0);
            LinearLayout layout = (LinearLayout) headerLayout.findViewById(R.id.navmain);
            TextView Dusernme = (TextView) layout.findViewById(R.id.logindetails);
            TextView Demail = (TextView) layout.findViewById(R.id.emaildisplay);

        Dusernme.setText("Sign up or Log in");
        Demail.setText("We got you on the best ticket deals!");

        if (session.isLoggedIn()) {
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");

            // Displaying the user details on the screen
            Dusernme.setText(name);
            Demail.setText(email);
        }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (session.isLoggedIn()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("You are already Logged in")
                                .setMessage("Are you sure you want to Logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        logoutUser();
                                        startActivity(new Intent(MainActivity.this, Signup_Login.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        startActivity(new Intent(MainActivity.this, Signup_Login.class));
                    }
                }
            });

            // Set the home as default
            Home_fragment fragment = new Home_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment, "fragment8")
                    .commit();

        }


        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_search);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }

        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return true;
    }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_logout:
                    logoutUser();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();
            if (id == R.id.nav_discover) {
                Home_fragment fragment = new Home_fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment8")
                        .commit();
            } else if (id == R.id.nav_search) {
                Search_Fragment fragment = new Search_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment1")
                        .commit();
            } else if (id == R.id.nav_tickets) {
                MyticketsFragment fragment = new MyticketsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment2")
                        .commit();

            } else if (id == R.id.nav_chats) {
                Mychats_Fragment fragment = new Mychats_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment3")
                        .commit();

            } else if (id == R.id.nav_share) {
                Share_Fragment fragment = new Share_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment4")
                        .commit();


            } else if (id == R.id.nav_settings) {
                Settings_Fragment fragment = new Settings_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment5")
                        .commit();

            } else if (id == R.id.nav_taxi) {
                Taxi_Fragment fragment = new Taxi_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment6")
                        .commit();

            } else if (id == R.id.nav_hireride) {
                CarHire_Fragment fragment = new CarHire_Fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frame, fragment, "fragment7")
                        .commit();

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    private void logoutUser() {
        session.setLogin(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        LinearLayout layout = (LinearLayout) headerLayout.findViewById(R.id.navmain);
        TextView Dusernme = (TextView) layout.findViewById(R.id.logindetails);
        TextView Demail = (TextView) layout.findViewById(R.id.emaildisplay);

        Dusernme.setText("Sign up or Log in");
        Demail.setText("We got you on the best ticket deals!");

        db.deleteUsers();

        Home_fragment fragment = new Home_fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame,fragment,"fragment8")
                .commit();

    }

}
