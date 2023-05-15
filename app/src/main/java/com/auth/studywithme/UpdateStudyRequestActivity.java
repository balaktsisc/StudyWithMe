package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateStudyRequestActivity extends AppCompatActivity {
    private StudyRequest studyRequest;

    private EditText subjectEditText;
    private EditText reasonEditText;
    private EditText placeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_study_request);


        // Get the selected study request from the intent
        studyRequest = (StudyRequest) getIntent().getSerializableExtra("studyRequest");

        // Initialize views
        subjectEditText = findViewById(R.id.et_subject);
        reasonEditText = findViewById(R.id.et_reason);
        placeEditText = findViewById(R.id.et_place);

        // Pre-fill the form fields with the current values of the study request
        subjectEditText.setText(studyRequest.getSubject());
        reasonEditText.setText(studyRequest.getReason());
        placeEditText.setText(studyRequest.getPlace());
    }

    public void updateStudyRequest(View view) {
        // Retrieve the updated values from the form fields
        String updatedSubject = subjectEditText.getText().toString();
        String updatedReason = reasonEditText.getText().toString();
        String updatedPlace = placeEditText.getText().toString();

        // Update the study request object with the new values
        studyRequest.setSubject(updatedSubject);
        studyRequest.setReason(updatedReason);
        studyRequest.setPlace(updatedPlace);

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