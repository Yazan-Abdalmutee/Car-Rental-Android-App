package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.DataBase.DatabaseManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


// create a car record class

public class ConnectActivity extends AppCompatActivity {
    DatabaseManager db = MyApplication.getDatabaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide the action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_page);
        // --- animation ---
        // animate the button to be faded in after 2 seconds
        Button connect = findViewById(R.id.connectButton);

        connect.setAlpha(0f);
        connect.animate().alpha(1f).setDuration(1000).setStartDelay(1000);
        // animate the logo to drop down in 2 seconds
        ImageView logo = findViewById(R.id.logo);
        logo.setTranslationY(-3000f);
        logo.animate().translationYBy(3000f).setDuration(2000);
        // animate the slogan to appear from the left in 2 seconds
        TextView slogan = findViewById(R.id.connectSlogan);
        slogan.setTranslationX(-3000f);
        slogan.setAlpha(0.1f);
        slogan.animate().alpha(1f).setDuration(2000);
        slogan.animate().translationXBy(3000f).setDuration(2000);
        View divider = findViewById(R.id.divider);
        divider.setTranslationX(-3000f);
        divider.setAlpha(0.1f);
        divider.animate().alpha(1f).setDuration(2000);
        divider.animate().translationXBy(3000f).setDuration(2000);
        // -- end of animation --

        connect.setOnClickListener(v -> {
            /// add transition to the next page
            if (db.getCarCount() > 0) {
                Intent intent = new Intent(ConnectActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            } else {
                new NetworkTask().execute();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String link = "https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/all-vehicles-model/records?select=make%2Cmodel%2Cdrive%2Cfueltype%2Cyear%2Cvclass&limit=100";
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
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(apiResult);

                    // Remove "total_count" attribute
                    if (jsonNode.has("total_count")) {
                        ((ObjectNode) jsonNode).remove("total_count");
                    }
                    int i=1;
                    JsonNode resultsArray = jsonNode.get("results");
                    String[] car_make = {"BMW", "Kia", "Ford", "Audi"};
                    if (resultsArray.isArray()) {
                        for (JsonNode result : resultsArray) {
                            if (Arrays.asList(car_make).contains(result.get("make").asText())) {
                                String drawableName = "cars/" + i;
                                i++;
                                int drawableResourceId = getResources().getIdentifier(drawableName, "drawable", getPackageName());

                                String make = result.get("make").asText();
                                String model = result.get("model").asText();
                                String drive = result.get("drive").asText();
                                String fuelType = result.get("fueltype").asText();
                                int randomInt = (int) (Math.random() * 40) + 1;
                                int year = Integer.parseInt(result.get("year").asText());
                                if (year > 2020) year -= randomInt;
                                int catOffer=0;
                                String vehicleClass = result.get("vclass").asText();
                                int price = (int) (Math.random() * 90000 + 10000);
                                if(price>60000)
                                {
                                 catOffer=price-((price*10)/100);
                                }
                                db.insertCar(make, drive, model, year, price, vehicleClass, fuelType,catOffer,null);
                                if (db.getCarCount() == 15) break;
                            }
                        }
                        Toast toast = Toast.makeText(ConnectActivity.this, "Loaded " + db.getCarCount() + " Cars", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ConnectActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(ConnectActivity.this, "API Call Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

