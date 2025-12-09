package com.santisoft.patinajemobile.data.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface EvaluacionesApi {

    @Multipart
    @POST("api/evaluacionestorneos")
    Call<Void> crearEvaluacion(
            @Part("PatinadorId") RequestBody patinadorId,
            @Part("TorneoId") RequestBody torneoId,
            @Part("Fecha") RequestBody fecha,
            @Part("Observaciones") RequestBody observaciones,
            @Part MultipartBody.Part archivoPdf
    );
}