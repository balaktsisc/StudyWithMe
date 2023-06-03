package com.auth.studywithme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewStudyRequestActivity extends AppCompatActivity {
    StudyRequest studyRequest;
    StorageHandler sh;
    User user;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_study_request);

        sh = new StorageHandler(this,null,1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            studyRequest = sh.fetchStudyRequestById(extras.getLong("studyRequestId"));
            user = sh.fetchUserById(extras.getLong("loggedUserId"));

            // Initialize views
            EditText subjectEditText = findViewById(R.id.et_subject);
            Spinner reasonSpinner = findViewById(R.id.sp_reason);
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


            reasonSpinner.setSelection(adapter1.getPosition(ReasonOfStudy.getReasonName(this,studyRequest.getReason())));
            reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view).setTextColor(Color.BLACK);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            periodSpinner.setSelection(adapter.getPosition(studyRequest.getPeriod().getDisplayName()));
            periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) view).setTextColor(Color.BLACK);
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

    public void viewSROwner(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("userId",studyRequest.getRequestedUserId());
        startActivity(intent);
    }
}