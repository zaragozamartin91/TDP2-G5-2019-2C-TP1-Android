package com.g5.tdp2.myhealthapp.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.g5.tdp2.myhealthapp.R;
import com.g5.tdp2.myhealthapp.ui.LoginActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
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

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + data);
            String message = Optional.ofNullable(data.get("message"))
                    .map(s -> s.replaceAll(" +", " "))
                    .map(s -> s.replace('\n', '\0'))
                    .orElse("");
            sendNotification(data.get("title"), message);
        }

        // Check if message contains a notification payload.
        Optional.ofNullable(remoteMessage.getNotification())
                .map(RemoteMessage.Notification::getBody).ifPresent(body -> {
            Log.d(TAG, "Message Notification Body: " + body);
            sendNotification("Notificacion", body);
        });
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
//        WorkManager.getInstance().beginWith(work).enqueue();
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        /* CODIGO PARA ABRIR UNA NUEVA PANTALLA AL CLICKEAR UNA NOTIFICACION */
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, new Intent(), PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.md_check)
                        .setContentTitle(title)
//                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "MyHealthApp channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
