package com.g5.tdp2.myhealthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<String> errMsg = new AtomicReference<>();
        try {
            Member member = loginUsecase.loginMember(MemberCredentials.of(id, pass));
            System.out.println(member);
        } catch (NumberFormatException e) {
            errMsg.set(getString(R.string.login_id_error_msg));
            idField.setError(errMsg.get());
        } catch (IllegalStateException e) {
            switch (e.getMessage()) {
                case MemberCredentials.EMPTY_PASSWORD:
                    errMsg.set(getString(R.string.login_password_err_msg));
                    passField.setError(errMsg.get());
                    break;
                case MemberCredentials.SHORT_PASSWORD:
                    errMsg.set(getString(R.string.login_password_err_short_msg));
                    passField.setError(errMsg.get());
                    break;
                default:
                    errMsg.set("Error");
                    passField.setError("Error");
            }
        }

        Optional.ofNullable(errMsg.get()).ifPresent(
                e -> Toast.makeText(this, e, Toast.LENGTH_LONG).show()
        );
    }
}
