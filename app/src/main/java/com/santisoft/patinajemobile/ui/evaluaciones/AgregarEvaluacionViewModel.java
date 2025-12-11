package com.santisoft.patinajemobile.ui.evaluaciones;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.data.model.torneos.Torneo;
import com.santisoft.patinajemobile.data.remote.PagedResponse;
import com.santisoft.patinajemobile.data.repository.EvaluacionesRepository;
import com.santisoft.patinajemobile.util.DialogEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarEvaluacionViewModel extends AndroidViewModel {

    private final EvaluacionesRepository repo;

    // Listas para los Spinners
    private final MutableLiveData<List<Torneo>> torneos = new MutableLiveData<>();
    private final MutableLiveData<List<PatinadoraListItem>> patinadoras = new MutableLiveData<>();

    private final MutableLiveData<Uri> uriPdf = new MutableLiveData<>();
    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();

    public AgregarEvaluacionViewModel(@NonNull Application application) {
        super(application);
        repo = new EvaluacionesRepository(application);
    }

    public LiveData<List<Torneo>> getTorneos() { return torneos; }
    public LiveData<List<PatinadoraListItem>> getPatinadoras() { return patinadoras; }
    public LiveData<Uri> getUriPdf() { return uriPdf; }
    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }

    public void seleccionarArchivo(Uri uri) {
        uriPdf.setValue(uri);
    }

    public void cargarDatos() {
        // Cargar Torneos
        repo.getTorneos().enqueue(new Callback<List<Torneo>>() {
            @Override
            public void onResponse(Call<List<Torneo>> call, Response<List<Torneo>> response) {
                if (response.isSuccessful()) {
                    torneos.setValue(response.body());
                }
            }
            @Override public void onFailure(Call<List<Torneo>> call, Throwable t) {}
        });

        // Cargar Patinadoras
        repo.getPatinadoras().enqueue(new Callback<PagedResponse<PatinadoraListItem>>() {
            @Override
            public void onResponse(Call<PagedResponse<PatinadoraListItem>> call, Response<PagedResponse<PatinadoraListItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    patinadoras.setValue(response.body().data);
                }
            }
            @Override public void onFailure(Call<PagedResponse<PatinadoraListItem>> call, Throwable t) {}
        });
    }

    public void guardar(int patinadorId, int torneoId, Date fecha, String obs) {
        if (patinadorId <= 0 || torneoId <= 0) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.WARNING, "Faltan datos", "Selecciona patinadora y torneo."));
            return;
        }
        if (uriPdf.getValue() == null) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.WARNING, "Falta PDF", "Debes adjuntar el PDF."));
            return;
        }

        dialogEvent.setValue(new DialogEvent(DialogEvent.Type.LOADING, "Subiendo...", null));

        try {
            byte[] pdfBytes = leerBytesDesdeUri(uriPdf.getValue());
            // Formato ISO seguro para .NET
            String fechaStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(fecha);

            repo.subirEvaluacion(patinadorId, torneoId, fechaStr, obs, pdfBytes)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                            if (response.isSuccessful()) {
                                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS, "Éxito", "Evaluación guardada correctamente."));
                            } else {
                                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error", "Código servidor: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error de red", t.getMessage()));
                        }
                    });

        } catch (IOException e) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error archivo", "No se pudo leer el PDF."));
        }
    }

    private byte[] leerBytesDesdeUri(Uri uri) throws IOException {
        InputStream iStream = getApplication().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}