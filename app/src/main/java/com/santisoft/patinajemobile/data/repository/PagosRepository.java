package com.santisoft.patinajemobile.data.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import com.santisoft.patinajemobile.data.remote.PagosApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosRepository {
    private final PagosApi api;

    public PagosRepository(Context context) {
        // Obtenemos la API desde el cliente Retrofit
        this.api = RetrofitClient.get(context).pagosApi();
    }

    // Método void que actualiza un LiveData (Regla del profe)
    public void cargarPendientes(MutableLiveData<List<PagoPendiente>> liveData, MutableLiveData<String> error) {
        api.getPendientes().enqueue(new Callback<List<PagoPendiente>>() {
            @Override
            public void onResponse(Call<List<PagoPendiente>> call, Response<List<PagoPendiente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    error.postValue("Error al cargar datos");
                }
            }
            @Override
            public void onFailure(Call<List<PagoPendiente>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
}