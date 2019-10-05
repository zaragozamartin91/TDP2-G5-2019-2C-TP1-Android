package com.g5.tdp2.myhealthapp.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import org.apache.commons.lang3.Validate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.EMPTY_FIRST_NAME;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.EMPTY_LAST_NAME;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_EMAIL;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_ID;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_MEMBER_ID;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.INVALID_PLAN;
import static com.g5.tdp2.myhealthapp.entity.MemberSignupForm.PASSWORDS_DONT_MATCH;
import static com.g5.tdp2.myhealthapp.usecase.SignupMember.INVALID_FORM;

public class SignupActivity extends AppCompatActivity {
    private EditText signupBirth;
    private final AtomicReference<Calendar> leDate = new AtomicReference<>();
    private EditText firstName;
    private EditText lastName;
    private EditText id;
    private EditText member;
    private Spinner plan;
    private String planValue;
    private EditText email;
    private EditText password;
    private EditText repPassword;

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.available_plans, R.layout.crm_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plan.setAdapter(adapter);
        plan.setOnItemSelectedListener(this.planItemSelectedListener);

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        repPassword = findViewById(R.id.signup_repeat_password);

        // TODO : eliminar precargado de campos
        firstName.setText("Christian");
        lastName.setText("Angelone");
        member.setText("0987654321");
        // BIRTH : 1990-08-31
        id.setText("34317677");
//        plan.setText("A310");
        email.setText("zaragozamartin91@outlook.com");
        password.setText("qwerty123");
        repPassword.setText("qwerty123");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO : habilitar cuando se quiera agregar opciones extra a la app
//        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
                numId, member.getText().toString(), planValue,
                leDate.get().getTime(), password.getText().toString(), repPassword.getText().toString()
        );

        SignupMember usecase = CrmBeanFactory.INSTANCE.getBean(SignupMember.class); // obtengo el caso de uso del registro
        usecase.signup(memberSignupForm, this::handleSignupOk, this::handleSignupError);
    }

    private void handleSignupOk() {
        Toast.makeText(SignupActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
//        Intent mapsIntent = new Intent(this, ProfessionalMapActivity.class);
//        startActivity(mapsIntent);
        finish();
    }

    private void handleSignupError(Exception e) {
        switch (e.getMessage()) {
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
                System.out.println("error?!");
                ((TextView) plan.getSelectedView()).setError(getString(R.string.signup_plan_err));
                break;
            case INVALID_FORM:
                DialogHelper.INSTANCE.showNonCancelableDialog(
                        SignupActivity.this,
                        getString(R.string.signup_unkerr_dialog_title),
                        getString(R.string.signup_invalid_form_err)
                );
                break;
            default:
                DialogHelper.INSTANCE.showNonCancelableDialog(
                        SignupActivity.this,
                        getString(R.string.signup_unkerr_dialog_title),
                        getString(R.string.signup_unkerr_dialog_msg)
                );
                break;
        }
    }

    private AdapterView.OnItemSelectedListener planItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) plan.getSelectedView()).setError(null);
            planValue = Optional.ofNullable(parent.getItemAtPosition(position))
                    .map(Object::toString)
                    .orElse("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };
}