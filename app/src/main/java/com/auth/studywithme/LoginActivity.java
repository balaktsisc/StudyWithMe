package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

/**
 * Activity for user login. Allows users to enter their username and password to authenticate and log in.
 * Provides an option to sign up for a new account.
 */
public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    StorageHandler storageHandler;
    User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the app's default night mode to NO night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Hide the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Get references to the username and password EditText views
        usernameEditText = findViewById(R.id.editTxtUsername);
        passwordEditText = findViewById(R.id.editTxtPassword);

        // Initialize the StorageHandler
        storageHandler = new StorageHandler(this,null,1);

        // Set up the "Not a member? Sign up now" text view with clickable span
        TextView textView = findViewById(R.id.textViewSignUp);
        SpannableString spannableString = new SpannableString("Not a member? Sign up now");
        textView.setText(spannableString);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // Start the SignUpActivity when the user clicks the span
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 14, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Called when the "Login" button is clicked. Attempts to authenticate the user with the entered credentials.
     *
     * @param v The view that was clicked (the "Login" button)
     */
    public void LoginClicked(View v) {
        // Get the username and password entered by the user
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if the username and password are correct
        loggedUser = authenticateUser(username, password);
        if (loggedUser != null) {
            // Show a success message
            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

            // Switch to the DashboardActivity for the logged-in user
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("loggedUserId",loggedUser.getId());
            startActivity(intent);
        } else {
            // Show an error message
            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        usernameEditText = findViewById(R.id.editTxtUsername);
        passwordEditText = findViewById(R.id.editTxtPassword);
        // Clear the password field when the activity resumes
        passwordEditText.setText("");
    }

    /**
     * Authenticate the user with the provided username and password.
     *
     * @param username The username entered by the user
     * @param password The password entered by the user
     * @return The authenticated User object, or null if authentication fails
     */
    private User authenticateUser(String username, String password){
        // Search the database for a user with the specified username and password
        return storageHandler.fetchUserByCredentials(username, password);
    }
}