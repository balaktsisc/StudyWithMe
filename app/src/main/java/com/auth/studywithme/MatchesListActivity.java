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
    RecyclerView recyclerView;
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder> adapter;
    StudyRequest sr;

//    static int CREATED_REQUEST = 100;
//    static int DELETED_ACCOUNT = 101;
//    static int UPDATED_REQUEST = 102;
//    static int DELETED_REQUEST = 103;


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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_appbar, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_add_request) {
//            Intent intent = new Intent(this, StudyRequestActivity.class);
//            intent.putExtra("loggedUser", sr);
//            activityResultLauncher.launch(intent);
//
//            item.setChecked(!item.isChecked());
//            return true;
//        } else if (item.getItemId() == R.id.menu_account) {
//            Intent intent = new Intent(this, AccountActivity.class);
//            intent.putExtra("loggedUser", sr);
//            activityResultLauncher.launch(intent);
//
//            item.setChecked(!item.isChecked());
//            return true;
//        }
//        return false;
//    }

    @Override
    public void showStudyRequestDetails(StudyRequest sr) {
        // Start the UpdateStudyRequestActivity and pass the selected study request
        Intent intent = new Intent(this, UpdateStudyRequestActivity.class);
        intent.putExtra("studyRequest", sr);
        this.startActivity(intent);
    }

//    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == DELETED_ACCOUNT) {
//                    startActivity(new Intent(this,LoginActivity.class));
//                    finish();
//                } else if (result.getResultCode() == CREATED_REQUEST) {
//                    recreate();
//                } else if(result.getResultCode() == UPDATED_REQUEST) {
//                    recreate();
//                } else if(result.getResultCode() == DELETED_REQUEST){
//                    recreate();
//                }
//            });
}
