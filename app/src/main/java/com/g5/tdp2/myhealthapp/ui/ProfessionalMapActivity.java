package com.g5.tdp2.myhealthapp.ui;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.Member;
import com.g5.tdp2.myhealthapp.entity.Office;
import com.g5.tdp2.myhealthapp.entity.Place;
import com.g5.tdp2.myhealthapp.entity.ProfessionalSearchForm;
import com.g5.tdp2.myhealthapp.entity.ProfessionalWdistForm;
import com.g5.tdp2.myhealthapp.entity.Provider;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionals;
import com.g5.tdp2.myhealthapp.util.DialogHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static android.location.LocationManager.*;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.INTERNAL_ERROR;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.INVALID_FORM;
import static com.g5.tdp2.myhealthapp.usecase.Usecase.UNKNOWN_ERROR;

//Antes extendia de FragmentActivity pero lo cambie para agregar el titulo en la vista
public class ProfessionalMapActivity extends ActivityWnavigation implements OnMapReadyCallback, LocationListener {
    private static final double DEF_RADIO = -1d;

    private GoogleMap mMap;
    private AtomicReference<Location> currentLocation = new AtomicReference<>();
    private Specialty specialtyVal;
    private double radioVal = DEF_RADIO;
    private Member member;


    @Override
    protected String actionBarTitle() {
        return "Profesionales";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professional_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Optional.ofNullable(getSupportFragmentManager())
                .map(f -> f.findFragmentById(R.id.map))
                .map(SupportMapFragment.class::cast)
                .ifPresent(m -> m.getMapAsync(this));

        setupDistances();

        setupSpecialties();

        findViewById(R.id.prof_map_search).setOnClickListener(this::searchProfessionals);

        member = (Member) getIntent().getSerializableExtra(MEMBER_EXTRA);
    }


    private void searchProfessionals(View v) {
        if (currentLocation.get() == null) {
            Toast.makeText(this, "Ubicacion desconocida", Toast.LENGTH_SHORT).show();
            return;
        }

        Place p = new Place() {
            public double getLat() {
                return currentLocation.get().getLatitude();
            }

            public double getLon() {
                return currentLocation.get().getLongitude();
            }
        };

        v.setEnabled(false);
        SearchProfessionals usecase = CrmBeanFactory.INSTANCE.getBean(SearchProfessionals.class);
        ProfessionalWdistForm form = new ProfessionalWdistForm(specialtyVal, member.getPlan(), radioVal, p);
        usecase.searchProfessionals(form, profs -> {
            v.setEnabled(true);
            profs.forEach(this::addMarker);
            if (profs.isEmpty()) {
                Toast.makeText(this, R.string.prof_map_no_results, Toast.LENGTH_SHORT).show();
            }
        }, err -> {
            v.setEnabled(true);
            switch (err.getMessage()) {
                case INVALID_FORM:
                    Toast.makeText(this, R.string.prof_map_no_filters, Toast.LENGTH_SHORT).show();
                    break;
                case UNKNOWN_ERROR:
                case INTERNAL_ERROR:
                default:
                    DialogHelper.INSTANCE.showNonCancelableDialog(
                            this,
                            getString(R.string.prof_map_dialog_err_title),
                            getString(R.string.prof_map_dialog_err_msg));
            }
        });
    }

    private void addMarker(Provider provider) {
        Optional.ofNullable(mMap).ifPresent(m -> {
            Office mainOffice = provider.getMainOffice();
            LatLng latLngLocation = new LatLng(mainOffice.getLat(), mainOffice.getLon());
            m.addMarker(
                    new MarkerOptions().position(latLngLocation).title(provider.zip())
            );
        });
    }

    private void setupDistances() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.map_distances,
                R.layout.crm_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner radio = findViewById(R.id.prof_map_radio);
        radio.setAdapter(adapter);
        radio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                radioVal = Optional.of(position)
                        .filter(p -> p > 0)
                        .map(parent::getItemAtPosition)
                        .map(String.class::cast)
                        .map(Double::valueOf)
                        .map(d -> d * 1000)
                        .orElse(DEF_RADIO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                radioVal = DEF_RADIO;
            }
        });
    }

    private void setupSpecialties() {
        Spinner specialty = findViewById(R.id.prof_map_specialty);
        List<Specialty> values = AppState.INSTANCE.getSpecialties();
        ArrayAdapter<Specialty> adapter = new ArrayAdapter<>(this, R.layout.crm_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialty.setAdapter(adapter);
        specialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });
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
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}

