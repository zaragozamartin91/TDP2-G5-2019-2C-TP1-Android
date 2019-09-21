package com.g5.tdp2.myhealthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.EMPTY_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.INVALID_ID;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.UNKNOWN_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.WRONG_CREDENTIALS;

public class LoginActivity extends MainActivity {
    private EditText idField;
    private EditText passField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginActivity self = this;
        idField = findViewById(R.id.login_id);
        passField = findViewById(R.id.login_password);

        // TODO : remover estas credenciales por defecto
        idField.setText("" + 34317677L);
        passField.setText("qwerty123");

        Button signupBtn = findViewById(R.id.login_signup_btn);
        signupBtn.setOnClickListener(v -> {
            Intent signupIntent = new Intent(self, SignupActivity.class);
            self.startActivity(signupIntent);
        });

        Button loginBtn = findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String id = idField.getText().toString();
        String pass = passField.getText().toString();

        MemberCredentials memberCredentials;
        try {
            memberCredentials = MemberCredentials.of(id, pass);
        } catch (NumberFormatException e) {
            idField.setError(getString(R.string.login_id_error_msg));
            return;
        }

        LoginMember usecase = UsecaseFactory.INSTANCE.getBean(LoginMember.class); // obtengo el caso de uso de inicio de sesion
        usecase.loginMember(memberCredentials, this::handleLoginOk, this::handleError);
    }

    private void handleLoginOk(Member member) {
        Intent signupIntent = new Intent(this, MapsActivity.class);
        startActivity(signupIntent);
    }

    private void showErrDialog(String title, String msg) {
        DialogHelper.INSTANCE.showNonCancelableDialog(this, title, msg);
    }

    private void handleError(Exception e) {
        switch (e.getMessage()) {
            case INVALID_ID:
                idField.setError(getString(R.string.login_id_error_msg));
                break;
            case EMPTY_PASSWORD:
                passField.setError(getString(R.string.login_password_err_msg));
                break;
            case WRONG_CREDENTIALS:
                showErrDialog(getString(R.string.login_dialog_title_err_msg), getString(R.string.login_err_403_msg));
                break;
            case UNKNOWN_ERROR:
            default:
                Log.e("login-error", e.getMessage(), e);
                showErrDialog(getString(R.string.login_dialog_title_err_msg), getString(R.string.std_unknown_err));
        }
    }
}


/*
https://tdp2-crmedical-api.herokuapp.com/auth/login
{
	"idn": 35317588,
	"password": "qwerty",
	"role": "affiliate"
}
 */