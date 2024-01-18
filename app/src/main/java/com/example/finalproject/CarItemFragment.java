package com.example.finalproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CarItemFragment extends Fragment {

    Dialog dialog;
    Button reserveButton;
    String carMake;
    String carModel;
    String carFuelType;
    int carYear, carId;
    double carPrice;
    DatabaseManager db = MyApplication.getDatabaseManager();
    private final boolean btnFavClicked = false;
    private boolean isCarInFavorites = false;

    public String getCarModel() {
        return carModel;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public void setVisibility(boolean isVisible) {
        if (getView() != null) {
            getView().setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_item, container, false);

        ImageView expandButton = view.findViewById(R.id.expand_button);

        final LinearLayout expandedData = view.findViewById(R.id.expanded_layot);

        ImageView favorite = view.findViewById(R.id.filter_button);

        CardView cardView = view.findViewById(R.id.car_card_dialog);

        cardView.setBackgroundResource(R.drawable.card_border);

        Bundle args = getArguments();

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext());

        String email = sharedPreferencesManager.getEmail();

        carId = args.getInt("carId", 0);
        carMake = args.getString("carMake", "");
        carModel = args.getString("carModel", "");
        carFuelType = args.getString("carFuelType", "");
        carYear = args.getInt("carYear", 0);
        carPrice = args.getDouble("carPrice", 0.0);

        TextView car_make = view.findViewById(R.id.car_make_dialog);
        TextView car_model = view.findViewById(R.id.car_model_dialog);
        TextView car_fuelType = view.findViewById(R.id.car_fuelType_dialog);
        TextView car_year = view.findViewById(R.id.car_year_dialog);
        TextView car_price = view.findViewById(R.id.car_price_dialog);
        TextView car_reservationDate = view.findViewById(R.id.reservation_date);
        LinearLayout showLayoutDate = view.findViewById(R.id.expand_reserve_date);

        car_make.setText(carMake);
        car_model.setText(carModel);
        car_fuelType.setText(carFuelType);
        car_year.setText(String.valueOf(carYear));
        car_price.setText(carPrice + "$");


        isCarInFavorites = db.isCarInFavorites(carId, email);


        if (isCarInFavorites) {
            favorite.setImageResource(R.drawable.heart_red);
        } else {
            favorite.setImageResource(R.drawable.heart);
        }
        if (db.isCarInReservations(carId)) {
            showLayoutDate.setVisibility(View.VISIBLE);
            car_reservationDate.setText(db.getReservationDateForCar(carId));

        } else {
            showLayoutDate.setVisibility(View.GONE);
        }


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCarInFavorites) {

                    favorite.setImageResource(R.drawable.heart_red);
                    isCarInFavorites = true;
                    db.insertFavorite(carId, email);

                } else {
                    isCarInFavorites = false;
                    favorite.setImageResource(R.drawable.heart);
                    db.removeFavorite(carId, email);


                }
                //db.close();
            }
        });
        reserveButton = view.findViewById(R.id.reserve_button);


        reserveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog = new Dialog(requireActivity());

                dialog.setContentView(R.layout.activity_pop_up_reserved_menu);
                TextView car_make_dialog = dialog.findViewById(R.id.car_make_dialog);
                TextView car_model_dialog = dialog.findViewById(R.id.car_model_dialog);
                TextView car_fuelType_dialog = dialog.findViewById(R.id.car_fuelType_dialog);
                TextView car_year_dialog = dialog.findViewById(R.id.car_year_dialog);
                TextView car_price_dialog = dialog.findViewById(R.id.car_price_dialog);
                Button closeButton = dialog.findViewById(R.id.cancel_button_dialog);
                Button confirm = dialog.findViewById(R.id.confirm_buton_dialog);
                CardView card = dialog.findViewById(R.id.car_card_dialog);
                card.setBackgroundResource(R.drawable.card_border);
                car_make_dialog.setText(carMake);
                car_model_dialog.setText(carModel);
                car_fuelType_dialog.setText(carFuelType);
                car_year_dialog.setText(String.valueOf(carYear));
                car_price_dialog.setText(carPrice + "$");

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!db.isCarInReservations(carId)) ;
                        {
                            Date currentDateTime = Calendar.getInstance().getTime();
                            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateTime = dateTimeFormat.format(currentDateTime);
                            db.insertReservation(carId, email, dateTime);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedData.setVisibility(expandedData.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                expandButton.setImageResource(expandedData.getVisibility() == View.VISIBLE ? R.drawable.arrow_up : R.drawable.expand_button);
            }
        });
        return view;
    }


}