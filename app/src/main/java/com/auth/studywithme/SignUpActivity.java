package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

/**
 * The SignUpActivity class handles the sign-up process for creating a new user account.
 */
public class SignUpActivity extends Account {
    TextView title;
    Button storeButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).hide();

        storeButton = findViewById(R.id.storeButton);
        storeButton.setText("Create");
        title = findViewById(R.id.title);
        title.setText("Sign up");
    }

    /**
     * Stores the new user account in the database.
     *
     * @param view the view that triggers the method
     */
    @Override
    public void storeAccount(View view){
        // Get text form inputs
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        University university = University.getUniversity(this,universitySpinner.getSelectedItem().toString());
        String department = departmentEditText.getText().toString();

        // Create a new user object with the form input data
        User user = new User(username,password,email,firstName,lastName,university,department);
        TryActivateStoreButton();

        try (StorageHandler storageHandler = new StorageHandler(this, null, 1)) {
            // Add user to database
            boolean success = storageHandler.addUser(user);

            // Show toast message based on success of adding user
            if (success) {
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(this, "Account failed to be created", Toast.LENGTH_SHORT).show();
            }
        }
    }
}