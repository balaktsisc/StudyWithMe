package com.auth.studywithme;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText emailEditText;
    EditText universityEditText;
    EditText departmentEditText;

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

        // Set text in textView for testing purposes.
       // TextView titleTextView = findViewById(R.id.);
       // titleTextView.setText("Password: "  + password + " " + department );

        // Create a new user object with the form input data
        User user = new User(firstName,lastName,username,password,email,university,department);

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