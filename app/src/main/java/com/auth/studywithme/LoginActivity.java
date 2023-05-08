package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private StorageHandler storageHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get references to the username and password EditText views
        usernameEditText = findViewById(R.id.editTxtUsername);
        passwordEditText = findViewById(R.id.editTxtPassword);

        // Initialize StorageHandler
        storageHandler = new StorageHandler(this,null,1);

        TextView textView = findViewById(R.id.textViewSignUp);
        SpannableString spannableString = new SpannableString("Not a member? Sign up now");

        textView.setText(spannableString);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, 14, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        Button loginButton = findViewById(R.id.loginButton);

    }
        public void onBtnClick (View v){
            // Get the username and password entered by the user
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Check if the username and password are correct
            if (isValidCredentials(username, password)) {
                // Show a success message
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                // Switch to the new activity for creating a study request
                //Intent intent = new Intent(LoginActivity.this, CreateStudyRequestActivity.class);
                //startActivity(intent);
            } else {
                // Show an error message
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }

        // Check if the username and password are valid
        private boolean isValidCredentials (String username, String password){
            // Search database for user with specified username and password
            User user = storageHandler.fetchUserByCredentials(username, password);
            return user!=null;
        }




}