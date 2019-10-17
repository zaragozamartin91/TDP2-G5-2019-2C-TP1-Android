package com.g5.tdp2.myhealthapp.ui;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Provider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ProfessionalInfoWindow implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;

    public ProfessionalInfoWindow(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        View v = inflater.inflate(R.layout.info_prof_window, null);
        Provider p = Provider.unzip(m.getTitle());

        Log.d("ProfessionalInfoWindow-getInfoContents", p.toString());

        //"Pepe Argento" + "&" + "Calle falsa 123" + "&" + "Oncologia" + "&1533246698"

        ((TextView) v.findViewById(R.id.info_prof_window_name)).setText(p.getName());
        ((TextView) v.findViewById(R.id.info_prof_window_special)).setText(Provider.formatField(p.getSpecialties()));
        ((TextView) v.findViewById(R.id.info_prof_window_addrWphone)).setText(p.getMainOffice().addressWphone());
        ((TextView) v.findViewById(R.id.info_prof_window_plan)).setText(p.getPlan());
        ((TextView) v.findViewById(R.id.info_prof_window_email)).setText(Provider.formatField(p.getEmails()));
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}
