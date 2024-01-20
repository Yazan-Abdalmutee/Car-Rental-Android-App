package com.example.finalproject.Admin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalproject.DataBase.DatabaseManager;
import com.example.finalproject.MyApplication;
import com.example.finalproject.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewAllReserves extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_reserves, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.view_reserves);
        linearLayout.removeAllViews();
        DatabaseManager db = MyApplication.getDatabaseManager();
        Cursor cursor = db.getAllReserves();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            String customer_firstName = cursor.getString(0);
            String customer_lastName = cursor.getString(1);
            String customer_email = cursor.getString(2);
            String reservationDate=cursor.getString(4);
            String model=cursor.getString(5);


            View customerCardView = inflater.inflate(R.layout.customer_reservation_card, container, false);
            TextView name = customerCardView.findViewById(R.id.nameHeader_reserve);
            TextView email = customerCardView.findViewById(R.id.emailHeader_reserve);
            TextView date=customerCardView.findViewById(R.id.dateHeader_reserve);
            TextView car_model=customerCardView.findViewById(R.id.car_model_header);
            CircleImageView circleImageView=customerCardView.findViewById(R.id.image_reserve);
            byte[] image=cursor.getBlob(3);
            if (image != null && image.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                Glide.with(this)
                        .load(bitmap)
                        .into(circleImageView);
            }
            name.setText(customer_firstName + " " + customer_lastName);
            email.setText(customer_email);
            date.setText(reservationDate);
            linearLayout.addView(customerCardView);
            car_model.setText(model);
        }
        return view;

    }


}