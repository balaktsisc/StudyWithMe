package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class UpdateStudyRequestActivity extends AppCompatActivity {
    private StudyRequest studyRequest;
    private EditText subjectEditText;
    private EditText reasonEditText;
    private EditText placeEditText;
    private EditText commentsEditText;
    private EditText maxMatchesEditText;
    private Spinner periodSpinner;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_study_request);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = (StudyRequest) extras.getSerializable("studyRequest");

            // Initialize views
            subjectEditText = findViewById(R.id.et_subject);
            reasonEditText = findViewById(R.id.et_reason);
            placeEditText = findViewById(R.id.et_place);
            commentsEditText = findViewById(R.id.et_comments);
            maxMatchesEditText = findViewById((R.id.et_max_matches));
            periodSpinner = findViewById(R.id.spinner_period_of_study);


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
            maxMatchesEditText.setText(Integer.toString(studyRequest.getMaxMatches()));
            periodSpinner.setSelection(periodAdapter.getPosition(studyRequest.getPeriod()));

            if(studyRequest.isMatched()){
                subjectEditText.setEnabled(false);
                subjectEditText.setEnabled(false);
                reasonEditText.setEnabled(false);
                placeEditText.setEnabled(false);
                maxMatchesEditText.setEnabled(false);
                commentsEditText.setEnabled(false);
                periodSpinner.setEnabled(false);
            }

        } else {
            finish();
        }
    }

    public void updateStudyRequest(View view) {

        if(studyRequest.isMatched()) {
            setResult(-1);
            finish();
        }

        // Retrieve the updated values from the form fields
        String updatedSubject = subjectEditText.getText().toString();
        String updatedReason = reasonEditText.getText().toString();
        String updatedPlace = placeEditText.getText().toString();
        String updatedComments = commentsEditText.getText().toString();
        String updatedMaxMatches = maxMatchesEditText.getText().toString();
        PeriodOfStudy updatedPeriod = (PeriodOfStudy) periodSpinner.getSelectedItem();

        // Update the study request object with the new values
        studyRequest.setSubject(updatedSubject);
        studyRequest.setReason(updatedReason);
        studyRequest.setPlace(updatedPlace);
        studyRequest.setComments(updatedComments);
        studyRequest.setMaxMatches(Integer.parseInt(updatedMaxMatches));
        studyRequest.setPeriod(updatedPeriod);

        // Save the updated study request to the database
        try (StorageHandler sh = new StorageHandler(this, null, 1)) {
            sh.updateStudyRequest(studyRequest);
        }

        // Finish the activity and return to the dashboard
        setResult(102);
        finish();
    }


    public void deleteStudyRequest(View view) {
        // Delete the study request from the database
        try (StorageHandler sh = new StorageHandler(this, null, 1)) {
            sh.deleteStudyRequest(studyRequest);
        }

        // Finish the activity and return to the dashboard
        setResult(103);
        finish();
    }


}