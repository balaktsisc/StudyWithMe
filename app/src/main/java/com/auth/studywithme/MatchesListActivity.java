package com.auth.studywithme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchesListActivity extends AppCompatActivity implements RecyclerAdapter.ISStudyRequestRecycler {
    public static double SIMILARITY_THRESHOLD = 0.6;
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    StudyRequest sr;
    StorageHandler sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        sh = new StorageHandler(this,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            sr = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));
        else
            finish();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this,sr,this,sh);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showStudyRequestDetails(long srId) {
        // Start the UpdateStudyRequestActivity and pass the selected study request
        Intent intent = new Intent(this, ViewStudyRequestActivity.class);
        intent.putExtra("studyRequestId", srId);
        this.startActivity(intent);
    }

}
