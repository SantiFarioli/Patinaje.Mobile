package com.santisoft.patinajemobile.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.remote.DashboardApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {
    private final DashboardApi api;

    public DashboardRepository(Context ctx) {
        this.api = RetrofitClient.get(ctx).dashboardApi();
    }

    // Método modificado para cargar datos en el LiveData del ViewModel
    public void loadSummary(MutableLiveData<DashboardSummary> liveData) {
        api.getSummary().enqueue(new Callback<DashboardSummary>() {
            @Override
            public void onResponse(Call<DashboardSummary> call, Response<DashboardSummary> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<DashboardSummary> call, Throwable t) {
                // Opcional: Manejar error
            }
        });
    }

    // Método modificado para cargar eventos
    public void loadEventos(MutableLiveData<List<Evento>> liveData) {
        api.getProximosEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                // Opcional: Manejar error
            }
        });
    }
}