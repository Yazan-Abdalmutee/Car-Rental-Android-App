package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
            new NetworkTask().execute();
        });


    }

    @SuppressLint("StaticFieldLeak")
    private class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/all-vehicles-model/records?select=make%2Cmodel%2Cdrive%2Cfueltype%2Cyear%2Cvclass%2Ccreatedon%2Cmodifiedon&limit=10";
                URL url = new URL(link); // Replace with your API endpoint
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String apiResult) {
            // check if the API call was successful using the code
            if (apiResult != null) {
                Intent intent = new Intent(ConnectActivity.this, CustomerNavigator.class);
                startActivity(intent);
            }
        }
    }
}