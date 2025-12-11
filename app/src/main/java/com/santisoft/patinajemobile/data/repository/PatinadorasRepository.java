package com.santisoft.patinajemobile.data.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.data.remote.PagedResponse;
import com.santisoft.patinajemobile.data.remote.PatinadorasApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatinadorasRepository {

    private final PatinadorasApi api;

    public PatinadorasRepository(Context ctx) {
        api = RetrofitClient.get(ctx).patinadorasApi();
    }

    public void getPatinadoras(Consumer<List<PatinadoraListItem>> cb) {
        api.getPatinadoras().enqueue(new Callback<PagedResponse<PatinadoraListItem>>() {
            @Override
            public void onResponse(Call<PagedResponse<PatinadoraListItem>> call,
                                   Response<PagedResponse<PatinadoraListItem>> res) {
                if (res.isSuccessful() && res.body() != null) {
                    Log.d("API_DEBUG", "Lista recibida: " + new Gson().toJson(res.body()));
                    cb.accept(res.body().data);
                } else {
                    Log.e("API_DEBUG", "Error lista patinadoras: " + res.code());
                    cb.accept(null);
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<PatinadoraListItem>> call, Throwable t) {
                Log.e("API_DEBUG", "Fallo lista patinadoras: " + t.getMessage(), t);
                cb.accept(null);
            }
        });
    }

    // 👇 Ahora devuelve PatinadoraDetail (con tutores incluidos)
    public void getDetalle(int id, Consumer<PatinadoraDetail> cb) {
        api.getDetalle(id).enqueue(new Callback<PatinadoraDetail>() {
            @Override
            public void onResponse(Call<PatinadoraDetail> call, Response<PatinadoraDetail> res) {
                if (res.isSuccessful() && res.body() != null) {
                    PatinadoraDetail detalle = res.body();
                    Log.d("API_DEBUG", "Detalle recibido: " + new Gson().toJson(detalle));
                    cb.accept(detalle);
                } else {
                    Log.e("API_DEBUG", "Respuesta vacía o error: " + res.code());
                    cb.accept(null);
                }
            }

            @Override
            public void onFailure(Call<PatinadoraDetail> call, Throwable t) {
                Log.e("API_DEBUG", "Error en getDetalle: " + t.getMessage(), t);
                cb.accept(null);
            }
        });
    }
}
