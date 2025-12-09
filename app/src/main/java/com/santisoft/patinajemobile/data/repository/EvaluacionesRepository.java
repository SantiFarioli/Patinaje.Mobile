package com.santisoft.patinajemobile.data.repository;

import android.content.Context;
import com.santisoft.patinajemobile.data.remote.EvaluacionesApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EvaluacionesRepository {

    private final EvaluacionesApi api;

    public EvaluacionesRepository(Context ctx) {
        api = RetrofitClient.get(ctx).evaluacionesApi();
    }

    public Call<Void> subirEvaluacion(int patinadorId, int torneoId, String fecha, String obs, byte[] pdfBytes) {

        // 1. Convertir datos a RequestBody (text/plain)
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(patinadorId));
        RequestBody rTorneo = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(torneoId));
        RequestBody rFecha = RequestBody.create(MediaType.parse("text/plain"), fecha); // Formato: yyyy-MM-dd
        RequestBody rObs = RequestBody.create(MediaType.parse("text/plain"), obs != null ? obs : "");

        // 2. Preparar el archivo (application/pdf)
        MultipartBody.Part bodyPdf = null;
        if (pdfBytes != null && pdfBytes.length > 0) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), pdfBytes);
            // "ArchivoPdf" debe coincidir con el nombre en el DTO del Backend (IFormFile ArchivoPdf)
            bodyPdf = MultipartBody.Part.createFormData("ArchivoPdf", "evaluacion.pdf", requestFile);
        }

        return api.crearEvaluacion(rId, rTorneo, rFecha, rObs, bodyPdf);
    }
}