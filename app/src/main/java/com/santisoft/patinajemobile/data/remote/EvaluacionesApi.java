package com.santisoft.patinajemobile.data.remote;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import com.santisoft.patinajemobile.data.model.torneos.Torneo; // Asegurate de este import

public interface EvaluacionesApi {

    @GET("api/torneos") // Asegurate que esta ruta sea real en tu controller de torneos
    Call<List<Torneo>> getTorneos();

    // 👇 OJO AQUÍ: La ruta debe coincidir con tu Controller.
    // Si tu Controller se llama EvaluacionesTorneosController, probablemente la ruta sea "api/evaluacionestorneos"
    @Multipart
    @POST("api/evaluacionestorneos")
    Call<Void> crearEvaluacion(
            @Part("PatinadorId") RequestBody patinadorId,   // Mayúscula como en C#
            @Part("TorneoId") RequestBody torneoId,         // Mayúscula como en C#
            @Part("Fecha") RequestBody fecha,               // Mayúscula como en C#
            @Part("Observaciones") RequestBody observaciones, // Mayúscula como en C#
            @Part MultipartBody.Part archivoPdf             // El nombre interno se define en el Repo
    );
}