package com.santisoft.patinajemobile.ui.asistencias;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.asistencias.AsistenciaPlanillaItem;
import com.santisoft.patinajemobile.data.model.asistencias.RegistrarAsistenciaRequest;
import com.santisoft.patinajemobile.data.remote.AsistenciasApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import com.santisoft.patinajemobile.util.DialogEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsistenciasViewModel extends AndroidViewModel {

    private final AsistenciasApi api;
    private final MutableLiveData<List<AsistenciaPlanillaItem>> planilla = new MutableLiveData<>();
    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();

    // Guardamos la fecha seleccionada (por defecto hoy)
    private Date fechaSeleccionada = new Date();

    public AsistenciasViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.get(application).retrofit().create(AsistenciasApi.class);
    }

    public LiveData<List<AsistenciaPlanillaItem>> getPlanilla() { return planilla; }
    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }

    public void cargarPlanilla(Date fecha) {
        this.fechaSeleccionada = fecha;
        String fechaStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(fecha);

        dialogEvent.setValue(new DialogEvent(DialogEvent.Type.LOADING, "Cargando...", null));

        api.getPlanilla(fechaStr).enqueue(new Callback<List<AsistenciaPlanillaItem>>() {
            @Override
            public void onResponse(Call<List<AsistenciaPlanillaItem>> call, Response<List<AsistenciaPlanillaItem>> response) {
                dialogEvent.setValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                if (response.isSuccessful() && response.body() != null) {
                    planilla.setValue(response.body());
                } else {
                    dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error", "No se pudo cargar la lista."));
                }
            }

            @Override
            public void onFailure(Call<List<AsistenciaPlanillaItem>> call, Throwable t) {
                dialogEvent.setValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error", t.getMessage()));
            }
        });
    }

    public void guardarCambios(List<AsistenciaPlanillaItem> items) {
        if (items == null || items.isEmpty()) return;

        dialogEvent.setValue(new DialogEvent(DialogEvent.Type.LOADING, "Guardando...", null));

        // Mapear a Request
        List<RegistrarAsistenciaRequest.Detalle> detalles = new ArrayList<>();
        for (AsistenciaPlanillaItem item : items) {
            detalles.add(new RegistrarAsistenciaRequest.Detalle(item.patinadorId, item.presente));
        }

        // ⚠️ CAMBIO CLAVE: Usamos formato ISO simple que .NET prefiere
        String fechaStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(fechaSeleccionada);
        // Ojo: Si el backend pide DateTime completo, usá "yyyy-MM-dd'T'HH:mm:ss"
        // Pero probemos simplificando primero si da error.

        // Vamos a loguear qué estamos mandando
        android.util.Log.d("ASISTENCIA_DEBUG", "Enviando fecha: " + fechaStr + " con " + detalles.size() + " items");

        RegistrarAsistenciaRequest request = new RegistrarAsistenciaRequest(fechaStr, detalles);

        api.registrar(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // PRIMERO cerramos el loading
                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));

                if (response.isSuccessful()) {
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS, "Guardado", "Asistencia registrada correctamente."));
                } else {
                    // Log del error real
                    android.util.Log.e("ASISTENCIA_DEBUG", "Error API: " + response.code() + " - " + response.message());
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error " + response.code(), "No se pudo guardar."));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // PRIMERO cerramos el loading
                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));

                android.util.Log.e("ASISTENCIA_DEBUG", "Fallo Red: " + t.getMessage());
                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error de conexión", t.getMessage()));
            }
        });
    }
}