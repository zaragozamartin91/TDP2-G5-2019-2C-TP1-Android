package com.g5.tdp2.myhealthapp.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Check;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

public class CheckDetailActivity extends ActivityWnavigation {
    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected String actionBarTitle() {
        return "Mi estudio";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        progressBar = findViewById(R.id.check_detail_progress);
        progressBar.setVisibility(View.VISIBLE);
        imageView = findViewById(R.id.check_detail_img);

        Check check = (Check) getIntent().getSerializableExtra(CHECK_EXTRA);

        TextView status = findViewById(R.id.check_detail_status);
        status.setText(getString(R.string.check_detail_status, check.translateStatus()));

        TextView obs = findViewById(R.id.check_detail_obs);
        obs.setText(getString(R.string.check_detail_obs, check.translateObs()));

        TextView checktype = findViewById(R.id.check_detail_checktype);
        checktype.setText(getString(R.string.check_detail_checktype, check.translateChecktype()));

        TextView specialty = findViewById(R.id.check_detail_specialty);
        specialty.setText(getString(
                R.string.check_card_specialty,
                check.translateSpecialty(AppState.INSTANCE::getSpecialty)));

        TextView createdAt = findViewById(R.id.check_detail_date);
        createdAt.setText(getString(
                R.string.check_detail_date,
                DateFormatter.YYYY_MM_DD.serialize(check.getCreatedAt())));

        TextView updatedAt = findViewById(R.id.check_detail_update);
        updatedAt.setText(getString(
                R.string.check_detail_update,
                DateFormatter.YYYY_MM_DD.serialize(check.getUpdatedAt())));

        TextView title = findViewById(R.id.check_detail_title);
        title.setText("Estudio " + check.getId());

        String url = check.getUrl();
        new UrlImgTask(this::drawImage, this::handleError).execute(url);
    }

    private void drawImage(Drawable d) {
        imageView.setImageDrawable(d);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleError(Exception e) {
        progressBar.setVisibility(View.INVISIBLE);
        Log.e("CheckDetailActivity-handleError", "Error al mostrar imagen", e);
        Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
    }
}
