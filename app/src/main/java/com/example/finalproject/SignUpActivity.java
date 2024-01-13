package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_=+-])(?=\\S+$).{5,}$";
        return password != null && password.matches(regex);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        Button signIn = findViewById(R.id.signUp_screen_login_button);
        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            SignUpActivity.this.startActivity(intent);
            finish();
        });


        //  Home page for customer
        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(v -> {
            // Home Page
            Intent intent = new Intent(SignUpActivity.this, SignInAsCustomerActivity.class);
            SignUpActivity.this.startActivity(intent);
            finish();
        });





        TextInputLayout passwordLayout = findViewById(R.id.password_text_input);
        EditText password = passwordLayout.getEditText();
        passwordLayout.setErrorIconDrawable(null);
        TextInputLayout emailLayout = findViewById(R.id.email_text_input);
        EditText email = emailLayout.getEditText();
        TextInputLayout firstNameLayout = findViewById(R.id.fName_text_input);
        EditText firstName = firstNameLayout.getEditText();
        TextInputLayout lastNameLayout = findViewById(R.id.lName_text_input);
        EditText lastName = lastNameLayout.getEditText();
        TextInputLayout confirmPasswordLayout = findViewById(R.id.cPassword_text_input);
        confirmPasswordLayout.setErrorIconDrawable(null);
        EditText confirmPassword = confirmPasswordLayout.getEditText();

        //check if the password is correctly formatted
        assert password != null;
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidPassword(password.getText().toString())) {
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
                if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (firstName.getText().toString().length() < 3) {
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
        assert lastName != null;
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
                if (lastName.getText().toString().length() < 3) {
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
        final AutoCompleteTextView countryAuto = findViewById(R.id.customerTextView);

        // create list of customer
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(SignUpActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.countries_array));
        countryAuto.setAdapter(countriesAdapter);
        countryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }
        });
        // select the first item from the list
        countryAuto.setText(countriesAdapter.getItem(0), false);
        countryAuto.setKeyListener(null);
        countryAuto.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
//        Spinner genderSpinner = findViewById(R.id.spinnerGender);
//        countrySpinner = findViewById(R.id.spinnerCountry);
//        citySpinner = findViewById(R.id.spinnerCity);
//        zipCode = findViewById(R.id.textView_ZipCode);
//        TextView login = findViewById(R.id.textView_login);
//        Button SignUp = findViewById(R.id.button_SignUp);
//
//        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
//        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        genderSpinner.setAdapter(adapterGender);
//
//
//        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
//        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        countrySpinner.setAdapter(adapterCountry);
//
//
//        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (position > 0) {
//                    String selectedCountry = parentView.getItemAtPosition(position).toString();
//                    updateZipCode(selectedCountry);
//                    updateCitySpinner(selectedCountry);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//        });
//
//        login.setOnClickListener(view -> {
//            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
//            SignUpActivity.this.startActivity(intent);
//            finish();
//        });
//        SignUp.setOnClickListener(view -> {
//            int selectedGenderPosition = genderSpinner.getSelectedItemPosition();
//            int selectedCountryPosition = countrySpinner.getSelectedItemPosition();
//            int selectedCityPostion = citySpinner.getSelectedItemPosition();
//            String firstPassword = password.getText().toString();
//            String secondPassword = confirmPassword.getText().toString();
//            if (firstName.getText().toString().length() <= 2) {
//                showToast("first name should be more then 2 letters");
//            } else if (lastName.getText().toString().length() <= 2) {
//                showToast("last name should be more then 2 letters");
//            } else if (selectedGenderPosition == 0) {
//                showToast("Please select a gender");
//            } else if (!isValidEmail(email.getText().toString())) {
//                showToast("Please enter a valid email");
//            } else if (!isValidPassword(firstPassword)) {
//            } else if (!firstPassword.equals(secondPassword)) {
//                showToast("Passwords did not match");
//            } else if (selectedCountryPosition == 0) {
//                showToast("Please select a Country");
//            } else if (selectedCityPostion == 0) {
//                showToast("Please select a City");
//            } else if (phoneNumber.getText().toString().length() <= 2) {
//                showToast("Please enter the phone number");
//            }
//        });
//    }
//
//    private boolean isValidPassword(String password) {
//        if (password.length() < 5) {
//            showToast("password length should be >= 5");
//            return false;
//        }
//        boolean hasLetter = false;
//        boolean hasNumber = false;
//        boolean hasSpecialCharacter = false;
//        for (char ch : password.toCharArray()) {
//            if (Character.isLetter(ch)) {
//                hasLetter = true;
//            } else if (Character.isDigit(ch)) {
//                hasNumber = true;
//            } else if (isSpecialCharacter(ch)) {
//                hasSpecialCharacter = true;
//            }
//        }
//        if (!hasLetter) {
//            showToast("password  should have at least 1 letter");
//        } else if (!hasNumber) {
//            showToast("password  should have at least 1 number");
//        } else if (!hasSpecialCharacter) {
//            showToast("password  should have at least 1 SpecialCharacter [@$!%*?&]");
//        }
//        return hasLetter && hasNumber && hasSpecialCharacter;
//    }
//
//    private boolean isSpecialCharacter(char ch) {
//        String specialCharacters = "@$!%*?&";
//        return specialCharacters.indexOf(ch) != -1;
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void updateCitySpinner(String selectedCountry) {
//        int cityArrayResourceId = getResourceIdForCountry(selectedCountry);
//        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, cityArrayResourceId, android.R.layout.simple_spinner_item);
//        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        citySpinner.setAdapter(cityAdapter);
//    }
//
//    private int getResourceIdForCountry(String selectedCountry) {
//        switch (selectedCountry) {
//            case "Palestine":
//                return R.array.cities_Palestine;
//            case "Jordan":
//                return R.array.cities_Jordan;
//            case "Germany":
//                return R.array.cities_Germany;
//            case "Spain":
//                return R.array.cities_Spain;
//            case "Japan":
//                return R.array.cities_Japan;
//            default:
//                return R.array.cities_array;
//        }
//    }
//
//    private void updateZipCode(String selectedCountry) {
//        switch (selectedCountry) {
//            case "Palestine":
//                zipCode.setText("+970");
//                break;
//            case "Jordan":
//                zipCode.setText("+962");
//                break;
//            case "Germany":
//                zipCode.setText("+49");
//                break;
//            case "Spain":
//                zipCode.setText("+34");
//                break;
//            case "Japan":
//                zipCode.setText("+81");
//                break;
//            default:
//                zipCode.setText("---");
//        }
//    }
//
//    private boolean isValidEmail(String email) { // real email ? !!!
//        String domain = "";
//        boolean isValidPattern = Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        if (isValidPattern) {
//            domain = getDomainFromEmail(email);
//        }
//        boolean validDomain = domain.length() >= 4 && (domain.endsWith(".com") || domain.endsWith(".edu"));
//        return isValidPattern && validDomain;
//    }
    }
}