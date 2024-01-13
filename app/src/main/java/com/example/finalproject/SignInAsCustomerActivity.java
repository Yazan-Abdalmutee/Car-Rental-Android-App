package com.example.finalproject;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class SignInAsCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout root_layout;
    FragmentManager fragmentManager;
    HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);

        toolbar = findViewById(R.id.toolbar_home);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigaton);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.homeItem);
        root_layout = findViewById(R.id.layout_root);
        fragmentManager = getSupportFragmentManager();
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.homeItem));


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.layout_root);

        int id = item.getItemId();
        if (id == R.id.homeItem) {
            if (!(currentFragment instanceof HomeFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_root, new HomeFragment(), "HomeFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.carMenuItem) {

            if (!(currentFragment instanceof CarMenuFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_root, new CarMenuFragment(), "CarMenuFrag");
                fragmentTransaction.commit();
            }
        }
        return true;
    }
}