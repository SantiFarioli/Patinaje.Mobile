package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.DashboardSummary;
import com.santisoft.patinajemobile.data.model.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeApi {

    @GET("dashboard/summary")
    Call<DashboardSummary> getSummary();

    @GET("eventos")
    Call<List<Evento>> getEventos(@Query("limit") int limit);
}
