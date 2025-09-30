package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PatinadorasApi {
    @GET("api/patinadores")
    Call<PagedResponse<PatinadoraListItem>> getPatinadoras(); // 👈 cambio

    @GET("api/Patinadores/{id}")
    Call<PatinadoraDetail> getDetalle(@Path("id") int id);
}
