package com.g5.tdp2.myhealthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.util.CrmFirebaseMessagingService;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.EMPTY_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.INVALID_ID;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.UNKNOWN_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.WRONG_CREDENTIALS;

public class LoginActivity extends MainActivity {
    private EditText idField;
    private EditText passField;
    private Button loginBtn;
    private ProgressBar progressBar;

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

        loginBtn = findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(v -> handleLogin());

        progressBar = findViewById(R.id.login_progress);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleLogin() {
        loginBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String id = idField.getText().toString();
        String pass = passField.getText().toString();


        try {
            CrmFirebaseMessagingService.getCurrToken(token -> {
                MemberCredentials memberCredentials = MemberCredentials.of(id, pass, token);
                LoginMember usecase = CrmBeanFactory.INSTANCE.getBean(LoginMember.class); // obtengo el caso de uso de inicio de sesion
                usecase.loginMember(memberCredentials, this::handleLoginOk, this::handleError);
            }, err -> {
                Log.e("LoginActivity::handleLogin::error", "Error al obtener token de firebase de usuario", err);
                this.handleError(new RuntimeException(UNKNOWN_ERROR));
            });
        } catch (NumberFormatException e) {
            idField.setError(getString(R.string.login_id_error_msg));
        }
    }

    private void handleLoginOk(Member member) {
        loginBtn.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, HubActivity.class);
        intent.putExtra(HubActivity.MEMBER_EXTRA, member);
        startActivity(intent);
    }

    private void handleError(Exception e) {
        loginBtn.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);

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

    private void showErrDialog(String title, String msg) {
        DialogHelper.INSTANCE.showNonCancelableDialog(this, title, msg);
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