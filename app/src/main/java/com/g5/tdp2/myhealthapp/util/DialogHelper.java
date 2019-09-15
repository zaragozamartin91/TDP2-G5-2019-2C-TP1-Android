package com.g5.tdp2.myhealthapp.util;

import android.app.AlertDialog;
import android.content.Context;

import com.g5.tdp2.myhealthapp.R;

public enum DialogHelper {
    INSTANCE;

    /**
     * Muestra un cuadro de dialogo no cancelable
     *
     * @param context Contexto (tipicamente la Activity que muestra el cuadro)
     * @param title   Titulo
     * @param msg     Mensaje
     */
    public void showNonCancelableDialog(Context context, String title, String msg) {
        showNonCancelableDialogWaction(context, title, msg, () -> {});
    }

    /**
     * Muestra un cuadro de dialogo no cancelable
     *
     * @param context Contexto (tipicamente la Activity que muestra el cuadro)
     * @param title   Titulo
     * @param msg     Mensaje
     * @param action  Accion a ejecutar al cerrar el cuadro de dialogo
     */
    public void showNonCancelableDialogWaction(Context context, String title, String msg, Runnable action) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.std_close, (dialog, which) -> action.run())
                .setCancelable(false)
                .create()
                .show();
    }
}
