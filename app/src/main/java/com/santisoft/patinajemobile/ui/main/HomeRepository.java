package com.santisoft.patinajemobile.ui.main;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.remote.HomeApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import com.santisoft.patinajemobile.util.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {

    private final HomeApi api;

    public HomeRepository(Context ctx) {
        this.api = RetrofitClient.get(ctx).homeApi();
    }

    public LiveData<Resource<DashboardSummary>> fetchSummary() {
        MutableLiveData<Resource<DashboardSummary>> live = new MutableLiveData<>();
        live.postValue(Resource.loading());

        api.getSummary().enqueue(new Callback<DashboardSummary>() {
            @Override public void onResponse(Call<DashboardSummary> call, Response<DashboardSummary> res) {
                if (res.isSuccessful() && res.body()!=null) {
                    live.postValue(Resource.success(res.body()));
                } else {
                    live.postValue(Resource.error("Error " + res.code()));
                }
            }
            @Override public void onFailure(Call<DashboardSummary> call, Throwable t) {
                live.postValue(Resource.error(t.getMessage()));
            }
        });

        return live;
    }

    public LiveData<Resource<List<Evento>>> fetchEventos() {
        MutableLiveData<Resource<List<Evento>>> live = new MutableLiveData<>();
        live.postValue(Resource.loading());

        api.getEventos().enqueue(new Callback<List<Evento>>() {
            @Override public void onResponse(Call<List<Evento>> call, Response<List<Evento>> res) {
                if (res.isSuccessful() && res.body()!=null) {
                    live.postValue(Resource.success(res.body()));
                } else {
                    live.postValue(Resource.error("Error " + res.code()));
                }
            }
            @Override public void onFailure(Call<List<Evento>> call, Throwable t) {
                live.postValue(Resource.error(t.getMessage()));
            }
        });

        return live;
    }
}
