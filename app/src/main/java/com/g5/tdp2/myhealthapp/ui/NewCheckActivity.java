package com.g5.tdp2.myhealthapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.util.DialogHelper;
import com.g5.tdp2.myhealthapp.util.MimeTypeResolver;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.g5.tdp2.myhealthapp.ui.UiReqCode.NEWCHECK_IMG_REQUEST_CODE;

public class NewCheckActivity extends AppCompatActivity {
    private static final int KB = 1024;
    private static final int MB = KB * 1024;
    private static final int MAX_SIZE = 4 * MB; // el tamano maximo permitido es 4MB
    private static final List<String> OK_IMG_TYPES =
            Arrays.asList("img", "jpg", "jpeg", "gif", "png");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imageView;
    private byte[] imgData;
    private String imgType;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        imageView = findViewById(R.id.newcheck_img);

        imageView.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, NEWCHECK_IMG_REQUEST_CODE);
        });

        btn = findViewById(R.id.newcheck_btn);
        btn.setOnClickListener(this::uploadImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Optional.ofNullable(data)
                .filter(n -> requestCode == NEWCHECK_IMG_REQUEST_CODE)
                .filter(n -> resultCode == Activity.RESULT_OK)
                .map(Intent::getData)
                .ifPresent(this::loadImage);
    }

    private void loadImage(Uri selectedImage) {
        imgType = getMimeType(selectedImage);

        Optional.ofNullable(imgType).map(s -> OK_IMG_TYPES.contains(s.toLowerCase())).map(t -> (Runnable) () -> {
            try {
                imgData = getData(selectedImage);
                ImageView imgView = findViewById(R.id.newcheck_img);
                imgView.setImageURI(selectedImage);
                imgView.setAdjustViewBounds(true);
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (Exception e) {
                DialogHelper.INSTANCE.showNonCancelableDialog(
                        NewCheckActivity.this, "Error al cargar imagen", e.getMessage());
            }
        }).orElse(() -> DialogHelper.INSTANCE.showNonCancelableDialog(
                NewCheckActivity.this, "Error al cargar imagen", "Tipo de imagen invalido")
        ).run();
    }

    private byte[] getData(Uri data) {
        try {
            return getData(getContentResolver().openInputStream(data), new ByteArrayOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Error al obtener datos de la imagen", e);
        }
    }

    private byte[] getData(InputStream is, ByteArrayOutputStream baos) throws IOException {
        if (baos.size() > MAX_SIZE) {
            throw new IllegalArgumentException("Imagen demasiado grande!");
        }

        byte[] buf = new byte[1024 * 64];
        int bytesRead = is.read(buf);
        if (bytesRead == -1) {
            return baos.toByteArray();
        } else {
            baos.write(buf, 0, bytesRead);
            return getData(is, baos);
        }
    }

    private String getMimeType(Uri uri) {
        return MimeTypeResolver.INSTANCE.fromUri(this, uri);
    }

    private void uploadImage(View v) {
        v.setEnabled(false);

        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("myhealthapp").child("checks").child("pending").child(UUID.randomUUID() + "." + imgType);
        UploadTask uploadTask = imgRef.putBytes(imgData);

        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(NewCheckActivity.this, "Fail to upload image", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            String msg = "Image uploaded - Size: " + taskSnapshot.getTotalByteCount();
            Toast.makeText(NewCheckActivity.this, msg, Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(t -> {
            v.setEnabled(true);
        });
    }
}
