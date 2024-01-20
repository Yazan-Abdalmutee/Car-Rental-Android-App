package com.example.finalproject.Admin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalproject.DataBase.DatabaseManager;
import com.example.finalproject.MyApplication;
import com.example.finalproject.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class DeleteCustomer extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_customer, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.view);
        // fill the linearLayout with the customer cards
        linearLayout.removeAllViews();
        DatabaseManager db = MyApplication.getDatabaseManager();
        Cursor cursor = db.getAllCustomers();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            String customer_email = cursor.getString(0);
            String customer_firstName = cursor.getString(1);
            String customer_lastName = cursor.getString(2);



            View customerCardView = inflater.inflate(R.layout.customer_card_delete, container, false);
            TextView name = customerCardView.findViewById(R.id.nameHeader);
            TextView email = customerCardView.findViewById(R.id.emailHeader);
            CircleImageView circleImageView=customerCardView.findViewById(R.id.image);
            byte[] image=cursor.getBlob(9);
            if (image != null && image.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                Glide.with(this)
                        .load(bitmap)
                        .into(circleImageView);
            }

            name.setText(customer_firstName + " " + customer_lastName);
            email.setText(customer_email);
            Button deleteButton = customerCardView.findViewById(R.id.button);
            deleteButton.setOnClickListener(v -> {
                db.deleteCustomer(customer_email);
                linearLayout.removeView(customerCardView);
            });
            linearLayout.addView(customerCardView);
        }
        return view;

    }


}