package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Objects;

/**
 * Activity for viewing a study request.
 */
public class ViewStudyRequestActivity extends AppCompatActivity {
    StudyRequest studyRequest;
    StorageHandler sh;
    User user;
    Spinner periodSpinner;
    Spinner reasonSpinner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_study_request);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sh = new StorageHandler(this,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));
            user = sh.fetchUserById(extras.getLong("loggedUserId"));

            // Initialize views
            EditText subjectEditText = findViewById(R.id.et_subject);
            reasonSpinner = findViewById(R.id.sp_reason);
            EditText placeEditText = findViewById(R.id.et_place);
            EditText commentsEditText = findViewById(R.id.et_comments);
            periodSpinner = findViewById(R.id.sp_period);

            // Initialize the periodSpinner and append selections (values)
            PeriodOfStudy[] periods = PeriodOfStudy.values();
            String[] periodValues = new String[periods.length];
            for (int i = 0; i < periods.length; i++)
                periodValues[i] = periods[i].getDisplayName();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,periodValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            periodSpinner.setAdapter(adapter);

            // Initialize the reasonSpinner and append selections (values)
            ReasonOfStudy[] reasons = ReasonOfStudy.values();
            String[] reasonsNames = new String[reasons.length];
            for (int i = 0; i < reasons.length; i++)
                reasonsNames[i] = ReasonOfStudy.getReasonName(this,reasons[i]);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,reasonsNames);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reasonSpinner.setAdapter(adapter1);


            // Pre-fill the form fields with the current values of the study request
            subjectEditText.setText(studyRequest.getSubject());
            placeEditText.setText(studyRequest.getPlace());
            commentsEditText.setText(studyRequest.getComments());
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

            reasonSpinner.setSelection(adapter1.getPosition(ReasonOfStudy.getReasonName(this,studyRequest.getReason())));
            reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(view != null) ((TextView) view).setTextColor(Color.BLACK);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            periodSpinner.setSelection(adapter.getPosition(studyRequest.getPeriod().getDisplayName()));
            periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(view != null) ((TextView) view).setTextColor(Color.BLACK);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if(sh.isStudyRequestMatched(studyRequest.getId())){
                subjectEditText.setInputType(InputType.TYPE_NULL);
                reasonSpinner.setEnabled(false);
                placeEditText.setInputType(InputType.TYPE_NULL);
                commentsEditText.setInputType(InputType.TYPE_NULL);
                periodSpinner.setEnabled(false);
            }

            if(studyRequest.getRequestedUserId() == user.getId()) {
                Button ownerUser = findViewById(R.id.ownerUser);
                ownerUser.setVisibility(View.GONE);
                TextView title = findViewById(R.id.tv_study_request_details);
                title.setText(R.string.your_study_request);
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

    public void viewSROwner(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId",studyRequest.getRequestedUserId());
        startActivity(intent);
    }
}