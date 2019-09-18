package com.g5.tdp2.myhealthapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.tdp2.myhealthapp.entity.MemberCredentials;
import com.g5.tdp2.myhealthapp.entity.MemberSignupForm;
import com.g5.tdp2.myhealthapp.util.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.g5.tdp2.myhealthapp", appContext.getPackageName());
    }


    @Test
    public void testHttpPost() throws IOException {

        URL url = new URL("https://tdp2-crmedical-api.herokuapp.com/auth/login");
        HttpURLConnection client = (HttpURLConnection) url.openConnection();

        client.setRequestMethod("POST");
        client.setDoOutput(true);


    }


    @Test
    public void testLoopj() throws UnsupportedEncodingException {
        MemberCredentials memberCredentials = new MemberCredentials(35317588L, "qwerty");

        SyncHttpClient client = new SyncHttpClient();
        String json = JsonParser.INSTANCE.writeValueAsString(memberCredentials);

        ByteArrayEntity entity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        String contentType = "application/json";
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
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


    @Test
    public void testLoopjAsync() throws ExecutionException, InterruptedException {
        MemberCredentials memberCredentials = new MemberCredentials(35317588L, "qwerty");

        JSONObject jsonObject = new JsonTask().execute(memberCredentials).get();
        System.out.println(jsonObject);
    }
}

class JsonTask extends AsyncTask<MemberCredentials, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(MemberCredentials... memberCredentials) {
        SyncHttpClient client = new SyncHttpClient();
        String json = JsonParser.INSTANCE.writeValueAsString(memberCredentials);

        ByteArrayEntity entity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
        String contentType = "application/json";
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, contentType));



        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
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
        return null;
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