package com.g5.tdp2.myhealthapp.util;

import android.content.Context;
import android.widget.Toast;

public enum ToastHelper {
    INSTANCE;

    public void showShort(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showLong(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
