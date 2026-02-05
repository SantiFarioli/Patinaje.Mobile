package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PagosApi {
    @GET("api/pagos/pendientes")
    Call<List<PagoPendiente>> getPendientes();

    @Multipart
    @POST("api/pagos/{id}/comprobante")
    Call<Void> uploadComprobante(
            @Path("id") int id,
            @Part MultipartBody.Part file);
}
