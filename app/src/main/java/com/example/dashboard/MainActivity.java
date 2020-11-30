package com.example.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawer;
    private CardView netCard, actCard, dataCard;
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.nav_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
//                break;
//            case R.id.nav_logout:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogoutFragment()).commit();
//                break;
//            default:
//                return true;
//
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                switch(id){
                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        loadFragment (fragment);
                        break;
                    case R.id.nav_logout:
                        fragment = new LogoutFragment();
                        loadFragment(fragment);
                        break;
                    default:
                        return true;
                }
                return true;
            }

            private void loadFragment(Fragment fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame,fragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                fragmentTransaction.addToBackStack(null);
            }
        });





        netCard = (CardView) findViewById(R.id.card1);
        actCard = (CardView) findViewById(R.id.card2);
        dataCard = (CardView) findViewById(R.id.card3);

        netCard.setOnClickListener(this);
        actCard.setOnClickListener(this);
        dataCard.setOnClickListener(this);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        break;

                    case R.id.calendar:
                        Intent intent1 = new Intent(MainActivity.this, Calendar.class);
                        startActivity(intent1);
                        break;

                    case R.id.notif:
                        Intent intent2 = new Intent(MainActivity.this, Notification.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });
    }





    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.card1:
                i = new Intent(this, NetworkSpeed.class);
                startActivity(i);
                break;
            case R.id.card2:
                i = new Intent(this, ActivitiesToday.class);
                startActivity(i);
                break;
            case R.id.card3:
                i = new Intent(this, DataPlan.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }


}