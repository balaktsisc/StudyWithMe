package com.auth.studywithme;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class UserDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    User loggedUser;
    StorageHandler storageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            loggedUser = (User) extras.getSerializable("loggedUser");
        else
            loggedUser = new User();

        storageHandler = new StorageHandler(this,null,1);

        StudyRequest sr = new StudyRequest("Algorithms", "Test", "Central Library","", new Date(),PeriodOfStudy.once,4);
        storageHandler.addStudyRequest(sr,loggedUser);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this,loggedUser);
        recyclerView.setAdapter(adapter);
    }
}
