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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            sr = (StudyRequest) extras.getSerializable("studyRequest");
        else
            finish();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this, sr,this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showStudyRequestDetails(StudyRequest sr) {
        // Start the UpdateStudyRequestActivity and pass the selected study request
        Intent intent = new Intent(this, ViewStudyRequestActivity.class);
        intent.putExtra("studyRequest", sr);
        this.startActivity(intent);
    }

}
