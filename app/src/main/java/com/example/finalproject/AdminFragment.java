package com.example.finalproject;

import static com.example.finalproject.SignUpActivity.isValidPassword;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;


public class AdminFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_admin, container, false);


        TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
        EditText password = passwordLayout.getEditText();
        passwordLayout.setErrorIconDrawable(null);
        TextInputLayout emailLayout = view.findViewById(R.id.email_text_input);
        EditText email = emailLayout.getEditText();
        TextInputLayout firstNameLayout = view.findViewById(R.id.fName_text_input);
        EditText firstName = firstNameLayout.getEditText();
        TextInputLayout lastNameLayout = view.findViewById(R.id.lName_text_input);
        EditText lastName = lastNameLayout.getEditText();
        TextInputLayout confirmPasswordLayout = view.findViewById(R.id.cPassword_text_input);
        confirmPasswordLayout.setErrorIconDrawable(null);
        EditText confirmPassword = confirmPasswordLayout.getEditText();
        TextInputLayout phoneLayout = view.findViewById(R.id.phone_text_input);
        EditText phone = phoneLayout.getEditText();
        //check if the password is correctly formatted
        assert password != null;
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidPassword(password.getText().toString()) && !password.getText().toString().isEmpty()) {
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
        assert confirmPassword != null;
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

        assert email != null;
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches() && !email.getText().toString().isEmpty()) {
                    if (emailLayout.getError() != null) return;
                    emailLayout.setError("Please enter a valid email address");
                } else
                    emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });


        assert firstName != null;
        assert lastName != null;
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
        final AutoCompleteTextView countryAuto = view.findViewById(R.id.customerTextView);
        final AutoCompleteTextView cityAuto = view.findViewById(R.id.citySpinnerAutoCompleteTextView);
        final AutoCompleteTextView genderAuto = view.findViewById(R.id.genderAutoCompleteTV);
        assert getContext() != null;
        ArrayAdapter<String> genderOptions = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_options));
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries_array));
        countryAuto.setAdapter(countriesAdapter);
        genderAuto.setAdapter(genderOptions);
        cityAuto.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Palestine)));
        assert getActivity() != null;
        genderAuto.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        countryAuto.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        cityAuto.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        countryAuto.setKeyListener(null);
        cityAuto.setKeyListener(null);
        genderAuto.setKeyListener(null);
        // select the first item from the list
        countryAuto.setText(countriesAdapter.getItem(0), false);
        phoneLayout.setPrefixText("+970 ");
        countryAuto.setOnItemClickListener((parent, v, position, id) -> {
            String country1 = countryAuto.getText().toString();
            ArrayAdapter<String> citiesAdapter = null;
            switch (country1) {
                case "PS":
                    phoneLayout.setPrefixText("+970 ");
                    citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Palestine));
                    break;
                case "JO":
                    phoneLayout.setPrefixText("+962 ");
                    citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Jordan));
                    break;
                case "DE":
                    phoneLayout.setPrefixText("+49 ");
                    citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Germany));
                    break;
                case "ES":
                    phoneLayout.setPrefixText("+34 ");
                    citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Spain));
                    break;
                case "JP":
                    phoneLayout.setPrefixText("+81 ");
                    citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cities_Japan));
                    break;
            }
            phoneLayout.setPrefixTextAppearance(R.style.prefixTextAppearance);
            cityAuto.setAdapter(citiesAdapter);
        });
        Button addAdmin = view.findViewById(R.id.addAdmin);
        addAdmin.setOnClickListener(v -> {
            if (emailLayout.getError() != null || passwordLayout.getError() != null || confirmPasswordLayout.getError() != null || firstNameLayout.getError() != null || lastNameLayout.getError() != null || phone.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields correctly", Toast.LENGTH_SHORT).show();
            } else {
                // check if the email is already registered
                DatabaseManager db = MyApplication.getDatabaseManager();
                if (db.isEmailUsed(email.getText().toString())) {
                    Toast.makeText(getContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                    emailLayout.setError("This email is already registered");
                } else {
                    // encrypt the password
                    String hashedPassword = PasswordHasher.hashPassword(password.getText().toString());
                    // insert the customer into the database
                    db.insertCustomer(email.getText().toString().trim(), firstName.getText().toString().trim(), lastName.getText().toString().trim(), hashedPassword, phone.getText().toString().trim(), genderAuto.getText().toString(), countryAuto.getText().toString(), cityAuto.getText().toString(), 1);
                    Toast.makeText(getContext(), "You have successfully registered", Toast.LENGTH_SHORT).show();
                    // clear all fields
                    email.setText("");
                    password.setText("");
                    confirmPassword.setText("");
                    firstName.setText("");
                    lastName.setText("");
                    phone.setText("");
                    genderAuto.setText("");
                    countryAuto.setText("");
                    cityAuto.setText("");
                    countryAuto.setText(countriesAdapter.getItem(0), false);
                    phoneLayout.setPrefixText("+970 ");
                }
            }
        });
        return view;

    }
}