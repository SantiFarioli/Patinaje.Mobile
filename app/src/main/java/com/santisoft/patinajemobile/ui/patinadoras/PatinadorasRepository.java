package com.santisoft.patinajemobile.ui.patinadoras;

import android.content.Context;

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
                    cb.accept(res.body().data); // 👈 usar .data
                } else {
                    cb.accept(null);
                }
            }

            @Override
            public void onFailure(Call<PagedResponse<PatinadoraListItem>> call, Throwable t) {
                cb.accept(null);
            }
        });
    }

    public void getDetalle(int id, Consumer<PatinadoraDetail> cb) {
        api.getDetalle(id).enqueue(new Callback<PatinadoraDetail>() {
            @Override
            public void onResponse(Call<PatinadoraDetail> call, Response<PatinadoraDetail> res) {
                cb.accept(res.isSuccessful() ? res.body() : null);
            }
            @Override
            public void onFailure(Call<PatinadoraDetail> call, Throwable t) {
                cb.accept(null);
            }
        });
    }
}
