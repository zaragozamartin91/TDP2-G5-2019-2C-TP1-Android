package com.g5.tdp2.myhealthapp.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMemberException;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

public class SignupActivity extends AppCompatActivity {
    private EditText signupBirth;
    private Calendar leDate;
    private EditText firstName;
    private EditText lastName;
    private EditText id;
    private EditText member;
    private EditText plan;
    private EditText email;
    private EditText password;
    private EditText repPassword;

    private SignupMember usecase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName = findViewById(R.id.signup_firstname);
        lastName = findViewById(R.id.signup_lastname);
        leDate = Calendar.getInstance();
        signupBirth = findViewById(R.id.signup_birth);
        id = findViewById(R.id.signup_id);
        member = findViewById(R.id.signup_member);
        plan = findViewById(R.id.signup_plan);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        repPassword = findViewById(R.id.signup_repeat_password);

        usecase = UsecaseFactory.INSTANCE.getBean(SignupMember.class);

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

        findViewById(R.id.signup_btn).setOnClickListener(v -> {
            long numId;
            try {
                numId = Long.parseLong(this.id.getText().toString());
            } catch (NumberFormatException e) {
                id.setError(getString(R.string.signup_id_error_msg));
                return;
            }
            MemberSignupForm memberSignupForm = new MemberSignupForm(
                    firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                    numId, member.getText().toString(), plan.getText().toString(),
                    leDate.getTime(), password.getText().toString(), repPassword.getText().toString()
            );

            try {
                usecase.signup(memberSignupForm);
            } catch (IllegalStateException e) {
                Log.e("signup-error", "Formulario invalido");
            } catch (SignupMemberException e) {
                Log.e("signup-error", "Error de registro");
            }
        });
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
