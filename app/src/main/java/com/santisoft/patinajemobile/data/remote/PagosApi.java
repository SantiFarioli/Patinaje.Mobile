package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PagosApi {
    @GET("api/pagos/pendientes")
    Call<List<PagoPendiente>> getPendientes();
}
