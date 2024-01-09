package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(MainActivity.this, SignInAsCustomerActivity.class);
        MainActivity.this.startActivity(intent);
        finish();

//            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
//            MainActivity.this.startActivity(intent);
//            finish();



    }
}