package com.g5.tdp2.myhealthapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.usecase.LoginMember;
import com.g5.tdp2.myhealthapp.usecase.LoginMemberException;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.EMPTY_PASSWORD;
import static com.g5.tdp2.myhealthapp.entity.MemberCredentials.INVALID_ID;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.UNKNOWN_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.LoginMember.WRONG_CREDENTIALS;

public class LoginActivity extends MainActivity {
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

        MemberCredentials memberCredentials;
        try {
            memberCredentials = MemberCredentials.of(id, pass);
        } catch (NumberFormatException e) {
            idField.setError(getString(R.string.login_id_error_msg));
            return;
        }

        LoginTask loginTask = new LoginTask(loginUsecase, this::handleLoginOk, e -> {
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
        });

        //loginTask.execute(memberCredentials);

        //new LoopjTask().execute(memberCredentials, this);

        //new LoginTask2(this).execute(new MemberCredentials(35317588L,"qwerty"));

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "https://tdp2-crmedical-api.herokuapp.com/auth/login";
            String mRequestBody = new ObjectMapper().writeValueAsString(new MemberCredentials(35317588L, "qwerty"));

            JSONObject jsonObject = new JSONObject(mRequestBody);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("response", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLoginOk(Member member) {
        Intent signupIntent = new Intent(this, MapsActivity.class);
        startActivity(signupIntent);
    }

    private void showErrDialog(String title, String msg) {
        DialogHelper.INSTANCE.showNonCancelableDialog(this, title, msg);
    }
}

class LoginTask extends AsyncTask<MemberCredentials, Void, Member> {
    private LoginMember loginUsecase;
    private Consumer<Member> successCallback;
    private Consumer<Exception> errCallback;

    LoginTask(LoginMember loginUsecase, Consumer<Member> successCallback, Consumer<Exception> errCallback) {
        this.loginUsecase = loginUsecase;
        this.successCallback = successCallback;
        this.errCallback = errCallback;
    }

    @Override
    protected Member doInBackground(MemberCredentials... memberCredentials) {
        try {
            return loginUsecase.loginMember(memberCredentials[0]);
        } catch (IllegalStateException | LoginMemberException e) {
            errCallback.accept(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Member member) {
        Optional.ofNullable(member).ifPresent(successCallback);
    }
}


class LoopjTask {
    public void execute(MemberCredentials mc, Context appContext) {
        MemberCredentials memberCredentials = new MemberCredentials(35317588L, "qwerty");

        AsyncHttpClient client = new AsyncHttpClient();
        String json = JsonParser.INSTANCE.writeValueAsString(memberCredentials);

        ByteArrayEntity entity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        String contentType = "application/json";
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));

        String url = "https://tdp2-crmedical-api.herokuapp.com/auth/login";

        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("le-response", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
                Log.e("le-fail", responseString);
            }
        };
        client.post(appContext, url, entity, contentType, responseHandler);

        System.out.println("waiting...");
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