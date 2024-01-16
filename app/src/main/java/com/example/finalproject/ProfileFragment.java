package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;


public class ProfileFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        Button edit = view.findViewById(R.id.edit_profile);
        TextView name = view.findViewById(R.id.name_textField);
        TextView email = view.findViewById(R.id.email_textField);

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
        Toast.makeText(getContext(), sharedPreferencesManager.getFirstName(), Toast.LENGTH_SHORT).show();
        name.setText(sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName());
        email.setText(sharedPreferencesManager.getEmail());
        assert getActivity() != null;
        TextInputLayout phoneLayout = view.findViewById(R.id.phone_text_input);
        EditText phone = phoneLayout.getEditText();
        Button cancel = view.findViewById(R.id.cancel_button);
        switch (sharedPreferencesManager.getCountry()) {
            case "PS":
            case "default":
                phoneLayout.setPrefixText("+970");
                break;
            case "JO":
                phoneLayout.setPrefixText("+962");
                break;
            case "DE":
                phoneLayout.setPrefixText("+49");
                break;
            case "ES":
                phoneLayout.setPrefixText("+34 ");
                break;
            case "JP":
                phoneLayout.setPrefixText("+81 ");
                break;
        }
        phoneLayout.setPrefixTextAppearance(R.style.prefixTextAppearance);
        phoneLayout.setPlaceholderText(sharedPreferencesManager.getPhone());

        TextInputLayout firstNameLayout = view.findViewById(R.id.fName_text_input);
        EditText firstName = firstNameLayout.getEditText();
        firstNameLayout.setPlaceholderText(sharedPreferencesManager.getFirstName());
        TextInputLayout lastNameLayout = view.findViewById(R.id.lName_text_input);
        EditText lastName = lastNameLayout.getEditText();
        lastNameLayout.setPlaceholderText(sharedPreferencesManager.getLastName());
        TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
        EditText password = passwordLayout.getEditText();

        TextInputLayout confirmPasswordLayout = view.findViewById(R.id.cPassword_text_input);
        EditText confirmPassword = confirmPasswordLayout.getEditText();

        assert firstName != null;
        assert lastName != null;
        assert phone != null;
        assert password != null;
        assert confirmPassword != null;

        LinearLayout form = view.findViewById(R.id.linearLayout);
        form.setVisibility(View.GONE);
        Button save = view.findViewById(R.id.saveButton);
        save.setVisibility(View.GONE);
        edit.setOnClickListener(v -> {
            Transition transition = new Slide();
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);

            form.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        });

        cancel.setOnClickListener(v -> {
            Transition transition = new Slide();
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
            form.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            firstName.setText("");
            lastName.setText("");
            phone.setText("");
            password.setText("");
            confirmPassword.setText("");
        });
        save.setOnClickListener(v -> {
            Transition transition = new Slide();
            TransitionManager.beginDelayedTransition((ViewGroup) view, transition);
            if (firstNameLayout.getError() != null || lastNameLayout.getError() != null || phoneLayout.getError() != null || passwordLayout.getError() != null || confirmPasswordLayout.getError() != null) {
                Toast.makeText(getContext(), "Please fix the errors", Toast.LENGTH_SHORT).show();
                return;
            }

            if (firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty() && phone.getText().toString().isEmpty() && password.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please fill at least one field", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseManager databaseManager = MyApplication.getDatabaseManager();

            String passwordHashed = PasswordHasher.hashPassword(password.getText().toString());
            databaseManager.editCustomer(sharedPreferencesManager.getEmail(), firstName.getText().toString(), lastName.getText().toString(), phone.getText().toString(), password.getText().toString());
            sharedPreferencesManager.setFirstName(firstName.getText().toString());
            sharedPreferencesManager.setLastName(lastName.getText().toString());
            sharedPreferencesManager.setPhone(phone.getText().toString());
            sharedPreferencesManager.setPasswordHashed(passwordHashed);
            phone.setText("");
            password.setText("");
            confirmPassword.setText("");
            firstName.setText("");
            lastName.setText("");
            name.setText(sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName());
            Activity parentActivity = getActivity();
            assert parentActivity != null;
            NavigationView navigationView = parentActivity.findViewById(R.id.navigation);
            TextView email_header = navigationView.getHeaderView(0).findViewById(R.id.emailHeader);
            email_header.setText(sharedPreferencesManager.getEmail());
            TextView name_header = navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
            String n = sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName();
            name_header.setText(n);
            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            form.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (firstName.getText().toString().length() < 3 && firstName.getText().toString().length() > 0) {
                    if (firstNameLayout.getError() != null) return;
                    firstNameLayout.setError("First name is too short");
                } else
                    firstNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (lastName.getText().toString().length() < 3 && lastName.getText().toString().length() > 0) {
                    if (lastNameLayout.getError() != null) return;
                    lastNameLayout.setError("Last name is too short");
                } else
                    lastNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!SignUpActivity.isValidPassword(password.getText().toString()) && !password.getText().toString().isEmpty()) {
                    if (passwordLayout.getError() != null) return;
                    passwordLayout.setError("Password must contain at least 5 characters, one uppercase, one lowercase, one number and one special character");
                } else
                    passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });
        //check if the confirm password matches the password
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!confirmPassword.getText().toString().equals(password.getText().toString()) && !confirmPassword.getText().toString().isEmpty()) {
                    if (confirmPasswordLayout.getError() != null) return;
                    confirmPasswordLayout.setError("Passwords do not match");
                } else
                    confirmPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });
        return view;
    }
}