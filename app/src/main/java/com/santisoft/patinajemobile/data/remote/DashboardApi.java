package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento; // O crea EventoDashboard en model/dashboard

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DashboardApi {
    @GET("api/dashboard/summary")
    Call<DashboardSummary> getSummary();

    @GET("api/dashboard/eventos")
    Call<List<Evento>> getProximosEventos();
}