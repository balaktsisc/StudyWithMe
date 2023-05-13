package com.auth.studywithme;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Account extends AppCompatActivity {
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText emailEditText;
    EditText universityEditText;
    EditText departmentEditText;
    StorageHandler storageHandler;
    Button signUpButton;
    static int NUM_FLAGS = 3;
    boolean[] flags = new boolean[NUM_FLAGS];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        for(int i = 0; i < 3; i++) flags[i] = false;

        // Get data from SignUp form
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        universityEditText = findViewById(R.id.universityEditText);
        departmentEditText = findViewById(R.id.departmentEditText);
        signUpButton = findViewById(R.id.storeButton);
        signUpButton.setEnabled(false);

        storageHandler = new StorageHandler(this,null,1);

        // Check if inserted account details are suitable
        try (StorageHandler storageHandler = new StorageHandler(this, null, 1)) {
            usernameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (usernameEditText.hasFocus()) {
                        if (s.length() >= 2 && storageHandler.fetchUserByUsername(s.toString()) != null) {
                            Toast.makeText(Account.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                            flags[0] = false;
                        } else if (usernameEditText.length() >= 4) {
                            Toast.makeText(Account.this, "Username available!", Toast.LENGTH_SHORT).show();
                            flags[0] = true;
                        } else {
                            Toast.makeText(Account.this, "Username should be longer than 3 characters!", Toast.LENGTH_SHORT).show();
                            flags[0] = false;
                        }
                        TryActivateStoreButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });

            passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if(passwordEditText.hasFocus()) {
                        if (isValid(s.toString())) {
                            Toast.makeText(Account.this, "Password looks nice!", Toast.LENGTH_SHORT).show();
                            flags[1] = true;
                        } else {
                            Toast.makeText(Account.this, "Password must contain digits & letters between 5 to 20.", Toast.LENGTH_SHORT).show();
                            flags[1] = false;
                        }
                        TryActivateStoreButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { signUpButton.setActivated(false); }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });

            emailEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (emailEditText.hasFocus()) {
                        flags[2] = s.toString().contains("@") && s.toString().contains(".");
                        TryActivateStoreButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
        }
    }

    // Stores or updates a user account in db
    public abstract void storeAccount(View view);

    // Deletes an existing user account from db
    public void deleteAccount(View view){
        String username = usernameEditText.getText().toString();

        try (StorageHandler storageHandler = new StorageHandler(this, null, 1)) {
            if (storageHandler.deleteUser(username)) {
                Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                setResult(101);
                finish();
            } else {
                Toast.makeText(this, "Account failed to be deleted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{4,20}$";
    static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    void TryActivateStoreButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.storeButton).setEnabled(f);
    }

}