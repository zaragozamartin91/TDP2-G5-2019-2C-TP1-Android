package com.g5.tdp2.myhealthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.Professional;
import com.g5.tdp2.myhealthapp.entity.ProfessionalSearchForm;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.entity.Zone;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionals;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.g5.tdp2.myhealthapp.usecase.SearchProfessionals.INVALID_FORM;
import static com.g5.tdp2.myhealthapp.usecase.SearchProfessionals.UNKNOWN_ERROR;

public class ProfessionalSearchActivity extends AppCompatActivity {
    public static final String MEMBER_EXTRA = "member";

    private Member member;

    private TextView name;
    private String specialtyVal;
    private String zoneVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_search);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        /* Habilito el default action bar */
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Profesionales");
        });

        name = findViewById(R.id.prof_search_name);

        setupSpecialties();

        setupZones();

        Button button = findViewById(R.id.prof_search_btn);
        button.setOnClickListener(this::searchProfessionals);
    }

    private void setupSpecialties() {
        Spinner specialty = findViewById(R.id.prof_search_specialty);
        List<Specialty> values = AppState.INSTANCE.getSpecialties();
        ArrayAdapter<Specialty> specAdapter = new ArrayAdapter<>(ProfessionalSearchActivity.this, R.layout.crm_spinner_item, values);
        specAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialty.setAdapter(specAdapter);
        specialty.setOnItemSelectedListener(new SpecialtyItemSelectedListener());
    }

    private void setupZones() {
        Spinner zone = findViewById(R.id.prof_search_zone);
        List<Zone> values = AppState.INSTANCE.getZones();
        ArrayAdapter<Zone> zoneAdapter = new ArrayAdapter<>(ProfessionalSearchActivity.this, R.layout.crm_spinner_item, values);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zone.setAdapter(zoneAdapter);
        zone.setOnItemSelectedListener(new ZoneItemSelectedListener());
    }

    class SpecialtyItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            specialtyVal = Optional.of(position)
                    .filter(p -> p > 0)
                    .map(parent::getItemAtPosition)
                    .map(Object::toString)
                    .orElse("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            specialtyVal = "";
        }
    }

    class ZoneItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            zoneVal = Optional.of(position)
                    .filter(p -> p > 0)
                    .map(parent::getItemAtPosition)
                    .map(Object::toString)
                    .orElse("");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            zoneVal = "";
        }
    }

    void searchProfessionals(View view) {
        ProfessionalSearchForm form =
                new ProfessionalSearchForm(specialtyVal, zoneVal, name.getText().toString(), member.getPlan());
        Toast.makeText(this, form.toString(), Toast.LENGTH_LONG).show();

        CrmBeanFactory.INSTANCE.getBean(SearchProfessionals.class)
                .searchProfessionals(form, this::handleProfessionals, this::handleSearchError);
    }

    private void handleProfessionals(List<Professional> professionals) {
        if (professionals.isEmpty()) {
            Toast.makeText(this, R.string.prof_search_empty_results, Toast.LENGTH_LONG).show();
        } else {
            // TODO : manejar resultados de busqueda
            Intent intent = new Intent(this, ProfessionalListActivity.class);
            intent.putExtra(ProfessionalListActivity.PROFESSIONALS_KEY, (Serializable) professionals);
            startActivity(intent);
        }
    }

    private void handleSearchError(Exception ex) {
        switch (ex.getMessage()) {
            case INVALID_FORM:
                Toast.makeText(this, R.string.prof_search_no_filters, Toast.LENGTH_LONG).show();
                break;
            case UNKNOWN_ERROR:
            default:
                DialogHelper.INSTANCE.showNonCancelableDialog(
                        this,
                        getString(R.string.prof_search_err_dialog_title),
                        getString(R.string.prof_search_err_dialog_msg)
                );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Si se clickeo la flecha "atras" -> se mata el activity y se vuelve al login */
        Optional.ofNullable(item)
                .filter(i -> i.getItemId() == android.R.id.home)
                .ifPresent(i -> finish());

        return super.onOptionsItemSelected(item);
    }
}




