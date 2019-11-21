package com.g5.tdp2.myhealthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.Sanatorium;
import com.g5.tdp2.myhealthapp.entity.SanatoriumSearchForm;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.entity.Zone;
import com.g5.tdp2.myhealthapp.usecase.SearchSanatoriums;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.g5.tdp2.myhealthapp.usecase.Usecase.INTERNAL_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.INVALID_FORM;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.UNKNOWN_ERROR;

public class SanatoriumSearchActivity extends ActivityWnavigation {
    private Member member;

    private TextView name;
    private Specialty specialtyVal;
    private Zone zoneVal;

    @Override
    protected String actionBarTitle() { return "Sanatorios"; }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanatorium_search);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);

        name = findViewById(R.id.san_search_name);

        setupSpecialties();

        setupZones();

        Button button = findViewById(R.id.san_search_btn);
        button.setOnClickListener(this::searchSanatoriums);

    }

    private void setupSpecialties() {
        Spinner specialty = findViewById(R.id.san_search_specialty);
        List<Specialty> values = AppState.INSTANCE.getSpecialties();
        ArrayAdapter<Specialty> specAdapter = new ArrayAdapter<>(SanatoriumSearchActivity.this, R.layout.crm_spinner_item, values);
        specAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialty.setAdapter(specAdapter);
        specialty.setOnItemSelectedListener(new SanatoriumSearchActivity.SpecialtyItemSelectedListener());
    }

    private void setupZones() {
        Spinner zone = findViewById(R.id.san_search_zone);
        List<Zone> values = AppState.INSTANCE.getZones();
        ArrayAdapter<Zone> zoneAdapter = new ArrayAdapter<>(SanatoriumSearchActivity.this, R.layout.crm_spinner_item, values);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zone.setAdapter(zoneAdapter);
        zone.setOnItemSelectedListener(new SanatoriumSearchActivity.ZoneItemSelectedListener());
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

    class ZoneItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            zoneVal = Optional.of(position)
                    .filter(p -> p > 0)
                    .map(parent::getItemAtPosition)
                    .map(Zone.class::cast)
                    .orElse(Zone.DEFAULT_ZONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            zoneVal = Zone.DEFAULT_ZONE;
        }
    }

    void searchSanatoriums(View view) {
        SanatoriumSearchForm form =
                new SanatoriumSearchForm(specialtyVal, zoneVal, name.getText().toString(), member.getPlan());
//        Toast.makeText(this, form.toString(), Toast.LENGTH_LONG).show();

        CrmBeanFactory.INSTANCE.getBean(SearchSanatoriums.class)
                .searchSanatoriums(form, this::handleSanatoriums, this::handleSearchError);
    }

    private void handleSanatoriums(List<Sanatorium> sanatoriums) {
        if (sanatoriums.isEmpty()) {
            Toast.makeText(this, R.string.san_search_empty_results, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, SanatoriumListActivity.class);
            intent.putExtra(SanatoriumListActivity.SANATORIUMS_KEY, (Serializable) sanatoriums);
            startActivity(intent);
        }
    }

    private void handleSearchError(Exception ex) {
        switch (ex.getMessage()) {
            case INVALID_FORM:
                Toast.makeText(this, R.string.san_search_no_filters, Toast.LENGTH_LONG).show();
                break;
            case INTERNAL_ERROR:
            case UNKNOWN_ERROR:
            default:
                DialogHelper.INSTANCE.showNonCancelableDialog(
                        this,
                        getString(R.string.san_search_err_dialog_title),
                        getString(R.string.san_search_err_dialog_msg)
                );
        }
    }
}
