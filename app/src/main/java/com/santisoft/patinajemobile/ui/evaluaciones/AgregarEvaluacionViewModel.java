package com.santisoft.patinajemobile.ui.evaluaciones;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.data.repository.EvaluacionesRepository;
import com.santisoft.patinajemobile.util.DialogEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarEvaluacionViewModel extends AndroidViewModel {

    private final EvaluacionesRepository repo;
    private final MutableLiveData<Uri> uriPdf = new MutableLiveData<>();
    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();

    public AgregarEvaluacionViewModel(@NonNull Application application) {
        super(application);
        repo = new EvaluacionesRepository(application);
    }

    public LiveData<Uri> getUriPdf() { return uriPdf; }
    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }

    // Llamado cuando el usuario elige un archivo
    public void seleccionarArchivo(Uri uri) {
        uriPdf.setValue(uri);
    }

    public void guardar(int patinadorId, int torneoId, Date fecha, String observaciones) {
        // 1. Validaciones
        if (patinadorId <= 0 || torneoId <= 0) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error", "Faltan datos del patinador o torneo."));
            return;
        }
        if (uriPdf.getValue() == null) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.WARNING, "Falta PDF", "Debes adjuntar el PDF de la evaluación."));
            return;
        }

        dialogEvent.setValue(new DialogEvent(DialogEvent.Type.LOADING, "Subiendo evaluación...", null));

        try {
            // 2. Leer bytes del PDF
            byte[] pdfBytes = leerBytesDesdeUri(uriPdf.getValue());
            String fechaStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(fecha);

            // 3. Llamar al Repo
            repo.subirEvaluacion(patinadorId, torneoId, fechaStr, observaciones, pdfBytes)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                            if (response.isSuccessful()) {
                                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS, "Éxito", "Evaluación cargada correctamente."));
                            } else {
                                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error", "Código: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Fallo de red", t.getMessage()));
                        }
                    });

        } catch (IOException e) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error archivo", "No se pudo leer el PDF."));
        }
    }

    // Helper para leer el archivo como bytes (Estilo Inmobiliaria)
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