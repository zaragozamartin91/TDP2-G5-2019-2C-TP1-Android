package com.g5.tdp2.myhealthapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.Optional;

public enum MimeTypeResolver {
    INSTANCE;

    public String fromUri(Context context, Uri uri) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return Optional.ofNullable(uri)
                .map(Uri::getScheme)
                .filter(ContentResolver.SCHEME_CONTENT::equals)
                .map(s -> mimeTypeMap.getExtensionFromMimeType(context.getContentResolver().getType(uri)))
                .orElse("");
    }
}
