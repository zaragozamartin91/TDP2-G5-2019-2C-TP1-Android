package com.g5.tdp2.myhealthapp.ui;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static android.location.LocationManager.*;
import static android.location.LocationManager.NETWORK_PROVIDER;

//Antes extendia de FragmentActivity pero lo cambie para agregar el titulo en la vista
public class ProfessionalMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private AtomicReference<Location> currentLocation = new AtomicReference<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_map);

        /* Habilito el default action bar */
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Profesionales");
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Optional.ofNullable(getSupportFragmentManager())
                .map(f -> f.findFragmentById(R.id.map))
                .map(SupportMapFragment.class::cast)
                .ifPresent(m -> m.getMapAsync(this));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(
                new ProfessionalInfoWindow(LayoutInflater.from(this))
        );
        setUpMap();
    }

    private void setUpMap() {
        // TODO : QUEDARSE CON UNO DE LOS LOCATION PROVIDERS... POSIBLEMENTE EL DE NETWORK
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            Optional<Location> location = Stream.of(
                    Optional.ofNullable(locationManager.getLastKnownLocation(NETWORK_PROVIDER)),
                    Optional.ofNullable(locationManager.getLastKnownLocation(GPS_PROVIDER)))
                    .filter(Optional::isPresent)
                    .findFirst()
                    .orElseGet(() -> {
                        Toast.makeText(this, getString(R.string.maps_err_noloc_msg), Toast.LENGTH_LONG).show();
                        Log.d("location-get", "NULL");
                        return Optional.empty();
                    });

            location.ifPresent(loc -> {
                Log.d("location-get", "NOT NULL");
                currentLocation.set(loc);
                centerAndMarkLocation(loc);
            });

            locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000, 5f, this);
            locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 5f, this);

            addStubMarker();
        } catch (SecurityException se) {
            Log.d("location-get", "SE CAUGHT");
            se.printStackTrace();
        }
    }

    private void centerAndMarkLocation(Location location) {
        Optional.ofNullable(mMap).ifPresent(m -> {
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLngLocation, 15);
            m.animateCamera(yourLocation);
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("CHANGED", "LOCATION UPDATED");
        if (currentLocation.getAndSet(location) == null) {
            Toast.makeText(this, getString(R.string.maps_loc_found_msg), Toast.LENGTH_LONG).show();
        }
        Optional.ofNullable(location).ifPresent(this::centerAndMarkLocation);
        addStubMarker();
    }

    Marker stubMarker;

    private void addStubMarker() {
        Optional.ofNullable(stubMarker).ifPresent(Marker::remove);

        Optional.ofNullable(currentLocation.get()).ifPresent(loc -> {
            double stubLat = loc.getLatitude() + 0.000274d;
            double stubLon = loc.getLongitude() - 0.003262d;

            Optional.ofNullable(mMap).ifPresent(m -> {
                LatLng latLngLocation = new LatLng(stubLat, stubLon);
                stubMarker = m.addMarker(new MarkerOptions().position(latLngLocation).title("Pepe Argento" + "&" + "Calle falsa 123" + "&" + "Oncologia" + "&1533246698"));
            });
        });
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
