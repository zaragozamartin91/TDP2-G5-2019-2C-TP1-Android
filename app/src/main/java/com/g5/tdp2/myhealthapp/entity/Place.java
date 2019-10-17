package com.g5.tdp2.myhealthapp.entity;

import java.util.function.Function;

/**
 * Representa un lugar o posicion con latitud y longitud en el mapa
 */
public interface Place {
    double getLat();

    double getLon();

    default double distanceMts(Place otherPlace, Function<Place, Function<Place, Double>> distanceCalc) {
        return distanceCalc.apply(this).apply(otherPlace);
    }
}
