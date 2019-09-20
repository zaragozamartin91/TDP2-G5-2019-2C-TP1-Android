package com.g5.tdp2.myhealthapp.ui;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.g5.tdp2.myhealthapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ProfessionalInfoWindow implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;

    public ProfessionalInfoWindow(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        View v = inflater.inflate(R.layout.info_prof_window, null);
        String[] info = m.getTitle().split(Pattern.quote("&"));

        Log.d("ProfessionalInfoWindow-getInfoContents", Arrays.toString(info));

        //"Pepe Argento" + "&" + "Calle falsa 123" + "&" + "Oncologia" + "&1533246698"
        String name, address, special, phone;
        int idx = 0;
        name = info[idx++];
        address = info[idx++];
        special = info[idx++];
        phone = info.length >= 4 ? info[idx] : "";


        ((TextView) v.findViewById(R.id.info_prof_window_addr)).setText(address);
        ((TextView) v.findViewById(R.id.info_prof_window_name)).setText(name);
        ((TextView) v.findViewById(R.id.info_prof_window_phone)).setText(phone);
        ((TextView) v.findViewById(R.id.info_prof_window_special)).setText(special);
        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}
