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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_request);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = (StudyRequest) extras.getSerializable("studyRequest");

            // Initialize views
            EditText subjectEditText = findViewById(R.id.et_subject);
            EditText reasonEditText = findViewById(R.id.et_reason);
            EditText placeEditText = findViewById(R.id.et_place);
            EditText commentsEditText = findViewById(R.id.et_comments);
            Spinner periodSpinner = findViewById(R.id.spinner_period_of_study);


            // Set up period spinner
            ArrayAdapter<PeriodOfStudy> periodAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, PeriodOfStudy.values());
            periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            periodSpinner.setAdapter(periodAdapter);

            // Pre-fill the form fields with the current values of the study request
            subjectEditText.setText(studyRequest.getSubject());
            reasonEditText.setText(studyRequest.getReason());
            placeEditText.setText(studyRequest.getPlace());
            commentsEditText.setText(studyRequest.getComments());
            periodSpinner.setSelection(periodAdapter.getPosition(studyRequest.getPeriod()));

            subjectEditText.setEnabled(false);
            reasonEditText.setEnabled(false);
            placeEditText.setEnabled(false);
            commentsEditText.setEnabled(false);
            periodSpinner.setEnabled(false);

        } else {
            finish();
        }
    }

    public void viewSROwner(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("user",studyRequest.getRequestedUser());
        startActivity(intent);
    }
}