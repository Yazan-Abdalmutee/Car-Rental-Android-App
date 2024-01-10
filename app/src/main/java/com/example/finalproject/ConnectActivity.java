package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;


// create a car record class

public class ConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide the action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_page);
        // --- animation ---
        // animate the button to be faded in after 2 seconds
        Button connect = findViewById(R.id.connectButton);
        connect.setAlpha(0f);
        connect.animate().alpha(1f).setDuration(1000).setStartDelay(2000);
        // animate the logo to drop down in 2 seconds
        ImageView logo = findViewById(R.id.logo);
        logo.setTranslationY(-3000f);
        logo.animate().translationYBy(3000f).setDuration(3000);
        // animate the slogan to appear from the left in 2 seconds
        TextView slogan = findViewById(R.id.connectSlogan);
        slogan.setTranslationX(-3000f);
        slogan.setAlpha(0.1f);
        slogan.animate().alpha(1f).setDuration(3000);
        slogan.animate().translationXBy(3000f).setDuration(3000);
        View divider = findViewById(R.id.divider);
        divider.setTranslationX(-3000f);
        divider.setAlpha(0.1f);
        divider.animate().alpha(1f).setDuration(3000);
        divider.animate().translationXBy(3000f).setDuration(3000);
        // -- end of animation --

        connect.setOnClickListener(v -> {
            /// add transition to the next page

            Intent intent = new Intent(ConnectActivity.this, SignInActivity.class);
            ConnectActivity.this.startActivity(intent);
            finish();
        });



    }
}