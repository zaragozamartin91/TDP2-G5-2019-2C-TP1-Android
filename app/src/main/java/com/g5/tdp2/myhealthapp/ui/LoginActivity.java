package com.g5.tdp2.myhealthapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.EMPTY_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.INVALID_ID;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.INVALID_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.SHORT_PASSWORD;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.UNKNOWN_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.WRONG_CREDENTIALS;

public class LoginActivity extends AppCompatActivity {
    private EditText idField;
    private EditText passField;
    private LoginMember loginUsecase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginActivity self = this;
        idField = findViewById(R.id.login_id);
        passField = findViewById(R.id.login_password);
        loginUsecase = UsecaseFactory.INSTANCE.getBean(LoginMember.class);

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

        AtomicReference<String> idErrMsg = new AtomicReference<>();
        AtomicReference<String> passErrMsg = new AtomicReference<>();
        try {
            MemberCredentials memberCredentials = MemberCredentials.of(id, pass);
            Member member = loginUsecase.loginMember(memberCredentials);
            handleLoginOk(member);
        } catch (NumberFormatException e) {
            idErrMsg.set(getString(R.string.login_id_error_msg));
        } catch (IllegalStateException e) {
            switch (e.getMessage()) {
                case INVALID_ID:
                    idErrMsg.set(getString(R.string.login_id_error_msg));
                    break;
                case EMPTY_PASSWORD:
                    passErrMsg.set(getString(R.string.login_password_err_msg));
                    break;
                case SHORT_PASSWORD:
                case INVALID_PASSWORD:
                    passErrMsg.set(getString(R.string.login_password_err_invalid_msg));
                    break;
                default:
                    passErrMsg.set("Error");
                    passField.setError("Error");
            }
        } catch (Exception e) {
            switch (e.getMessage()) {
                case WRONG_CREDENTIALS:
                    showErrDialog(getString(R.string.login_dialog_title_err_msg), getString(R.string.login_err_403_msg));
                    break;
                case UNKNOWN_ERROR:
                default:
                    Log.e("login-error", e.getMessage(), e);
                    showErrDialog(getString(R.string.login_dialog_title_err_msg), getString(R.string.std_unknown_err));
            }
        }

        Optional.ofNullable(idErrMsg.get()).ifPresent(e -> idField.setError(e));
        Optional.ofNullable(passErrMsg.get()).ifPresent(e -> passField.setError(e));
    }

    private void handleLoginOk(Member member) {
        Intent signupIntent = new Intent(this, MapsActivity.class);
        startActivity(signupIntent);
    }

    private void showErrDialog(String title, String msg) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.std_close, (dialog, which) -> { })
                .setCancelable(false)
                .create()
                .show();
    }
}
