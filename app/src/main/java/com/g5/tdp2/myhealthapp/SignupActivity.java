package com.g5.tdp2.myhealthapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

public class SignupActivity extends AppCompatActivity {
    private EditText signupBirth;
    private Calendar leDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        leDate = Calendar.getInstance();
        signupBirth = findViewById(R.id.signup_birth);

        /* Habilito el default action bar */
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        });

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            leDate.set(Calendar.YEAR, year);
            leDate.set(Calendar.MONTH, monthOfYear);
            leDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        signupBirth.setOnClickListener(v -> new DatePickerDialog(
                SignupActivity.this,
                dateSetListener,
                leDate.get(Calendar.YEAR),
                leDate.get(Calendar.MONTH),
                leDate.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Si se clickeo la flecha "atras" -> se mata el activity y se vuelve al login */
        Optional.ofNullable(item)
                .filter(i -> i.getItemId() == android.R.id.home)
                .ifPresent(i -> finish());

        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        signupBirth.setText(sdf.format(leDate.getTime()));
    }
}
