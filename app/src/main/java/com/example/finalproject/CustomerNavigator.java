package com.example.finalproject;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;

public class CustomerNavigator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout root_layout;
    FragmentManager fragmentManager;
    Dialog filterDialog;
    ArrayList<CarItemFragment> listOfFragments;
    boolean[] pages =new boolean[3];
    private ChipGroup chipGroupMake, chipGroupFuelType;
    private RangeSlider sliderYear, sliderPrice;
    SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Arrays.fill(pages, false);
        setContentView(R.layout.activity_home_layout);
        if (!sharedPreferencesManager.getSignedIn()) {
            try {
                Cursor cursor = MyApplication.getDatabaseManager().customerInfo(sharedPreferencesManager.getEmail());

                if (cursor != null && cursor.moveToFirst()) {
                    // Retrieve and process data from the cursor
                    @SuppressLint("Range") String customerEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_EMAIL));
                    @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_FIRST_NAME));
                    @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_LAST_NAME));
                    @SuppressLint("Range") String passwordHashed = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_PASSWORD_HASHED));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_PHONE_NUMBER));
                    @SuppressLint("Range") String country = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_COUNTRY));
                    @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_CITY));
                    @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_GENDER));

                    // Ensure that integer columns have valid values
                    @SuppressLint("Range") int isAdmin = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_ADMIN));
                    // Save the retrieved information into SharedPreferences
                    sharedPreferencesManager.saveUserInfo(customerEmail, firstName, lastName, passwordHashed,
                            phoneNumber, country, city, gender, isAdmin);
                } else {
                    // Handle the case where no matching record is found
                    Toast.makeText(this, "No customer found for the signed-in email", Toast.LENGTH_SHORT).show();
                }

                // Close the cursor when done
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                // Handle any exceptions here
                e.printStackTrace();
                Toast.makeText(this, "An error happened in getSignedIn()", Toast.LENGTH_SHORT).show();
            }
        }
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
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.adminMenu).setVisible(sharedPreferencesManager.getIsAdmin() == 1);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.emailHeader);
        email.setText(sharedPreferencesManager.getEmail());
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
        String n = sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName();
        name.setText(n);

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
            Arrays.fill(pages, false);
            if (!(currentFragment instanceof HomeFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.layout_root, new HomeFragment(), "HomeFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.carMenuItem && !pages[0]) {

            DatabaseManager db = MyApplication.getDatabaseManager();
            Cursor AllCarscursor = db.getCarsNotInReservations();
            listOfFragments = getCars(AllCarscursor,"car");

        }
        else if (id == R.id.favorite_menu && !pages[1]) {
            DatabaseManager db = MyApplication.getDatabaseManager();
            Cursor AllCarscursor = db.getFavoriteCarsForUser(sharedPreferencesManager.getEmail());
            listOfFragments = getCars(AllCarscursor,"favorite");
        }

        else if (id == R.id.reservations_menu && !pages[2]) {
            DatabaseManager db = MyApplication.getDatabaseManager();
            Cursor AllCarscursor = db.getReservationsCarsForUser(sharedPreferencesManager.getEmail());
            listOfFragments = getCars(AllCarscursor,"reservation");
        }

        else if (id == R.id.contactMenuItem) {
            Arrays.fill(pages, false);
            if (!(currentFragment instanceof ContactFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);

                fragmentTransaction.replace(R.id.layout_root, new ContactFragment(), "ContactFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.signOutMenuItem) {
            Arrays.fill(pages, false);
            sharedPreferencesManager.setSignedIn(false);
            sharedPreferencesManager.clearAllButRememberMe();
            Intent intent = new Intent(CustomerNavigator.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.profileMenuItem) {
            Arrays.fill(pages, false);
            if (!(currentFragment instanceof ProfileFragment)) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.layout_root, new ProfileFragment(), "ProfileFrag");
                fragmentTransaction.commit();

            }
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
            listOfFragments = getCars(cursor,"car");
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
        Cursor cursor = db.getCarsByFilterAndNotInReservations(cars_make, fuelTypes, (int) minSlider1, (int) maxSlider1, (int) minSlider2, (int) maxSlider2);

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


    public ArrayList<CarItemFragment> getCars(Cursor cursor,String  page) {
        ArrayList<CarItemFragment> fragmentList = new ArrayList<>();
        Arrays.fill(pages, false);
        if (page.equalsIgnoreCase("car"))
            pages[0] = true;
        else if (page.equalsIgnoreCase("favorite"))
            pages[1] = true;
        else if (page.equalsIgnoreCase("reservation"))
            pages[2] = true;

        removeFragments();


        String carMake;
        String carModel;
        int carYear;
        int cardId;
        double carPrice;
        String carFuelType;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (cursor!=null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.moveToNext()) {
                    cardId = cursor.getInt(0);
                    carMake = cursor.getString(1);
                    carModel = cursor.getString(2);
                    carYear = Integer.parseInt(cursor.getString(3));
                    carPrice = Double.parseDouble(cursor.getString(4));
                    carFuelType = cursor.getString(7);

                    CarItemFragment carItemFragment = new CarItemFragment();
                    Bundle args = new Bundle();
                    args.putInt("carId", cardId);
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





