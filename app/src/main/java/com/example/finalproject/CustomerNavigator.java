package com.example.finalproject;


import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class CustomerNavigator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout root_layout;
    FragmentManager fragmentManager;
    Dialog filterDialog;
    ArrayList<CarItemFragment> listOfFragments;
    boolean inCarMenuPage = false;
    private ChipGroup chipGroupMake, chipGroupModel, chipGroupFuelType;
    private RangeSlider sliderYear, sliderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { //navigation drawer


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.layout_root);


        int id = item.getItemId();
        if (item.isCheckable()) toolbar.setTitle(item.getTitle());
        if (id == R.id.homeItem) {
            inCarMenuPage = false;
            if (!(currentFragment instanceof HomeFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.layout_root, new HomeFragment(), "HomeFrag");
                fragmentTransaction.commit();
            }
        } else if ((id == R.id.carMenuItem) && (!inCarMenuPage)) {

            DatabaseManager db = MyApplication.getDatabaseManager();
            Cursor cursor = db.getAllCars();
            listOfFragments = getCars(cursor);
//            if (!(currentFragment instanceof CarMenuFragment)) {
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.layout_root, new CarMenuFragment(), "CarMenuFrag");
//                fragmentTransaction.commit();
//            }
        }
        // close drawer when item is tapped
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        assert searchView != null;
        searchView.setQueryHint("Type here car model");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterCardsBySearch(searchView.getQuery().toString(), listOfFragments);
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter_button) {
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showFilterDialog() {
        filterDialog = new Dialog(this);
        filterDialog.setContentView(R.layout.cars_filter);
        Button saveButton = filterDialog.findViewById(R.id.filter_save_button);
        Button cancelButton = filterDialog.findViewById(R.id.filter_cancel_button);

        //chipGroupModel = filterDialog.findViewById(R.id.ship_group_make);
        chipGroupMake = filterDialog.findViewById(R.id.ship_group_make);
        chipGroupFuelType = filterDialog.findViewById(R.id.ship_group_fuelType);
        sliderYear = filterDialog.findViewById(R.id.year_range_slider);
        sliderPrice = filterDialog.findViewById(R.id.price_range_slider);


        saveButton.setOnClickListener(v -> {

            Cursor cursor = getDataAfterFiltering();
            listOfFragments = getCars(cursor);
            filterDialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> filterDialog.dismiss());
        filterDialog.show();
    }


    public Cursor getDataAfterFiltering() {
        boolean isUserInteracting = false;


        sliderYear.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
            }
        });

        sliderPrice.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
            }
        });
        float minSlider1 = sliderYear.getValues().get(0);
        float maxSlider1 = sliderYear.getValues().get(1);

        float minSlider2 = sliderPrice.getValues().get(0);
        float maxSlider2 = sliderPrice.getValues().get(1);
        String[] cars_make = getChipGroupData(chipGroupMake);
        String[] fuelTypes = getChipGroupData(chipGroupFuelType);
        DatabaseManager db = MyApplication.getDatabaseManager();
        Cursor cursor = db.getCarsByFilter(cars_make, fuelTypes, (int) minSlider1, (int) maxSlider1, (int) minSlider2, (int) maxSlider2);

        return cursor;
    }


    public void filterCardsBySearch(String search, ArrayList<CarItemFragment> fragments) {

        for (Fragment fragment : fragments) {

            CarItemFragment currentFragment = (CarItemFragment) fragment;
            String make = currentFragment.getCarModel();
            if (make.toLowerCase().contains(search.toLowerCase())) {
                currentFragment.setVisibility(true);
            } else {
                currentFragment.setVisibility(false);
            }


        }
    }


    public String[] getChipGroupData(ChipGroup chipGroup) {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                dataList.add(chip.getText().toString());
            }
        }
        return dataList.toArray(new String[0]);
    }


    public ArrayList<CarItemFragment> getCars(Cursor cursor) {
        ArrayList<CarItemFragment> fragmentList = new ArrayList<>();

        inCarMenuPage = true;

        removeFragments();

        int carCounts = cursor.getCount();

        String carMake;
        String carModel;
        int carYear;
        double carPrice;
        String carFuelType;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < carCounts; i++) {
            if (cursor.moveToNext()) {
                carMake = cursor.getString(1);
                carModel = cursor.getString(2);
                carYear = Integer.parseInt(cursor.getString(3));
                carPrice = Double.parseDouble(cursor.getString(4));
                carFuelType = cursor.getString(7);

                CarItemFragment carItemFragment = new CarItemFragment();
                Bundle args = new Bundle();
                args.putString("carMake", carMake);
                args.putString("carModel", carModel);
                args.putString("carFuelType", carFuelType);
                args.putInt("carYear", carYear);
                args.putDouble("carPrice", carPrice);
                carItemFragment.setArguments(args);

                fragmentTransaction.add(R.id.layout_root, carItemFragment);
                fragmentList.add(carItemFragment);

            }
        }
        fragmentTransaction.commitNow();
        return fragmentList;


    }

    public void removeFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.findFragmentById(R.id.layout_root) != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.layout_root));
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();
        }
    }

}





