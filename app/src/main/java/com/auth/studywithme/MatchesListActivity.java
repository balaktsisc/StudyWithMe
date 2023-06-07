package com.auth.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * The MatchesListActivity displays a list of study request matches for a particular study request.
 * It allows the user to view the details of each match and navigate back to the previous activity.
 */
public class MatchesListActivity extends AppCompatActivity implements RecyclerAdapter.ISStudyRequestRecycler {
    public static double SIMILARITY_THRESHOLD = 0.6;        // The threshold over which two requests are considered similar enough to match
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    StudyRequest sr;
    StorageHandler sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        // Set the default night mode to NO night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Enable the back button in the action bar for navigating back
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize the StorageHandler
        sh = new StorageHandler(this, null, 1);

        // Retrieve the study request ID passed from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            sr = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));
        else
            finish();

        // Set up the RecyclerView to display the list of matches
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, sr, this, sh);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the selection of menu items in the action bar
        if (item.getItemId() == android.R.id.home) {
            // When the back button is pressed, navigate back to the previous activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showStudyRequestDetails(long srId) {
        // Start the ViewStudyRequestActivity and pass the selected study request ID and the ID of the requested user
        Intent intent = new Intent(this, ViewStudyRequestActivity.class);
        intent.putExtra("studyRequestId", srId);
        intent.putExtra("loggedUserId", sr.getRequestedUserId());
        this.startActivity(intent);
    }

}
