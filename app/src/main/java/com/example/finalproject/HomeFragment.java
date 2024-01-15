package com.example.finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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
                "The fictional history of our car dealership dates back to 1985 when a group of passionate automotive enthusiasts came together to establish a haven for car enthusiasts. Over the years, our dealership has grown into a trusted destination, providing top-notch service and a diverse range of quality vehicles. Committed to customer satisfaction, we take pride in our rich history of delivering exceptional automobiles and creating lasting relationships with our valued customers.\n" );

        return view;
    }



}