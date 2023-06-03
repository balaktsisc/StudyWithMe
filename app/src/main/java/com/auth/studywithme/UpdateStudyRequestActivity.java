package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class UpdateStudyRequestActivity extends AppCompatActivity {
    StudyRequest studyRequest;
    EditText subjectEditText;
    EditText reasonEditText;
    EditText placeEditText;
    EditText commentsEditText;
    EditText maxMatchesEditText;
    Spinner periodSpinner;
    StorageHandler sh;
    Button updateBtn;

    static int NUM_FLAGS = 4;
    boolean[] flags = new boolean[NUM_FLAGS];
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_study_request);

        sh = new StorageHandler(this,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));

            for (int i = 0; i < NUM_FLAGS; i++) flags[i] = true;

            // Initialize views
            updateBtn = findViewById(R.id.btn_update);

            subjectEditText = findViewById(R.id.et_subject);
            subjectEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (subjectEditText.hasFocus()) {
                        flags[0] = s.length() > 0;
                        TryActivateUpdateButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });

            reasonEditText = findViewById(R.id.et_reason);
            reasonEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (reasonEditText.hasFocus()) {
                        flags[1] = s.length() > 0;
                        TryActivateUpdateButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
            placeEditText = findViewById(R.id.et_place);
            placeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (placeEditText.hasFocus()) {
                        flags[2] = s.length() > 0;
                        TryActivateUpdateButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });
            commentsEditText = findViewById(R.id.et_comments);
            maxMatchesEditText = findViewById(R.id.et_max_matches);
            maxMatchesEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (maxMatchesEditText.hasFocus()) {
                        flags[3] = s.length() > 0;
                        TryActivateUpdateButton();
                    } else if (maxMatchesEditText.getText().toString().equals("")) {
                        maxMatchesEditText.setText("1");
                        flags[3] = true;
                        TryActivateUpdateButton();
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
            });


            periodSpinner = findViewById(R.id.sp_period);


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
            maxMatchesEditText.setText(Integer.toString(studyRequest.getMaxMatches()));
            periodSpinner.setSelection(adapter.getPosition(studyRequest.getPeriod().getDisplayName()));
        } else {
            finish();
        }
    }

    public void updateStudyRequest(View view) {
        StudyRequest s = new StudyRequest();

        if(sh.isStudyRequestMatched(studyRequest.getId())) {
            setResult(-1);
            finish();
        }

        // Retrieve the updated values from the form fields
        String updatedSubject = subjectEditText.getText().toString();
        String updatedReason = reasonEditText.getText().toString();
        String updatedPlace = placeEditText.getText().toString();
        String updatedComments = commentsEditText.getText().toString();
        String updatedMaxMatches = maxMatchesEditText.getText().toString();
        if (updatedMaxMatches.equals("")) updatedMaxMatches = "1";
        PeriodOfStudy updatedPeriod = PeriodOfStudy.getPeriodOfStudy((String) periodSpinner.getSelectedItem());

        // Update the study request object with the new values
        s.setSubject(updatedSubject);
        s.setReason(updatedReason);
        s.setPlace(updatedPlace);
        s.setComments(updatedComments);
        s.setMaxMatches(Integer.parseInt(updatedMaxMatches));
        s.setPeriod(updatedPeriod);
        s.setDatetime(new Date());

        // Save the updated study request to the database

        sh.updateStudyRequest(studyRequest.getId(),s);

        // Finish the activity and return to the dashboard
        setResult(102);
        finish();
    }

    public void deleteStudyRequest(View view) {
        // Delete the study request from the database
        sh.deleteStudyRequest(studyRequest.getId());

        // Finish the activity and return to the dashboard
        setResult(103);
        finish();
    }

    void TryActivateUpdateButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.btn_update).setEnabled(f);
    }

}