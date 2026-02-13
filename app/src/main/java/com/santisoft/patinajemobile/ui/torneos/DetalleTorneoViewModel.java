package com.santisoft.patinajemobile.ui.torneos;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.eventos.Evento;

public class DetalleTorneoViewModel extends AndroidViewModel {

    private final MutableLiveData<Evento> evento = new MutableLiveData<>();
    private final MutableLiveData<Intent> mapIntent = new MutableLiveData<>();

    public DetalleTorneoViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEvento(Evento e) {
        this.evento.setValue(e);
    }

    public LiveData<Evento> getEvento() {
        return evento;
    }

    public LiveData<Intent> getMapIntent() {
        return mapIntent;
    }

    public void abrirMapa() {
        Evento e = evento.getValue();
        if (e != null) {
            // Usar coordenadas si existen, sino usar nombre del lugar
            Uri gmmIntentUri;
            if (e.latitud != null && e.longitud != null) {
                gmmIntentUri = Uri.parse("geo:" + e.latitud + "," + e.longitud + "?q=" + e.latitud + "," + e.longitud
                        + "(" + Uri.encode(e.lugar) + ")");
            } else {
                gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(e.lugar));
            }

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            this.mapIntent.setValue(mapIntent);
        }
    }

    // Resetear el evento de navegación para evitar múltiples aperturas
    public void onMapOpened() {
        this.mapIntent.setValue(null);
    }

    public int getImagenTorneo() {
        Evento e = evento.getValue();
        if (e != null) {
            return com.santisoft.patinajemobile.ui.common.ImageMapper.getTorneoImage(e.lugar);
        }
        return com.santisoft.patinajemobile.R.drawable.poli_ejemplo;
    }

    public String getDireccionTorneo() {
        Evento e = evento.getValue();
        if (e != null) {
            // Aseguramos que el objeto e tenga la dirección asginada
            com.santisoft.patinajemobile.ui.common.ImageMapper.asignarDireccion(e);
            return e.direccion;
        }
        return "Dirección no disponible";
    }
}
