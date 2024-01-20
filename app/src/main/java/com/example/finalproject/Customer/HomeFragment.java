package com.example.finalproject.Customer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;


public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView text =view.findViewById(R.id.textView_homePage);
        text.setText("\n" +
                "Capital Cars Rental, established in 2008, has been a trusted name in the car rental industry. With a diverse fleet of vehicles and a commitment to customer satisfaction, they provide convenient and reliable transportation solutions for various travel needs.");

        return view;
    }



}