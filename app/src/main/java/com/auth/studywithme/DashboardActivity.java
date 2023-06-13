package com.auth.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Activity for displaying the user's study requests and providing options to create, update, or delete requests.
 * Also allows the user to navigate to other activities such as account settings and about page.
 */
public class DashboardActivity extends AppCompatActivity implements RecyclerAdapter.ISStudyRequestRecycler {
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    User loggedUser;
    StorageHandler sh;

    static int CREATED_REQUEST = 100;
    static int DELETED_ACCOUNT = 101;
    static int UPDATED_REQUEST = 102;
    static int DELETED_REQUEST = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Initialize the StorageHandler
        sh = new StorageHandler(this,null,1);

        // Retrieve the logged-in user from the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            loggedUser = sh.fetchUserById(extras.getLong("loggedUserId"));
        else
            finish();

        TextView emptyDashboardTV = findViewById(R.id.emptyDashboardTV);
        if (sh.fetchStudyRequestsOfUser(loggedUser.getId()).size() > 0)
            emptyDashboardTV.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this,loggedUser,this,sh);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_request) {
            // Start the CreateStudyRequestActivity
            Intent intent = new Intent(this, CreateStudyRequestActivity.class);
            intent.putExtra("loggedUserId",loggedUser.getId());
            activityResultLauncher.launch(intent);

            item.setChecked(!item.isChecked());
            return true;
        } else if (item.getItemId() == R.id.menu_account) {
            // Start the AccountActivity
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("loggedUserId",loggedUser.getId());
            activityResultLauncher.launch(intent);

            item.setChecked(!item.isChecked());
            return true;
        } else if (item.getItemId() == R.id.menu_about) {
            // Start the AboutActivity
            Intent intent = new Intent(this, AboutActivity.class);
            activityResultLauncher.launch(intent);

            item.setChecked(!item.isChecked());
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            // Log out the user and start the LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            item.setChecked(!item.isChecked());
            return true;
        }
        return false;
    }

    @Override
    public void showStudyRequestDetails(long srId) {
        Intent intent;

        if(sh.isStudyRequestMatched(srId))
            intent = new Intent(this, MatchesListActivity.class);
        else
            intent = new Intent(this, UpdateStudyRequestActivity.class);

        intent.putExtra("studyRequestId", srId);
        activityResultLauncher.launch(intent);
    }

    // Handle activity results
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == DELETED_ACCOUNT) {
                    // If the account was deleted, start LoginActivity and finish this activity
                    finish();
                } else if (result.getResultCode() == CREATED_REQUEST || result.getResultCode() == UPDATED_REQUEST ||
                        result.getResultCode() == DELETED_REQUEST) {
                    // If a request was created, updated, or deleted, recreate the activity
                    recreate();
                }
            });
}
