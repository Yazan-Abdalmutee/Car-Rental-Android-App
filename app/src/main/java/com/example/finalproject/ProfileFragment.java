package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    CircleImageView profile_image,swap_profile_image;


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
        profile_image=view.findViewById(R.id.profile_image_header);
        swap_profile_image=view.findViewById(R.id.profile_image_swap);
        swap_profile_image.setVisibility(View.GONE);

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
        name.setText(sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName());
        email.setText(sharedPreferencesManager.getEmail());
        assert getActivity() != null;
        TextInputLayout phoneLayout = view.findViewById(R.id.phone_text_input);
        EditText phone = phoneLayout.getEditText();
        Button cancel = view.findViewById(R.id.cancel_button);
        switch (sharedPreferencesManager.getCountry()) {
            case "PS":
            case "default":
                phoneLayout.setPrefixText("+970 ");
                break;
            case "JO":
                phoneLayout.setPrefixText("+962 ");
                break;
            case "DE":
                phoneLayout.setPrefixText("+49 ");
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
            swap_profile_image.setVisibility(View.VISIBLE);
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
            swap_profile_image.setVisibility(View.GONE);
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
            // if all empty, do nothing
            if (firstName.getText().toString().isEmpty() && lastName.getText().toString().isEmpty() && phone.getText().toString().isEmpty() && password.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "No changes made", Toast.LENGTH_SHORT).show();
                return;
            }
            // if one of the password fields is empty, do nothing
            if (password.getText().toString().isEmpty() ^ confirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please fill both password fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (firstName.getText().toString().isEmpty())
                firstName.setText(sharedPreferencesManager.getFirstName());
            if (lastName.getText().toString().isEmpty())
                lastName.setText(sharedPreferencesManager.getLastName());
            if (phone.getText().toString().isEmpty())
                phone.setText(sharedPreferencesManager.getPhone());
            if (password.getText().toString().isEmpty())
                password.setText(sharedPreferencesManager.getPasswordHashed());
            if (confirmPassword.getText().toString().isEmpty())
                confirmPassword.setText(sharedPreferencesManager.getPasswordHashed());


            DatabaseManager databaseManager = MyApplication.getDatabaseManager();




            passwordLayout.setErrorIconDrawable(null);
            confirmPasswordLayout.setErrorIconDrawable(null);
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



        swap_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateProfileImage(profile_image);

    }
    private void updateProfileImage(CircleImageView profile_image) {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
            String imageString= sharedPreferencesManager.getImage();
        if (imageString != null && !imageString.isEmpty()) {
            byte[] imageByteArray = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            Glide.with(this)
                    .load(bitmap)
                    .into(profile_image);
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                byte[] imageByteArray = convertBitmapToByteArray(bitmap);
                DatabaseManager db = MyApplication.getDatabaseManager();
                SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());
                db.updateCustomerImage(sharedPreferencesManager.getEmail(),imageByteArray);
                sharedPreferencesManager.setUserImage(imageByteArray);
                Activity parentActivity = getActivity();
                assert parentActivity != null;
                NavigationView navigationView = parentActivity.findViewById(R.id.navigation);
                CircleImageView profile_header = navigationView.getHeaderView(0).findViewById(R.id.profile_image_header);
                updateProfileImage(profile_header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}