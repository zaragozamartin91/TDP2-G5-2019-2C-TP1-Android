package com.g5.tdp2.myhealthapp.ui;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;

public class UrlImgTask extends AsyncTask<String, Void, Drawable> {
    private Consumer<Drawable> consumer;
    private final Consumer<Exception> errCallback;

    public UrlImgTask(Consumer<Drawable> consumer, Consumer<Exception> errCallback) {
        this.consumer = consumer;
        this.errCallback = errCallback;
    }

    @Override
    protected Drawable doInBackground(String... strings) {
        String url = strings[0];
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            errCallback.accept(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        Optional.ofNullable(drawable).ifPresent(consumer);
    }
}
