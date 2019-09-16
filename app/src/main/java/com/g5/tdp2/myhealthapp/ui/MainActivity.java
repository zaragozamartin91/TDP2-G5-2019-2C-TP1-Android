package com.g5.tdp2.myhealthapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.util.DialogHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

public abstract class MainActivity extends AppCompatActivity {
    private static final Integer PERMS_REQUEST_CODE = 1307;

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Optional.of(requestCode).filter(PERMS_REQUEST_CODE::equals).ifPresent(i -> {
            String rejectionMsg = IntStream.range(0, grantResults.length)
                    .filter(ii -> grantResults[ii] == PERMISSION_DENIED)
                    .mapToObj(ii -> permissions[ii])
                    .map(permissionMsgMap::get)
                    .map(this::getString)
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
}
