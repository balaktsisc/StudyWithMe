package com.auth.studywithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

public class CreateStudyRequestActivity extends AppCompatActivity {
    User loggedUser;
    StorageHandler storageHandler;
    EditText subjectEditText;
    EditText reasonEditText;
    EditText placeEditText;
    EditText commentsEditText;
    EditText maxMatchesEditText;
    PeriodOfStudy selectedPeriod;
    Button createBtn;
   static int NUM_FLAGS = 4;
    boolean[] flags = new boolean[NUM_FLAGS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_request);

        // Initialize StorageHandler
        storageHandler = new StorageHandler(this,null,1);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            loggedUser = storageHandler.fetchUserById(extras.getLong("loggedUserId"));
        }

        createBtn = findViewById(R.id.btn_create_request);
        createBtn.setEnabled(false);

        subjectEditText = findViewById(R.id.et_subject);
        subjectEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (subjectEditText.hasFocus()) {
                    flags[0] = s.length() > 0;
                    TryActivateCreateButton();
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
                    TryActivateCreateButton();
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
                    TryActivateCreateButton();
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
                    TryActivateCreateButton();
                } else if (maxMatchesEditText.getText().toString().equals("")) {
                    maxMatchesEditText.setText("1");
                    flags[3] = true;
                    TryActivateCreateButton();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
        flags[3] = true;
        TryActivateCreateButton();


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

    public void createRequest(View view) {
        // Get form values
        String subject = subjectEditText.getText().toString();
        String reason = reasonEditText.getText().toString();
        String place = placeEditText.getText().toString();
        String comments = commentsEditText.getText().toString();
        String maxMatches = maxMatchesEditText.getText().toString();
        if (maxMatches.equals("")) maxMatches = "1";

        // Create new Study Request object and add it to the database
        StudyRequest sr = new StudyRequest(subject, reason, place,comments, new Date(),selectedPeriod,Integer.parseInt(maxMatches));
        sr.setRequestedUserId(loggedUser.getId());
        storageHandler.addStudyRequest(sr);

        setResult(100);
        finish();
    }

    void TryActivateCreateButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.btn_create_request).setEnabled(f);
    }
}