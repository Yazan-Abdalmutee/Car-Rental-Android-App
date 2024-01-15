package com.example.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

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

public class CustomerNavigator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout root_layout;
    FragmentManager fragmentManager;
    SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        sharedPreferencesManager.setSignedIn(true);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle("Home");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
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
        if (item.isCheckable()) toolbar.setTitle(item.getTitle());
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
        } else if (id == R.id.contactMenuItem) {
            if (!(currentFragment instanceof ContactFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_root, new ContactFragment(), "ContactFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.signOutMenuItem) {
            sharedPreferencesManager.setSignedIn(false);
            Intent intent = new Intent(CustomerNavigator.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        // close drawer when item is tapped
        drawerLayout.closeDrawers();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

}