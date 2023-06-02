package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class CreateStudyRequestActivity extends AppCompatActivity {

    private User loggedUser;
    StorageHandler storageHandler;
    private EditText subjectEditText;
    private EditText reasonEditText;
    private EditText placeEditText;
    private EditText commentsEditText;
    private EditText maxMatchesEditText;
    private PeriodOfStudy selectedPeriod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_request);

        // Initialize StorageHandler
        storageHandler = new StorageHandler(this,null,1);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
//            Get current user
            loggedUser = storageHandler.fetchUserById(extras.getLong("loggedUserId"));
        }

        subjectEditText = findViewById(R.id.et_subject);
        reasonEditText = findViewById(R.id.et_reason);
        placeEditText = findViewById(R.id.et_place);
        commentsEditText = findViewById(R.id.et_comments);
        maxMatchesEditText = findViewById(R.id.et_max_matches);

        Spinner periodSpinner = findViewById(R.id.sp_period);
        // Define the string array for the spinner values


        PeriodOfStudy[] periods = PeriodOfStudy.values();
        String[] periodValues = new String[periods.length];
        for (int i = 0; i < periods.length; i++)
            periodValues[i] = periods[i].getDisplayName();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,periodValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter);

        // Set listener so the spinner can listen for the selected item
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  String selectedItem = periodValues[position];
                // Convert the selected string value to the corresponding PeriodOfStudy enum value
                selectedPeriod = PeriodOfStudy.getPeriodOfStudy(periodValues[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected (if needed)
                selectedPeriod = null;
            }
        });


    }

    public void onCreateButtonClick(View view) {
        // Get form values
        String subject = subjectEditText.getText().toString();
        String reason = reasonEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String comments = commentsEditText.getText().toString();
        String maxMatches = maxMatchesEditText.getText().toString();

        // Create new Study Request object and add it to the database
        StudyRequest sr = new StudyRequest(subject, reason, place,comments, new Date(),selectedPeriod,Integer.parseInt(maxMatches));
        sr.setRequestedUserId(loggedUser.getId());
        storageHandler.addStudyRequest(sr);

        setResult(100);
        finish();
    }
}