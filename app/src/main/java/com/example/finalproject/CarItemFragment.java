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

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;


public class CarItemFragment extends Fragment {

    Dialog dialog;
    Button reserveButton;
    ImageButton closeButton;
    private boolean btnFavClicked=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_item, container, false);

        ImageView expandButton = view.findViewById(R.id.expand_button);

        final LinearLayout expandedData = view.findViewById(R.id.expanded_layot);

        LottieAnimationView favouriteAnimation=view.findViewById(R.id.favorite);
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
                expandButton.setImageResource(expandedData.getVisibility()==View.VISIBLE ? R.drawable.arrow_up: R.drawable.expand_button);
            }
        });
        return view;
    }


}