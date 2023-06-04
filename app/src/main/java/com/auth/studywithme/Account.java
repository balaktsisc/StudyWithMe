package com.auth.studywithme;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Account extends AppCompatActivity {
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText emailEditText;
    Spinner universitySpinner;
    EditText departmentEditText;
    StorageHandler storageHandler;
    Button signUpButton;
    StorageHandler sh;
    ArrayAdapter<String> adapter;
    static int NUM_FLAGS = 3;
    boolean[] flags = new boolean[NUM_FLAGS];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sh = new StorageHandler(this,null,1);

        for(int i = 0; i < 3; i++) flags[i] = false;

        // Get data from SignUp form
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        universitySpinner = findViewById(R.id.uniSpinner);
        departmentEditText = findViewById(R.id.departmentEditText);
        signUpButton = findViewById(R.id.storeButton);
        signUpButton.setEnabled(false);

        storageHandler = new StorageHandler(this,null,1);

        // Check if inserted account details are suitable
        usernameEditText.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable runnable;
            @Override
            public void afterTextChanged(Editable s) {
                if (usernameEditText.hasFocus()) {
                    if (s.length() >= 2 && storageHandler.fetchUserByUsername(s.toString()) != null) {
                        runnable = () -> showToast("Username already exists!");
                        handler.postDelayed(runnable, 800);
                        flags[0] = false;
                    } else if (usernameEditText.length() >= 4) {
                        runnable = () -> showToast("Username available!");
                        handler.postDelayed(runnable, 800);
                        flags[0] = true;
                    } else {
                        runnable = () -> showToast("Username should be longer than 3 characters!");
                        handler.postDelayed(runnable, 800);
                        flags[0] = false;
                    }
                    TryActivateStoreButton();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (runnable != null) handler.removeCallbacks(runnable);
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            private final Handler handler = new Handler();
            private Runnable runnable;
            @Override
            public void afterTextChanged(Editable s) {
                if(passwordEditText.hasFocus()) {
                    if (isValid(s.toString())) {
                        runnable = () -> showToast("Password looks nice!");
                        handler.postDelayed(runnable, 800);
                        flags[1] = true;
                    } else {
                        runnable = () -> showToast("Password must contain digits & letters between 5 to 20.");
                        handler.postDelayed(runnable, 800);
                        flags[1] = false;
                    }
                    TryActivateStoreButton();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (runnable != null) handler.removeCallbacks(runnable);
            }
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

        University[] unis = University.values();
        String[] unisNames = new String[unis.length];
        for (int i = 0; i < unis.length; i++)
            unisNames[i] = University.getUniversityName(this,unis[i]);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,unisNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(adapter);

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Stores or updates a user account in db
    public abstract void storeAccount(View view);

    // Deletes an existing user account from db
    public void deleteAccount(View view){
        String username = usernameEditText.getText().toString();
        if (storageHandler.deleteUser(username)) {
            Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
            setResult(101);
            finish();
        } else {
            Toast.makeText(this, "Account failed to be deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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