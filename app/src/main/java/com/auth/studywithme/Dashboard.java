package com.auth.studywithme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    User loggedUser;

    static int CREATED_REQUEST = 100;
    static int DELETED_ACCOUNT = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            loggedUser = (User) extras.getSerializable("loggedUser");
        else
            loggedUser = new User();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this,loggedUser);
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
           // Intent intent = new Intent(this, REPLACE-WITH-NEW-REQUEST-ACTIVITY.class);
           // intent.putExtra("loggedUser",loggedUser);
           //  activityResultLauncher.launch(intent);

            item.setChecked(!item.isChecked());
            return true;
        } else if (item.getItemId() == R.id.menu_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("loggedUser",loggedUser);
            activityResultLauncher.launch(intent);

            item.setChecked(!item.isChecked());
            return true;
        }
        return false;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == DELETED_ACCOUNT) {
                    startActivity(new Intent(this,LoginActivity.class));
                    finish();
                } else if (result.getResultCode() == CREATED_REQUEST) {
                    recreate();
                }
            });

}
