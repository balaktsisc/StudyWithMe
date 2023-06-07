package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import java.util.Objects;

/**
 * Represts the About Activity that displays user and technincal
 * info about the application. Its usage is considered static.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up the back button in the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection
        if (item.getItemId() == android.R.id.home) {
            // If the home button is pressed, go back to the previous activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}