package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        //check if the email is correctly formatted
        assert email != null;
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
                return;
            }
            Toast toast = Toast.makeText(SignInActivity.this, "Welcome back!", Toast.LENGTH_SHORT);
            toast.show();
        });
        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            SignInActivity.this.startActivity(intent);
            finish();
        });

    }
}