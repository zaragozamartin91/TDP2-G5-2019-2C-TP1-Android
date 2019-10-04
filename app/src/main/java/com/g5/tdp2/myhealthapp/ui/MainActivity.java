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
import com.g5.tdp2.myhealthapp.gateway.impl.WebZoneGateway;
import com.g5.tdp2.myhealthapp.service.LoginMemberStub;
import com.g5.tdp2.myhealthapp.service.SearchProfessionalsStub;
import com.g5.tdp2.myhealthapp.service.SignupMemberStub;
import com.g5.tdp2.myhealthapp.service.WebLoginMember;
import com.g5.tdp2.myhealthapp.service.WebSignupMember;
import com.g5.tdp2.myhealthapp.usecase.UsecaseFactory;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

public abstract class MainActivity extends AppCompatActivity {
    private static final Integer PERMS_REQUEST_CODE = 1307;
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    private static final String[] REQUIRED_PERMISSIONS = {
            INTERNET, ACCESS_NETWORK_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    };


    private void checkPermissions() {
        String[] missingPermissions = Arrays.stream(REQUIRED_PERMISSIONS)
                .filter(p -> checkSelfPermission(p) == PERMISSION_DENIED)
                .toArray(String[]::new);

        Optional.of(Arrays.asList(missingPermissions))
                .filter(mp -> !mp.isEmpty())
                .ifPresent(mp -> requestPermissions(missingPermissions, PERMS_REQUEST_CODE));
    }

    private Map<String, Integer> permissionMsgMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionMsgMap.put(INTERNET, R.string.internet_perm_reject);
        permissionMsgMap.put(ACCESS_NETWORK_STATE, R.string.network_perm_reject);
        permissionMsgMap.put(ACCESS_FINE_LOCATION, R.string.location_perm_reject);

        checkPermissions();

        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Optional.of(requestCode).filter(PERMS_REQUEST_CODE::equals).ifPresent(i -> {
            String rejectionMsg = IntStream.range(0, grantResults.length)
                    .filter(ii -> grantResults[ii] == PERMISSION_DENIED)
                    .mapToObj(ii -> REQUIRED_PERMISSIONS[ii])
                    .map(permissionMsgMap::get)
                    .map(this::getString)
                    .distinct()
                    .collect(Collectors.joining("\n"));

            Optional.of(rejectionMsg).filter(StringUtils::isNotBlank).ifPresent(
                    s -> DialogHelper.INSTANCE.showNonCancelableDialogWaction(
                            this, getString(R.string.premissions_req), s, this::closeApp
                    )
            );
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
            webLoginMember.setTokenConsumer(AppState.INSTANCE::putToken);
            UsecaseFactory.INSTANCE.addBean(webLoginMember);

            WebSignupMember signupMember = new WebSignupMember("http://" + value + "/auth/register", requestQueue);
            UsecaseFactory.INSTANCE.addBean(signupMember);
        });

        alert.setNegativeButton("Usar Heroku", (dialog, whichButton) -> {
            WebLoginMember webLoginMember = new WebLoginMember(apiBaseUrl + "/auth/login", requestQueue);
            webLoginMember.setTokenConsumer(AppState.INSTANCE::putToken);
            UsecaseFactory.INSTANCE.addBean(webLoginMember);

            WebSignupMember signupMember = new WebSignupMember(apiBaseUrl + "/auth/register", requestQueue);
            UsecaseFactory.INSTANCE.addBean(signupMember);

            dialog.cancel();
        });

        alert.setNeutralButton("Modo stub", (dialog, which) -> {
            UsecaseFactory.INSTANCE.addBean(new LoginMemberStub());
            UsecaseFactory.INSTANCE.addBean(new SignupMemberStub());
            dialog.cancel();
        });

        // TODO : Modificar esta linea dependiendo del ambiente
        UsecaseFactory.INSTANCE.addBean(new SearchProfessionalsStub());

        UsecaseFactory.INSTANCE.addBean(new WebZoneGateway(apiBaseUrl + "/zones", requestQueue));

        alert.setTitle("Configuracion de API");
        alert.show();
    }
}
