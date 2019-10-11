package com.g5.tdp2.myhealthapp.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.g5.tdp2.myhealthapp.ui.UiReqCode.NEWCHECK_IMG_REQUEST_CODE;

public class NewCheckActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        findViewById(R.id.newcheck_img).setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, NEWCHECK_IMG_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Optional.of(data)
                .filter(n -> requestCode == NEWCHECK_IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK)
                .map(Intent::getData)
                .ifPresent(this::uploadImage);
    }

    private void uploadImage(Uri selectedImage) {
        // Create a storage reference from our app
        String type = getMimeType(this, selectedImage);
        List<String> okTypes = Arrays.asList("img", "jpg", "jpeg", "gif", "png");


        byte[] imgData = getData(selectedImage);

        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("myhealthapp").child("checks").child("pending").child(UUID.randomUUID() + "." + type);
        UploadTask uploadTask = imgRef.putBytes(imgData);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(NewCheckActivity.this, "Fail to upload image", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            String msg = "Image uploaded - Size: " + taskSnapshot.getTotalByteCount();
            Toast.makeText(NewCheckActivity.this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    private byte[] getData(Uri data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = getContentResolver().openInputStream(data);
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = is.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
}
