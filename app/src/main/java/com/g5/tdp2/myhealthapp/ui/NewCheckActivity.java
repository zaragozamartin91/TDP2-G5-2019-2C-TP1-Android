package com.g5.tdp2.myhealthapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.NewCheckForm;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.usecase.PostNewCheck;
import com.g5.tdp2.myhealthapp.util.DialogHelper;
import com.g5.tdp2.myhealthapp.util.MimeTypeResolver;
import com.g5.tdp2.myhealthapp.util.ToastHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
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

    private Member member;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView imageView;
    private byte[] imgData;
    private String imgType;
    private Specialty specialtyVal;
    private String imgUri;
    private StorageReference imgRef;
    private Button btn;
    private ToastHelper toastHelper = ToastHelper.INSTANCE;
    private ProgressBar progressBar;
    private Spinner specialty;
    private Spinner checkType;

    @Override
    protected String actionBarTitle() { return "Estudios"; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        progressBar = findViewById(R.id.newcheck_progress);
        progressBar.setVisibility(View.INVISIBLE);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        imageView = findViewById(R.id.newcheck_img);

        imageView.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, NEWCHECK_IMG_REQUEST_CODE);
        });

        btn = findViewById(R.id.newcheck_btn);
        btn.setOnClickListener(this::postNewCheck);

        setupSpecialties();
        setupCheckTypes();
    }

    private void setupSpecialties() {
        specialty = findViewById(R.id.newcheck_specialty);
        List<Specialty> values = AppState.INSTANCE.getSpecialties();
        ArrayAdapter<Specialty> specAdapter = new ArrayAdapter<>(NewCheckActivity.this, R.layout.crm_spinner_item, values);
        specAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialty.setAdapter(specAdapter);
        specialty.setOnItemSelectedListener(new SpecialtyItemSelectedListener());
    }

    private void setupCheckTypes() {
        // TODO : agregar logica
        checkType = findViewById(R.id.newcheck_checktype);
        List<String> values = Collections.singletonList("Seleccione un tipo de estudio");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(NewCheckActivity.this, R.layout.crm_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checkType.setAdapter(adapter);
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

    /**
     * Carga un preview de la imagen en la pantalla
     *
     * @param selectedImage Uri de imagen seleccionada
     */
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
        Validate.inclusiveBetween(0, MAX_SIZE, baos.size(), "Imagen demasiado grande!");

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

    private void postNewCheck(View v) {
        if (specialtyVal == null || Specialty.DEFAULT_SPECIALTY.equals(specialtyVal)) {
            Toast.makeText(this, "Debe seleccionar una especialidad", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imgType == null || imgData == null) {
            Toast.makeText(this, R.string.newcheck_no_img, Toast.LENGTH_SHORT).show();
            return;
        }

        v.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        StorageReference storageRef = storage.getReference();
        imgRef = storageRef.child("myhealthapp").child("checks").child("pending").child(UUID.randomUUID() + "." + imgType);
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/" + imgType.toLowerCase()).build();
        UploadTask uploadTask = imgRef.putBytes(imgData, metadata);

        uploadTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                return imgRef.getDownloadUrl();
            } else {
                throw (task.getException() == null ? new Exception("Error desconocido") : task.getException());
            }
        }).addOnFailureListener(this::onImgUploadErr).addOnSuccessListener(this::onImgUploadOk);
    }

    private void onImgUploadErr(Exception exception) {
        Log.e("NewCheckActivity::onImgOk", "Error al subir imagen de estudio", exception);
        Toast.makeText(NewCheckActivity.this, "Error al subir imagen de estudio", Toast.LENGTH_SHORT).show();
    }

    private void onImgUploadOk(Uri uri) {
        ToastHelper.INSTANCE.showShort(NewCheckActivity.this, "Image uploaded - " + uri);

        imgUri = uri.toString();
        String path = Optional.ofNullable(imgRef).map(StorageReference::getPath).orElse("");
        long specId = Optional.ofNullable(specialtyVal).orElse(Specialty.DEFAULT_SPECIALTY).getId();
        long userId = member.getId();
        NewCheckForm form = new NewCheckForm(imgUri, path, specId, userId);

        PostNewCheck usecase = CrmBeanFactory.INSTANCE.getBean(PostNewCheck.class);
        usecase.postNewCheck(form, () -> {
            btn.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            toastHelper.showShort(NewCheckActivity.this, "Estudio subido exitosamente");
            resetInput();
        }, err -> {
            btn.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            Log.e("NewCheckActivity-onImgUploadOk-error", err.toString());
            toastHelper.showShort(NewCheckActivity.this, "Ocurrio un error al crear el estudio");
        });
    }

    private void resetInput() {
        specialtyVal = Specialty.DEFAULT_SPECIALTY;
        specialty.setSelection(0, true);
        imgType = null;
        imgData = null;
        imageView.setImageResource(R.drawable.med_chart_logo);
    }
}
