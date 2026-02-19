package com.santisoft.patinajemobile.data.repository;

import android.content.Context;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.data.model.torneos.Torneo;
import com.santisoft.patinajemobile.data.remote.EvaluacionesApi;
import com.santisoft.patinajemobile.data.remote.PagedResponse;
import com.santisoft.patinajemobile.data.remote.PatinadorasApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class EvaluacionesRepository {

    private final EvaluacionesApi api;
    private final PatinadorasApi patinadorasApi;

    public EvaluacionesRepository(Context ctx) {
        RetrofitClient rc = RetrofitClient.get(ctx);
        this.api = rc.evaluacionesApi();
        this.patinadorasApi = rc.patinadorasApi();
    }

    public Call<List<Torneo>> getTorneos() {
        return api.getTorneos();
    }

    public Call<PagedResponse<PatinadoraListItem>> getPatinadoras() {
        // Reutilizamos la API de patinadoras para llenar el combo
        return patinadorasApi.getPatinadoras();
    }

    public Call<Void> subirEvaluacion(int patinadorId, int torneoId, String fecha, String obs, byte[] pdfBytes) {

        // 1. Textos planos (MediaType text/plain)
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(patinadorId));
        RequestBody rTorneo = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(torneoId));
        RequestBody rFecha = RequestBody.create(MediaType.parse("text/plain"), fecha);
        RequestBody rObs = RequestBody.create(MediaType.parse("text/plain"), obs != null ? obs : "");

        // 2. Archivo PDF
        MultipartBody.Part bodyPdf = null;
        if (pdfBytes != null && pdfBytes.length > 0) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), pdfBytes);

            // ⚠️ IMPORTANTE: El primer parámetro debe ser EXACTAMENTE "ArchivoPdf"
            // porque así se llama la propiedad en tu DTO de C#: public IFormFile? ArchivoPdf { get; set; }
            bodyPdf = MultipartBody.Part.createFormData("ArchivoPdf", "planilla_torneo.pdf", requestFile);
        }

        return api.crearEvaluacion(rId, rTorneo, rFecha, rObs, bodyPdf);
    }
}