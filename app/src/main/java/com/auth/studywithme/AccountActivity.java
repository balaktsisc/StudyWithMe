package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            loggedUser = super.sh.fetchUserById(extras.getLong("loggedUserId"));

            if(loggedUser == null) {
                loggedUser = super.sh.fetchUserById(extras.getLong("userId"));
                authorized = false;
            }

            super.firstNameEditText.setText(loggedUser.getFirstName());
            super.lastNameEditText.setText(loggedUser.getLastName());
            super.usernameEditText.setText(loggedUser.getUsername());
            super.passwordEditText.setText(loggedUser.getPassword());
            super.emailEditText.setText(loggedUser.getEmail());
            super.universityEditText.setText(loggedUser.getUniversity());
            super.departmentEditText.setText((loggedUser.getDepartment()));

            if(authorized) {
                title.setText("Account");
                for (int i = 0; i < NUM_FLAGS; i++) super.flags[i] = true;
                TryActivateStoreButton();
            } else {
                title.setText("Study partner details");
                firstNameEditText.setText(loggedUser.getName());
                firstNameEditText.setEnabled(false);
                lastNameEditText.setVisibility(View.GONE);
                usernameEditText.setVisibility(View.GONE);
                passwordEditText.setVisibility(View.GONE);
                emailEditText.setEnabled(false);
                universityEditText.setEnabled(false);
                departmentEditText.setEnabled(false);
                deleteButton.setVisibility(View.INVISIBLE);
                storeButton.setVisibility(View.INVISIBLE);
            }
        } else {
            finish();
        }
    }

    @Override
    public void storeAccount(View view) {
        if (authorized) {
            User u = new User();
            u.setFirstName(firstNameEditText.getText().toString());
            u.setLastName(lastNameEditText.getText().toString());
            u.setUsername(usernameEditText.getText().toString());
            u.setPassword(passwordEditText.getText().toString());
            u.setEmail(emailEditText.getText().toString());
            u.setUniversity(universityEditText.getText().toString());
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