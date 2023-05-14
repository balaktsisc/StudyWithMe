package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

public class StudyRequestActivity extends AppCompatActivity {

    private User loggedUser;
    StorageHandler storageHandler;

    private EditText subjectEditText;
    private EditText reasonEditText;
    private EditText placeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_request);

        // Initialize StorageHandler
        storageHandler = new StorageHandler(this,null,1);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
//            Get current user
            loggedUser = (User) extras.getSerializable("loggedUser");
        }

        subjectEditText = findViewById(R.id.et_subject);
        reasonEditText = findViewById(R.id.et_reason);
        placeEditText = findViewById(R.id.et_place);

    }

    public void onBtnClick(View view) {
        String subject = subjectEditText.getText().toString();
        String reason = reasonEditText.getText().toString();
        String place = placeEditText.getText().toString();

        StudyRequest sr = new StudyRequest(subject, reason, place,"o", new Date(),PeriodOfStudy.once,4);
        storageHandler.addStudyRequest(sr,loggedUser);

        setResult(100);
        finish();
    }
}