package com.santisoft.patinajemobile.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.remote.DashboardApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import com.santisoft.patinajemobile.util.Resource;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private final DashboardApi api;

    public DashboardRepository(Context ctx) {
        this.api = RetrofitClient.get(ctx).dashboardApi();
    }

    public LiveData<Resource<DashboardSummary>> getSummary() {
        MutableLiveData<Resource<DashboardSummary>> liveData = new MutableLiveData<>();
        // CORREGIDO: loading() no lleva null
        liveData.setValue(Resource.loading());

        api.getSummary().enqueue(new Callback<DashboardSummary>() {
            @Override
            public void onResponse(Call<DashboardSummary> call, Response<DashboardSummary> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    // CORREGIDO: error() solo lleva el mensaje
                    liveData.setValue(Resource.error("Error al obtener resumen"));
                }
            }

            @Override
            public void onFailure(Call<DashboardSummary> call, Throwable t) {
                // CORREGIDO: error() solo lleva el mensaje
                liveData.setValue(Resource.error(t.getMessage()));
            }
        });
        return liveData;
    }

    public LiveData<Resource<List<Evento>>> getEventos() {
        MutableLiveData<Resource<List<Evento>>> liveData = new MutableLiveData<>();
        // CORREGIDO: loading() no lleva null
        liveData.setValue(Resource.loading());

        api.getProximosEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    // CORREGIDO: error() solo lleva el mensaje
                    liveData.setValue(Resource.error("Error al obtener eventos"));
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                // CORREGIDO: error() solo lleva el mensaje
                liveData.setValue(Resource.error(t.getMessage()));
            }
        });
        return liveData;
    }
}