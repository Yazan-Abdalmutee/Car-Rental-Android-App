package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationPage extends AppCompatActivity {
    TextView zipCode;
    private Spinner countrySpinner, citySpinner;

    private static String getDomainFromEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            return parts[1];
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        EditText firstName = findViewById(R.id.editText_FirstName);
        EditText lastName = findViewById(R.id.editText_LastName);
        EditText email = findViewById(R.id.editText_Email);
        EditText password = findViewById(R.id.editText_Password);
        EditText confirmPassword = findViewById(R.id.editText_ConfirmPassword);
        EditText phoneNumber = findViewById(R.id.editText_PhoneNumber);

        Spinner genderSpinner = findViewById(R.id.spinnerGender);
        countrySpinner = findViewById(R.id.spinnerCountry);
        citySpinner = findViewById(R.id.spinnerCity);
        zipCode = findViewById(R.id.textView_ZipCode);
        TextView login = findViewById(R.id.textView_login);
        Button SignUp = findViewById(R.id.button_SignUp);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapterGender);


        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapterCountry);


        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    String selectedCountry = parentView.getItemAtPosition(position).toString();
                    updateZipCode(selectedCountry);
                    updateCitySpinner(selectedCountry);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationPage.this, LoginPage.class);
                RegistrationPage.this.startActivity(intent);
                finish();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderPostion = genderSpinner.getSelectedItemPosition();
                int selectedCountryPostion = countrySpinner.getSelectedItemPosition();
                int selectedCityPostion = citySpinner.getSelectedItemPosition();
                String firstPassword = password.getText().toString();
                String secondPassword = confirmPassword.getText().toString();
                if (firstName.getText().toString().length() <= 2) {
                    showToast("first name should be more then 2 letters");
                } else if (lastName.getText().toString().length() <= 2) {
                    showToast("last name should be more then 2 letters");
                } else if (selectedGenderPostion == 0) {
                    showToast("Please select a gender");
                } else if (!isValidEmail(email.getText().toString())) {
                    showToast("Please enter a valid email");
                } else if (!isValidPassword(firstPassword)) {
                } else if (!firstPassword.equals(secondPassword)) {
                    showToast("Passwords did not match");
                } else if (selectedCountryPostion == 0) {
                    showToast("Please select a Country");
                } else if (selectedCityPostion == 0) {
                    showToast("Please select a City");
                } else if (phoneNumber.getText().toString().length() <= 2) {
                    showToast("Please enter the phone number");
                }
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 5) {
            showToast("password length should be >= 5");
            return false;
        }
        boolean hasLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;
        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if (isSpecialCharacter(ch)) {
                hasSpecialCharacter = true;
            }
        }
        if (!hasLetter) {
            showToast("password  should have at least 1 letter");
        } else if (!hasNumber) {
            showToast("password  should have at least 1 number");
        } else if (!hasSpecialCharacter) {
            showToast("password  should have at least 1 SpecialCharacter [@$!%*?&]");
        }
        return hasLetter && hasNumber && hasSpecialCharacter;
    }

    private boolean isSpecialCharacter(char ch) {
        String specialCharacters = "@$!%*?&";
        return specialCharacters.indexOf(ch) != -1;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateCitySpinner(String selectedCountry) {
        int cityArrayResourceId = getResourceIdForCountry(selectedCountry);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, cityArrayResourceId, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
    }

    private int getResourceIdForCountry(String selectedCountry) {
        switch (selectedCountry) {
            case "Palestine":
                return R.array.cities_Palestine;
            case "Jordan":
                return R.array.cities_Jordan;
            case "Germany":
                return R.array.cities_Germany;
            case "Spain":
                return R.array.cities_Spain;
            case "Japan":
                return R.array.cities_Japan;
            default:
                return R.array.cities_array;
        }
    }

    private void updateZipCode(String selectedCountry) {
        switch (selectedCountry) {
            case "Palestine":
                zipCode.setText("+970");
                break;
            case "Jordan":
                zipCode.setText("+962");
                break;
            case "Germany":
                zipCode.setText("+49");
                break;
            case "Spain":
                zipCode.setText("+34");
                break;
            case "Japan":
                zipCode.setText("+81");
                break;
            default:
                zipCode.setText("---");
        }
    }

    private boolean isValidEmail(String email) { // real email ? !!!
        String domain = "";
        boolean isValidPattern = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (isValidPattern) {
            domain = getDomainFromEmail(email);
        }
        boolean validDomain = domain.length() >= 4 && (domain.endsWith(".com") || domain.endsWith(".edu"));
        return isValidPattern && validDomain;
    }
}