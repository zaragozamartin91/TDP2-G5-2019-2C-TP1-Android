package com.g5.tdp2.myhealthapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g5.tdp2.myhealthapp.R;

import java.util.Optional;

/**
 * Pantalla con navegacion y un menu de contexto
 */
abstract public class ActivityWnavigation extends AppCompatActivity {
    public static final String MEMBER_EXTRA = "member";

    /**
     * Establece el titulo de la pantalla
     *
     * @return titulo de la pantalla
     */
    protected String actionBarTitle() { return "MyHealthApp"; }

    /**
     * Determina si esta actividad debe tener un menu de contexto
     *
     * @return true si esta actividad debe tener un menu de contexto, false en caso contrario.
     */
    protected boolean wMenu() { return false; }

    /**
     * Determina si esta actividad debe tener un boton "atras"
     *
     * @return true si esta actividad debe tener un boton "atras", false en caso contrario
     */
    protected boolean wBack() { return true; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Habilito el default action bar */
        Optional.ofNullable(getSupportActionBar()).ifPresent(actionBar -> {
            if (wBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
            actionBar.setTitle(actionBarTitle());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(true);
                break;
            case R.id.main_menu_logout:
                logout();
                break;
            default:
                Toast.makeText(this, "Opcion no implementada", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        /* Limpiamos el stack previo de actividades y volvemos al menu de inicio de sesion */
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (wMenu()) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public void onBackPressed() { onBackPressed(wBack()); }

    private void onBackPressed(boolean goBack) { if (goBack) super.onBackPressed(); }
}