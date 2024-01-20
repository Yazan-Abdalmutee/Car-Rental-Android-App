package com.example.finalproject.Customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;


public class ContactFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact, container, false);
        Button call = view.findViewById(R.id.call);
        Button email = view.findViewById(R.id.mail);
        Button location = view.findViewById(R.id.maps);

        call.setOnClickListener(v -> {
            // open dialer and call the number 059-9999999
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:059-9999999"));
            startActivity(intent);
        });
        email.setOnClickListener(v -> {
            // open email app and send email to the address
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:mabushelbai@gmail.com"));
            startActivity(intent);
        });
        location.setOnClickListener(v -> {
            // open google maps and show the location
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:31.2622,34.8019"));
            startActivity(intent);
        });
        return view;

    }



}