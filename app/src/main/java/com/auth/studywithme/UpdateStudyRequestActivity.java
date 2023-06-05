package com.auth.studywithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.Objects;

/**
 * Activity for updating a study request.
 */
public class UpdateStudyRequestActivity extends AppCompatActivity {
    StudyRequest studyRequest;
    EditText subjectEditText;
    Spinner reasonSpinner;
    EditText placeEditText;
    EditText commentsEditText;
    EditText maxMatchesEditText;
    Spinner periodSpinner;
    StorageHandler sh;
    Button updateBtn;

    static int NUM_FLAGS = 3;
    boolean[] flags = new boolean[NUM_FLAGS];
    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_study_request);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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


            placeEditText = findViewById(R.id.et_place);
            placeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (placeEditText.hasFocus()) {
                        flags[1] = s.length() > 0;
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
                        flags[2] = s.length() > 0;
                        TryActivateUpdateButton();
                    } else if (maxMatchesEditText.getText().toString().equals("")) {
                        maxMatchesEditText.setText("1");
                        flags[2] = true;
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

            reasonSpinner = findViewById(R.id.sp_reason);
            ReasonOfStudy[] reasons = ReasonOfStudy.values();
            String[] reasonsNames = new String[reasons.length];
            for (int i = 0; i < reasons.length; i++)
                reasonsNames[i] = ReasonOfStudy.getReasonName(this,reasons[i]);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,reasonsNames);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reasonSpinner.setAdapter(adapter1);


            // Pre-fill the form fields with the current values of the study request
            subjectEditText.setText(studyRequest.getSubject());
            reasonSpinner.setSelection(adapter1.getPosition(ReasonOfStudy.getReasonName(this,studyRequest.getReason())));
            placeEditText.setText(studyRequest.getPlace());
            commentsEditText.setText(studyRequest.getComments());
            maxMatchesEditText.setText(Integer.toString(studyRequest.getMaxMatches()));
            periodSpinner.setSelection(adapter.getPosition(studyRequest.getPeriod().getDisplayName()));

            if (savedInstanceState != null){
                //Retrieve data from the Bundle (other methods include getInt(), getBoolean() etc)
                CharSequence period = savedInstanceState.getCharSequence("period");
                CharSequence reason = savedInstanceState.getCharSequence("reason");
                //Restore the dynamic state of the UI
                periodSpinner.setSelection(adapter.getPosition(period.toString()));
                reasonSpinner.setSelection(adapter1.getPosition(reason.toString()));
            }
            else{
                //Initialize the UI
                periodSpinner.setSelection(0);
                reasonSpinner.setSelection(0);
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save data to the Bundle (other methods include putInt(), putBoolean() etc)
        CharSequence period = periodSpinner.getSelectedItem().toString();
        outState.putCharSequence("period", period);
        CharSequence reason = reasonSpinner.getSelectedItem().toString();
        outState.putCharSequence("reason", reason);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the study request with the new values from the form fields.
     *
     * @param view The clicked view.
     */
    public void updateStudyRequest(View view) {
        StudyRequest s = new StudyRequest();

        if(sh.isStudyRequestMatched(studyRequest.getId())) {
            setResult(-1);
            finish();
        }

        // Retrieve the updated values from the form fields
        String updatedSubject = subjectEditText.getText().toString();
        ReasonOfStudy updatedReason = ReasonOfStudy.getReason(this,reasonSpinner.getSelectedItem().toString());
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

    /**
     * Deletes the study request from the database.
     *
     * @param view The clicked view.
     */
    public void deleteStudyRequest(View view) {
        // Delete the study request from the database
        sh.deleteStudyRequest(studyRequest.getId());

        // Finish the activity and return to the dashboard
        setResult(103);
        finish();
    }

    /**
     * Tries to activate the update button based on the form field flags.
     */
    void TryActivateUpdateButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.btn_update).setEnabled(f);
    }

}