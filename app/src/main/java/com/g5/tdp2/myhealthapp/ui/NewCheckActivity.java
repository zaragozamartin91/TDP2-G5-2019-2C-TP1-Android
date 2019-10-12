package com.g5.tdp2.myhealthapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.util.DialogHelper;
import com.g5.tdp2.myhealthapp.util.MimeTypeResolver;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
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


public class NewCheckActivity extends ActivityWnavigation {
    private static final int KB = 1024;
    private static final int MB = KB * 1024;
    private static final int MAX_SIZE = 4 * MB; // el tamano maximo permitido es 4MB
    private static final List<String> OK_IMG_TYPES =
            Arrays.asList("img", "jpg", "jpeg", "gif", "png");

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imageView;
    private byte[] imgData;
    private String imgType;
    private Specialty specialtyVal;

    @Override
    protected String actionBarTitle() { return "Estudios"; }

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

        Button btn = findViewById(R.id.newcheck_btn);
        btn.setOnClickListener(this::uploadImage);

        setupSpecialties();
    }

    private void setupSpecialties() {
        Spinner specialty = findViewById(R.id.newcheck_specialty);
        List<Specialty> values = AppState.INSTANCE.getSpecialties();
        ArrayAdapter<Specialty> specAdapter = new ArrayAdapter<>(NewCheckActivity.this, R.layout.crm_spinner_item, values);
        specAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialty.setAdapter(specAdapter);
        specialty.setOnItemSelectedListener(new SpecialtyItemSelectedListener());
    }

    class SpecialtyItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            specialtyVal = Optional.of(position)
                    .filter(p -> p > 0)
                    .map(parent::getItemAtPosition)
                    .map(Specialty.class::cast)
                    .orElse(Specialty.DEFAULT_SPECIALTY);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            specialtyVal = Specialty.DEFAULT_SPECIALTY;
        }
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
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
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
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/" + imgType.toLowerCase()).build();
        UploadTask uploadTask = imgRef.putBytes(imgData, metadata);

        uploadTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                return imgRef.getDownloadUrl();
            } else {
                throw (task.getException() == null ? new Exception("Error desconocido") : task.getException());
            }
        }).addOnFailureListener(exception -> {
            Log.e("NewCheckActivity::uploadImage", "Error al subir imagen de estudio", exception);
            Toast.makeText(NewCheckActivity.this, "Error al subir imagen de estudio", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(uri -> {
            String msg = "Image uploaded - " + uri;
            Toast.makeText(NewCheckActivity.this, msg, Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(t -> v.setEnabled(true));

//        uploadTask.addOnFailureListener(exception -> {
//            Toast.makeText(NewCheckActivity.this, "Fail to upload image", Toast.LENGTH_SHORT).show();
//        }).addOnSuccessListener(taskSnapshot -> {
//            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//            // ...
//            String msg = "Image uploaded - Size: " + taskSnapshot.getTotalByteCount();
//            Toast.makeText(NewCheckActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//        }).addOnCompleteListener(t -> {
//            v.setEnabled(true);
//        });
    }
}
