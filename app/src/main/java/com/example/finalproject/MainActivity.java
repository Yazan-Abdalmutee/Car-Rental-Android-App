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
        Intent testIntent = new Intent(MainActivity.this, ConnectActivity.class);
        MainActivity.this.startActivity(testIntent);
        finish();
        Button login = findViewById(R.id.button_Login);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        });

        Button signUp = findViewById(R.id.button_signUp);
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        });
        Button connect = findViewById(R.id.button_connectt);
        connect.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        });
    }
}