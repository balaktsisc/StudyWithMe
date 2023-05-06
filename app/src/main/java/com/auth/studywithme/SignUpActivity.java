package com.auth.studywithme;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText emailEditText;
    EditText universityEditText;
    EditText departmentEditText;
    static boolean[] flags = new boolean[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get data from SignUp form
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        universityEditText = findViewById(R.id.universityEditText);
        departmentEditText = findViewById(R.id.departmentEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setEnabled(false);

        // Check if inserted account details are suitable
        try (StorageHandler storageHandler = new StorageHandler(this, null, 1)) {
            usernameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (usernameEditText.hasFocus()) {
                        if (s.length() >= 2 && storageHandler.fetchUserByCredentials(s.toString()) != null) {
                            Toast.makeText(SignUpActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                            flags[0] = false;
                        } else if (usernameEditText.length() >= 4) {
                            Toast.makeText(SignUpActivity.this, "Username available!", Toast.LENGTH_SHORT).show();
                            flags[0] = true;
                        } else {
                            Toast.makeText(SignUpActivity.this, "Username should be longer than 3 characters!", Toast.LENGTH_SHORT).show();
                            flags[0] = false;
                        }
                        TryActivateSigningButton();
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
                            Toast.makeText(SignUpActivity.this, "Password looks nice!", Toast.LENGTH_SHORT).show();
                            flags[1] = true;
                        } else {
                            Toast.makeText(SignUpActivity.this, "Password must contain digits & letters between 5 to 20.", Toast.LENGTH_SHORT).show();
                            flags[1] = false;
                        }
                        TryActivateSigningButton();
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
                        TryActivateSigningButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
        }
    }
    public void onBtnClick(View view){
        // Get text form inputs
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String university = universityEditText.getText().toString();
        String department = departmentEditText.getText().toString();

        // Create a new user object with the form input data
        User user = new User(firstName,lastName,username,password,email,university,department);
        TryActivateSigningButton();

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

    static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{4,20}$";
    static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    static boolean isValid(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    void TryActivateSigningButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.signUpButton).setEnabled(f);
    }

}