package com.g5.tdp2.myhealthapp.ui;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMemberException;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import org.apache.commons.lang3.Validate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.EMPTY_FIRST_NAME;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.EMPTY_LAST_NAME;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_EMAIL;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_ID;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_MEMBER_ID;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_PLAN;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.PASSWORDS_DONT_MATCH;

public class SignupActivity extends AppCompatActivity {
    private EditText signupBirth;
    private final AtomicReference<Calendar> leDate = new AtomicReference<>();
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
            leDate.get().set(Calendar.YEAR, year);
            leDate.get().set(Calendar.MONTH, monthOfYear);
            leDate.get().set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            signupBirth.setError(null);
        };

        signupBirth.setOnClickListener(v -> {
            leDate.compareAndSet(null, Calendar.getInstance());
            new DatePickerDialog(
                    SignupActivity.this,
                    dateSetListener,
                    leDate.get().get(Calendar.YEAR),
                    leDate.get().get(Calendar.MONTH),
                    leDate.get().get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        findViewById(R.id.signup_btn).setOnClickListener(v -> submitSignupForm());
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
        signupBirth.setText(sdf.format(leDate.get().getTime()));
    }

    /**
     * Sube el formulario de registro
     */
    private void submitSignupForm() {
        long numId;
        try {
            numId = Long.parseLong(this.id.getText().toString());
            Validate.notNull(leDate.get());
        } catch (NumberFormatException e) {
            id.setError(getString(R.string.signup_id_error_msg));
            return;
        } catch (NullPointerException e) {
            signupBirth.setError(getString(R.string.signup_birth_err));
            return;
        }

        MemberSignupForm memberSignupForm = new MemberSignupForm(
                firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                numId, member.getText().toString(), plan.getText().toString(),
                leDate.get().getTime(), password.getText().toString(), repPassword.getText().toString()
        );

        SignupTask signupTask = new SignupTask(usecase, () -> {
            Toast.makeText(SignupActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
        }, error -> {
            switch (error.getMessage()) {
                case INVALID_PASSWORD:
                    password.setError(getString(R.string.signup_password_err_invalid_msg));
                    break;
                case EMPTY_FIRST_NAME:
                    firstName.setError(getString(R.string.signup_fname_err));
                    break;
                case EMPTY_LAST_NAME:
                    lastName.setError(getString(R.string.signup_lname_err));
                    break;
                case INVALID_EMAIL:
                    email.setError(getString(R.string.signup_email_err));
                    break;
                case INVALID_ID:
                    id.setError(getString(R.string.signup_id_err));
                    break;
                case INVALID_MEMBER_ID:
                    member.setError(getString(R.string.signup_member_err));
                    break;
                case PASSWORDS_DONT_MATCH:
                    repPassword.setError(getString(R.string.signup_reppass_err));
                    break;
                case INVALID_PLAN:
                    plan.setError(getString(R.string.signup_plan_err));
                    break;
                default:
                    DialogHelper.INSTANCE.showNonCancelableDialog(
                            SignupActivity.this,
                            getString(R.string.signup_unkerr_dialog_title),
                            getString(R.string.signup_unkerr_dialog_msg)
                    );
            }
            Log.e("signup-error", "Formulario invalido");
        });

        signupTask.execute(memberSignupForm);
    }
}


class SignupTask extends AsyncTask<MemberSignupForm, Void, Boolean> {
    private SignupMember usecase;
    private Runnable successCallback;
    private Consumer<Exception> errorCallback;

    SignupTask(SignupMember usecase, Runnable successCallback, Consumer<Exception> errorCallback) {
        this.usecase = usecase;
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
    }

    @Override
    protected Boolean doInBackground(MemberSignupForm... memberSignupForms) {
        try {
            usecase.signup(memberSignupForms[0]);
            return true;
        } catch (IllegalStateException | SignupMemberException e) {
            errorCallback.accept(e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) successCallback.run();
    }
}