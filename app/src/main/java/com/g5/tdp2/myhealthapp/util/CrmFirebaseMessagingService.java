package com.g5.tdp2.myhealthapp.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Optional;
import java.util.function.Consumer;

public class CrmFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = CrmFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(getClass().getSimpleName(), "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO : enviar token al API
    }

    public static void getCurrToken(Consumer<String> tokenConsumer, Consumer<Exception> errHandler) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> Optional.of(task)
                .filter(Task::isSuccessful)
                .map(Task::getResult)
                .map(InstanceIdResult::getToken)
                .map(token -> (Runnable) () -> tokenConsumer.accept(token))
                .orElse(() -> {
                    Log.w(TAG, "Error al obtener token", task.getException());
                    errHandler.accept(task.getException());
                }).run());
    }
}
