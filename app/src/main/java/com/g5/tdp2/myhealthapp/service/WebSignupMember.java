package com.g5.tdp2.myhealthapp.service;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.usecase.SignupMember;
import com.g5.tdp2.myhealthapp.usecase.SignupMemberException;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.function.Consumer;

public class WebSignupMember implements SignupMember {
    private String url;
    private RequestQueue requestQueue;

    public WebSignupMember(String url, RequestQueue requestQueue) {
        this.url = url;
        this.requestQueue = requestQueue;
    }

    @Override
    public void signup(MemberSignupForm signupForm, Runnable succCallback, Consumer<Exception> errCallback) {
        try {
            signupForm.validate();

            String mRequestBody = new ObjectMapper().writeValueAsString(new RequestBody(signupForm));
            JSONObject jsonObject = new JSONObject(mRequestBody);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                Log.i("WebSignupMember-onResponse", response.toString());
                succCallback.run();
            }, error -> {
                Log.e("WebSignupMember-onErrorResponse", error.toString());
                logError(error.networkResponse.data);
                switch (error.networkResponse.statusCode) {
                    case 400:
                    case 403:
                        errCallback.accept(new SignupMemberException(INVALID_FORM));
                        break;
                    default:
                        errCallback.accept(new SignupMemberException(UNKNOWN_ERROR));
                }
            });
            requestQueue.add(jsonObjectRequest);

        } catch (IllegalStateException e) {
            errCallback.accept(new SignupMemberException(e.getMessage(), e));
        } catch (Exception e) {
            errCallback.accept(new SignupMemberException(INTERNAL_ERROR, e));
        }
    }

    private void logError(byte[] data) {
        try {
            String msg = new String(data, Charset.forName("UTF-8"));
            Log.e("WebSignupMember-onErrorResponse", msg);
        } catch (Exception e) { e.printStackTrace(); }
    }
}

class RequestBody {
    private MemberSignupForm signupForm;

    @JsonCreator
    RequestBody(@JsonProperty("info") MemberSignupForm signupForm) {
        this.signupForm = signupForm;
    }

    @JsonProperty("info")
    public MemberSignupForm getSignupForm() {
        return signupForm;
    }

    @JsonProperty("role")
    public String getRole() {
        return "affiliate";
    }
}

/* {
          firstname: 'Christian',
          lastname: 'Angelone',
          birthdate: '1990-08-31',
          affiliate_id: '1234567890',
          plan: 'A210',
          idn: 35317588
        },
        {
          firstname: 'Martin',
          lastname: 'Garcia',
          birthdate: '1990-07-21',
          affiliate_id: '0987654321',
          plan: 'A310',
          idn: 34317677
        }
el nombrre es irrlevante
lo importante es el dni y el nro de afiliado
y el plan tiene que ser ese que figura en esos json
*/
