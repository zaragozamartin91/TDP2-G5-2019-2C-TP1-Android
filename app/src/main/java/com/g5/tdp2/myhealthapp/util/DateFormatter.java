package com.g5.tdp2.myhealthapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public enum DateFormatter {
    YYYY_MM_DD {
        @Override
        public String serialize(Date date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return simpleDateFormat.format(date);
        }

        @Override
        public Date deserialize(String sdate) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                return simpleDateFormat.parse(sdate);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Formato de fecha invalido");
            }
        }
    };


    public abstract String serialize(Date date);

    public abstract Date deserialize(String sdate);
}
