package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class SignUpActivity extends Account {
    TextView title;
    Button storeButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();

        storeButton = findViewById(R.id.storeButton);
        storeButton.setText("Create");
        title = findViewById(R.id.title);
        title.setText("Sign up");
    }

    @Override
    public void storeAccount(View view){
        // Get text form inputs
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String university = universityEditText.getText().toString();
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