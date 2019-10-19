package com.g5.tdp2.myhealthapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.g5.tdp2.myhealthapp.AppState;
import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.entity.SimpleEntity;
import com.g5.tdp2.myhealthapp.entity.Specialty;
import com.g5.tdp2.myhealthapp.entity.Zone;
import com.g5.tdp2.myhealthapp.gateway.SpecialtyGateway;
import com.g5.tdp2.myhealthapp.gateway.ZoneGateway;
import com.g5.tdp2.myhealthapp.gateway.impl.WebSpecialtyGateway;
import com.g5.tdp2.myhealthapp.gateway.impl.WebZoneGateway;
import com.g5.tdp2.myhealthapp.service.GetChecksStub;
import com.g5.tdp2.myhealthapp.service.LoginMemberStub;
import com.g5.tdp2.myhealthapp.service.SearchProfessionalsStub;
import com.g5.tdp2.myhealthapp.service.SignupMemberStub;
import com.g5.tdp2.myhealthapp.service.WebLoginMember;
import com.g5.tdp2.myhealthapp.service.WebSearchProfessionals;
import com.g5.tdp2.myhealthapp.service.WebSignupMember;
import com.g5.tdp2.myhealthapp.CrmBeanFactory;
import com.g5.tdp2.myhealthapp.usecase.SearchProfessionals;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static com.g5.tdp2.myhealthapp.ui.UiReqCode.PERMS_REQUEST_CODE;

public abstract class MainActivity extends AppCompatActivity {
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    private static final String[] REQUIRED_PERMISSIONS = {
            INTERNET, ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    };


    private Map<String, Integer> permissionMsgMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionMsgMap.put(INTERNET, R.string.internet_perm_reject);
        permissionMsgMap.put(ACCESS_NETWORK_STATE, R.string.network_perm_reject);
        permissionMsgMap.put(ACCESS_FINE_LOCATION, R.string.location_perm_reject);

