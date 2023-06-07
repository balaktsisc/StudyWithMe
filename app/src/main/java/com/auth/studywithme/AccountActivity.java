package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for managing user account information and actions.
 * This class extends the abstract class Account, which provides common functionality.
 */
public class AccountActivity extends Account {
    TextView title;
    Button storeButton;
    Button deleteButton;
    User loggedUser;
    Boolean authorized;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authorized = true;

        storeButton = findViewById(R.id.storeButton);
        storeButton.setText("Update");

        title = findViewById(R.id.title);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Retrieve the logged-in user from the database
            loggedUser = super.sh.fetchUserById(extras.getLong("loggedUserId"));

            // If the activity isn't launched by Dashboard Activity, that means
            // that the user requests to see the user details of another user,
            // so the user corresponding to the pre-filled fields is not authorized
            // to view all the details, neither edit any of them.
            if(loggedUser == null) {
                loggedUser = super.sh.fetchUserById(extras.getLong("userId"));
                authorized = false;
            }

            // Set the EditText fields with user's information
            firstNameEditText.setText(loggedUser.getFirstName());
            lastNameEditText.setText(loggedUser.getLastName());
            usernameEditText.setText(loggedUser.getUsername());
            passwordEditText.setText(loggedUser.getPassword());
            emailEditText.setText(loggedUser.getEmail());
            universitySpinner.setSelection(adapter.getPosition(University.getUniversityName(this,loggedUser.getUniversity())));
            departmentEditText.setText((loggedUser.getDepartment()));

            if(authorized) {
                // The logged-in user is authorized to edit the account
                title.setText("Account");
                for (int i = 0; i < NUM_FLAGS; i++) super.flags[i] = true;
                TryActivateStoreButton();
            } else {
                // Displaying details of a study partner, not authorized to edit
                title.setText("Study partner details");
                firstNameEditText.setText(loggedUser.getName());
                firstNameEditText.setInputType(InputType.TYPE_NULL);
                lastNameEditText.setVisibility(View.GONE);
                usernameEditText.setVisibility(View.GONE);
                passwordEditText.setVisibility(View.GONE);
                emailEditText.setInputType(InputType.TYPE_NULL);
                universitySpinner.setEnabled(false);
                departmentEditText.setInputType(InputType.TYPE_NULL);
                deleteButton.setVisibility(View.INVISIBLE);
                storeButton.setVisibility(View.INVISIBLE);
            }
        } else {
            // No user information provided, finish the activity
            finish();
        }
    }

    @Override
    public void storeAccount(View view) {
        if (authorized) {
            // Update the account with the modified user information
            User u = new User();
            u.setFirstName(firstNameEditText.getText().toString());
            u.setLastName(lastNameEditText.getText().toString());
            u.setUsername(usernameEditText.getText().toString());
            u.setPassword(passwordEditText.getText().toString());
            u.setEmail(emailEditText.getText().toString());
            u.setUniversity(University.getUniversity(this,universitySpinner.getSelectedItem().toString()));
            u.setDepartment(departmentEditText.getText().toString());

            TryActivateStoreButton();

            try (StorageHandler storageHandler = new StorageHandler(this, null, 1)) {
                if (storageHandler.updateUser(loggedUser.getId(), u)) {
                    Toast.makeText(this, "Account updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Account failed to be updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}