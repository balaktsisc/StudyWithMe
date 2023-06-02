package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ViewStudyRequestActivity extends AppCompatActivity {

    StudyRequest studyRequest;
    StorageHandler sh;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_study_request);

        sh = new StorageHandler(this,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));

            // Initialize views
            EditText subjectEditText = findViewById(R.id.et_subject);
            EditText reasonEditText = findViewById(R.id.et_reason);
            EditText placeEditText = findViewById(R.id.et_place);
            EditText commentsEditText = findViewById(R.id.et_comments);
            Spinner periodSpinner = findViewById(R.id.sp_period);

            // Set up period spinner
            PeriodOfStudy[] periods = PeriodOfStudy.values();
            String[] periodValues = new String[periods.length];
            for (int i = 0; i < periods.length; i++)
                periodValues[i] = periods[i].getDisplayName();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,periodValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            periodSpinner.setAdapter(adapter);

            // Pre-fill the form fields with the current values of the study request
            subjectEditText.setText(studyRequest.getSubject());
            reasonEditText.setText(studyRequest.getReason());
            placeEditText.setText(studyRequest.getPlace());
            commentsEditText.setText(studyRequest.getComments());
            periodSpinner.setSelection(adapter.getPosition(studyRequest.getPeriod().getDisplayName()));

            if(sh.isStudyRequestMatched(studyRequest.getId())){
                subjectEditText.setEnabled(false);
                reasonEditText.setEnabled(false);
                placeEditText.setEnabled(false);
                commentsEditText.setEnabled(false);
                periodSpinner.setEnabled(false);
            }

        } else {
            finish();
        }
    }

    public void viewSROwner(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId",studyRequest.getRequestedUserId());
        startActivity(intent);
    }
}