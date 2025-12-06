package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.asistencias.AsistenciaPlanillaItem;
import com.santisoft.patinajemobile.data.model.asistencias.RegistrarAsistenciaRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AsistenciasApi {

    @GET("api/asistencias/planilla")
    Call<List<AsistenciaPlanillaItem>> getPlanilla(@Query("fecha") String fecha);

    @POST("api/asistencias")
    Call<Void> registrar(@Body RegistrarAsistenciaRequest body);
}