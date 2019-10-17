package com.g5.tdp2.myhealthapp.util;

import android.location.Location;

public enum LocationManager {
    INSTANCE;

    /**
     * Crea una nueva instancia de {@link Location}
     *
     * @param myLat Latitud
     * @param myLon Longitud
     * @return nueva instancia de {@link Location}
     */
    public Location newLocation(double myLat, double myLon) {
        Location myLocation = new Location("");
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLon);
        return myLocation;
    }

    /**
     * Calcula la distancia en metros entre my ubicacion y un destino
     *
     * @param myLat  Mi latitud
     * @param myLon  Mi longitud
     * @param tgtLat Latitud destino
     * @param tgtLon Longitud destino
     * @return Distancia en metros calculada
     */
    public double distanceMts(double myLat, double myLon, double tgtLat, double tgtLon) {
        Location myLocation = newLocation(myLat, myLon);
        Location targetLocation = newLocation(tgtLat, tgtLon);

        return targetLocation.distanceTo(myLocation);
    }
}
