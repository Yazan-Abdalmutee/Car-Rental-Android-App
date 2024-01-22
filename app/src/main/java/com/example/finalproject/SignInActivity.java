package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Customer.CustomerNavigator;
import com.example.finalproject.DataBase.DatabaseManager;
import com.example.finalproject.DataBase.PasswordHasher;
import com.example.finalproject.DataBase.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        TextInputLayout passwordLayout = findViewById(R.id.password_text_input);
        EditText password = passwordLayout.getEditText();
        TextInputLayout emailLayout = findViewById(R.id.email_text_input);
        EditText email = emailLayout.getEditText();
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        assert email != null;
        if (sharedPreferencesManager.getEmail() != null) {
            email.setText(sharedPreferencesManager.getRememberMe());
        }
        //check if the email is correctly formatted
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !email.getText().toString().isEmpty()) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    emailLayout.setError("Please enter a valid email address");
                } else {
                    emailLayout.setError(null);
                }
            }
        });

        Button signIn = findViewById(R.id.signIn_button);
        signIn.setOnClickListener(v -> {
            if (email.getText().toString().isEmpty() || Objects.requireNonNull(password).getText().toString().isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseManager databaseManager = MyApplication.getDatabaseManager();
                String HashedPassword = PasswordHasher.hashPassword(password.getText().toString());
                if (databaseManager.isLoginCredentialsValid(email.getText().toString(), HashedPassword)) {
                    Intent intent = new Intent(SignInActivity.this, CustomerNavigator.class);
                    CheckBox rememberMe = findViewById(R.id.rememberMe);
                    if (rememberMe.isChecked()) {
                        sharedPreferencesManager.rememberMe(email.getText().toString().toLowerCase());

                    } else {
                        sharedPreferencesManager.rememberMe(null);
                    }
                    sharedPreferencesManager.setEmail(email.getText().toString().toLowerCase());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }


        });
        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            SignInActivity.this.startActivity(intent);
        });

    }
}