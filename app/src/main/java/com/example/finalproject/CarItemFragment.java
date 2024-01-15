package com.example.finalproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class CarItemFragment extends Fragment {

    Dialog dialog;
    Button reserveButton;
    ImageButton closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_item, container, false);

        TextView expandTextView = view.findViewById(R.id.carFactory);

        final LinearLayout additionalText = view.findViewById(R.id.addtional_info_layout);

        reserveButton = view.findViewById(R.id.button_reserve);
        reserveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog = new Dialog(requireActivity());
                dialog.setContentView(R.layout.activity_pop_up_reserved_menu);
                ImageButton closeButton = dialog.findViewById(R.id.dialog_close);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        expandTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalText.setVisibility(additionalText.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        return view;
    }


}