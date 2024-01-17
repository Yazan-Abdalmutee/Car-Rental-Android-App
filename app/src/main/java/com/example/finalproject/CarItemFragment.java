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

import com.airbnb.lottie.LottieAnimationView;


public class CarItemFragment extends Fragment {

    Dialog dialog;
    Button reserveButton;
    ImageButton closeButton;
    String carMake;
    String carModel;
    String carFuelType;
    int carYear;
    double carPrice;
    private boolean btnFavClicked = false;

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

        LottieAnimationView favouriteAnimation = view.findViewById(R.id.filter_button);

        CardView cardView = view.findViewById(R.id.car_card);

        cardView.setBackgroundResource(R.drawable.card_border);

        Bundle args = getArguments();

        carMake = args.getString("carMake", "");
        carModel = args.getString("carModel", "");
        carFuelType = args.getString("carFuelType", "");
        carYear = args.getInt("carYear", 0);
        carPrice = args.getDouble("carPrice", 0.0);

        TextView car_make = view.findViewById(R.id.car_make);
        TextView car_model = view.findViewById(R.id.car_model);
        TextView car_fuelType = view.findViewById(R.id.car_fuelType);
        TextView car_year = view.findViewById(R.id.car_year);
        TextView car_price = view.findViewById(R.id.car_price);

        car_make.setText(carMake);
        car_model.setText(carModel);
        car_fuelType.setText(carFuelType);
        car_year.setText(String.valueOf(carYear));
        car_price.setText(carPrice + "$");


        favouriteAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnFavClicked) {

                    favouriteAnimation.setSpeed(2.5f);
                    favouriteAnimation.playAnimation();
                    btnFavClicked = true;
                } else {
                    favouriteAnimation.cancelAnimation();
                    favouriteAnimation.setProgress(0f);

                    btnFavClicked = false;
                }
            }
        });
        reserveButton = view.findViewById(R.id.reserve_buton);


        reserveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog = new Dialog(requireActivity());
                dialog.setContentView(R.layout.activity_pop_up_reserved_menu);
                ImageView closeButton = dialog.findViewById(R.id.dialog_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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