        checkPermissions();
    }

    private void checkPermissions() {
        String[] missingPermissions = Arrays.stream(REQUIRED_PERMISSIONS)
                .filter(p -> checkSelfPermission(p) == PERMISSION_DENIED)
                .toArray(String[]::new);

        boolean permissionsGranted = missingPermissions.length == 0;
        if (permissionsGranted) {
            initialize();
        } else {
            requestPermissions(missingPermissions, PERMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Optional.of(requestCode).filter(PERMS_REQUEST_CODE::equals).ifPresent(i -> {
            boolean permissionsGranted = Arrays.stream(grantResults).noneMatch(gr -> gr == PERMISSION_DENIED);
            if (permissionsGranted) {
                initialize();
            } else {
                String rejectionMsg = IntStream.range(0, grantResults.length)
                        .filter(ii -> grantResults[ii] == PERMISSION_DENIED)
                        .mapToObj(ii -> REQUIRED_PERMISSIONS[ii])
                        .map(permissionMsgMap::get)
                        .map(this::getString)
                        .distinct()
                        .collect(Collectors.joining("\n"));
                DialogHelper.INSTANCE.showNonCancelableDialogWaction(
                        this, getString(R.string.premissions_req), rejectionMsg, this::closeApp
                );
            }
        });
    }

    private void closeApp() {
        finish();
        System.exit(0);
    }

    /**
     * Inicializa los beans de la app y configura la IP del API al iniciar la misma
     */
    private void initialize() {
        if (initialized.compareAndSet(false, true)) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            showConfigDialog(requestQueue);
        }
    }

    private void showConfigDialog(RequestQueue requestQueue) {
        Context appContext = this;
        final AlertDialog.Builder alert = new AlertDialog.Builder(appContext);
        final EditText input = new EditText(appContext);
        input.setHint("<host>:<puerto>");
        alert.setView(input);

        alert.setCancelable(false);

        String apiBaseUrl = appContext.getString(R.string.api_base_url);

        alert.setPositiveButton("Usar IP", (dialog, whichButton) -> {
            String value = input.getText().toString().trim();

            if (StringUtils.isBlank(value)) {
                Toast.makeText(this, "Ip invalida", Toast.LENGTH_SHORT).show();
                showConfigDialog(requestQueue);
                return;
            }

            WebLoginMember webLoginMember = new WebLoginMember("http://" + value + "/auth/login", requestQueue);
            CrmBeanFactory.INSTANCE.addBean(webLoginMember);

            CrmBeanFactory.INSTANCE.addBean(new WebSignupMember("http://" + value + "/auth/register", requestQueue));
            CrmBeanFactory.INSTANCE.addBean(new WebSearchProfessionals("http://" + value + "/lenders", requestQueue));
        });

        alert.setNegativeButton("Usar Heroku", (dialog, whichButton) -> {
            WebLoginMember webLoginMember = new WebLoginMember(apiBaseUrl + "/auth/login", requestQueue);
            CrmBeanFactory.INSTANCE.addBean(webLoginMember);

            CrmBeanFactory.INSTANCE.addBean(new WebSignupMember(apiBaseUrl + "/auth/register", requestQueue));
            CrmBeanFactory.INSTANCE.addBean(new WebSearchProfessionals(apiBaseUrl + "/lenders", requestQueue));

            dialog.cancel();
        });

        alert.setNeutralButton("Modo stub", (dialog, which) -> {
            CrmBeanFactory.INSTANCE.addBean(new LoginMemberStub());
            CrmBeanFactory.INSTANCE.addBean(new SignupMemberStub());
            CrmBeanFactory.INSTANCE.addBean(new SearchProfessionalsStub());
            dialog.cancel();
        });

        // TODO : eliminar este bean en cuanto se desarrolle el "posta" que va contra el API
        CrmBeanFactory.INSTANCE.addBean(new GetChecksStub());

        setupZones(new WebZoneGateway(apiBaseUrl + "/zones", requestQueue));
        setupSpecialties(new WebSpecialtyGateway(apiBaseUrl + "/specialties", requestQueue));

        alert.setTitle("Configuracion de API");
        alert.show();
    }

    private void setupZones(ZoneGateway zoneGateway) {
        CrmBeanFactory.INSTANCE.addBean(zoneGateway);

        zoneGateway.getZones(zones -> {
            List<Zone> values = Stream.of(Collections.singletonList(Zone.DEFAULT_ZONE), zones)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            AppState.INSTANCE.put(AppState.ZONES_KEY, values);
            Toast.makeText(this, "Localidades cargadas", Toast.LENGTH_SHORT).show();
        }, e -> {
            Toast.makeText(this, "Ocurrio un error al obtener las localidades. Cargando valores por defecto", Toast.LENGTH_LONG).show();
            // ANTE UN ERROR SE CARGAN LAS ZONAS 'POR DEFECTO'
            String[] values = getResources().getStringArray(R.array.available_zones);
            List<String> ss = Arrays.asList(values);
            AppState.INSTANCE.put(AppState.ZONES_KEY, SimpleEntity.fromNames(ss, Zone.class));
        });
    }

    private void setupSpecialties(SpecialtyGateway specialtyGateway) {
        CrmBeanFactory.INSTANCE.addBean(specialtyGateway);

        specialtyGateway.getSpecialties(specialties -> {
            List<Specialty> values = Stream.of(Collections.singletonList(Specialty.DEFAULT_SPECIALTY), specialties)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            AppState.INSTANCE.put(AppState.SPECIALTIES_KEY, values);
            Toast.makeText(this, "Especialidades cargadas", Toast.LENGTH_SHORT).show();
        }, e -> {
            Toast.makeText(this, "Ocurrio un error al obtener las especialidades. Cargando valores por defecto", Toast.LENGTH_LONG).show();
            String[] values = getResources().getStringArray(R.array.available_specialties);
            List<String> ss = Arrays.asList(values);
            AppState.INSTANCE.put(AppState.SPECIALTIES_KEY, SimpleEntity.fromNames(ss, Specialty.class));
        });
    }
}
