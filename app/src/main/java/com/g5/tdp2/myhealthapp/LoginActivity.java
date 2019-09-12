package com.g5.tdp2.myhealthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginActivity self = this;

        Button signupBtn = findViewById(R.id.login_signup_btn);
        signupBtn.setOnClickListener(v -> {
            Intent signupIntent = new Intent(self, SignupActivity.class);
            self.startActivity(signupIntent);
        });
    }

}
