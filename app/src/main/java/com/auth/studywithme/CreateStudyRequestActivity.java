package com.auth.studywithme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;
import java.util.Objects;

/**
 * Activity for creating a study request.
 * Allows the user to enter details such as subject, reason, place, comments, and maximum matches for the study request.
 */
public class CreateStudyRequestActivity extends AppCompatActivity {
    User loggedUser;
    StorageHandler storageHandler;
    EditText subjectEditText;
    Spinner reasonSpinner;
    EditText placeEditText;
    EditText commentsEditText;
    EditText maxMatchesEditText;
    PeriodOfStudy selectedPeriod;
    Spinner periodSpinner;
    Button createBtn;
    static int NUM_FLAGS = 3;
    boolean[] flags = new boolean[NUM_FLAGS];   // Helper flags to determine the correctness of the user input

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_request);

        // Set the app's default night mode to NO night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize StorageHandler
        storageHandler = new StorageHandler(this,null,1);

        // Retrieve the logged-in user from the extras
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

        // Initialize the reasonSpinner and append selections (values)
        reasonSpinner = findViewById(R.id.sp_reason);
        ReasonOfStudy[] reasons = ReasonOfStudy.values();
        String[] reasonsNames = new String[reasons.length];
        for (int i = 0; i < reasons.length; i++)
            reasonsNames[i] = ReasonOfStudy.getReasonName(this,reasons[i]);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,reasonsNames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter1);

        placeEditText = findViewById(R.id.et_place);
        placeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (placeEditText.hasFocus()) {
                    flags[1] = s.length() > 0;
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
                    flags[2] = s.length() > 0;
                    TryActivateCreateButton();
                } else if (maxMatchesEditText.getText().toString().equals("")) {
                    maxMatchesEditText.setText("1");
                    flags[2] = true;
                    TryActivateCreateButton();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
        
        // if maxMatches is not filled, consider default; 1
        flags[2] = true;
        TryActivateCreateButton();

        // Initialize the periodSpinner and append selections (values)
        periodSpinner = findViewById(R.id.sp_period);
        PeriodOfStudy[] periods = PeriodOfStudy.values();
        String[] periodValues = new String[periods.length];
        for (int i = 0; i < periods.length; i++)
            periodValues[i] = periods[i].getDisplayName();

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,periodValues);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter2);

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

        if (savedInstanceState != null){
            //Retrieve data from the Bundle (other methods include getInt(), getBoolean() etc)
            CharSequence period = savedInstanceState.getCharSequence("period");
            CharSequence reason = savedInstanceState.getCharSequence("reason");
            //Restore the dynamic state of the UI
            periodSpinner.setSelection(adapter2.getPosition(period.toString()));
            reasonSpinner.setSelection(adapter1.getPosition(reason.toString()));
        }
        else{
            //Initialize the UI
            periodSpinner.setSelection(0);
            reasonSpinner.setSelection(0);
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
     * Creates a study request based on the entered details and adds it to the database.
     * Called when the "Create Request" button is clicked.
     *
     * @param view The button view that was clicked
     */
    public void createRequest(View view) {
        // Get form values
        String subject = subjectEditText.getText().toString();
        ReasonOfStudy reason = ReasonOfStudy.getReason(this,reasonSpinner.getSelectedItem().toString());
        String place = placeEditText.getText().toString();
        String comments = commentsEditText.getText().toString();
        String maxMatches = maxMatchesEditText.getText().toString();
        if (maxMatches.equals("")) maxMatches = "1";

        // Create a new StudyRequest object with the entered details
        StudyRequest sr = new StudyRequest(subject, reason, place,comments, new Date(),selectedPeriod,Integer.parseInt(maxMatches));
        sr.setRequestedUserId(loggedUser.getId());

        // Add the study request to the database
        storageHandler.addStudyRequest(sr);

        setResult(100);
        finish();
    }

    /**
     * Checks if all required fields are filled to enable the "Create Request" button.
     * Activates the button if all fields are filled, otherwise deactivates it.
     */
    void TryActivateCreateButton() {
        boolean f = true;
        for(boolean b : flags) { if (!b) { f = false; break;} }
        findViewById(R.id.btn_create_request).setEnabled(f);
    }
}