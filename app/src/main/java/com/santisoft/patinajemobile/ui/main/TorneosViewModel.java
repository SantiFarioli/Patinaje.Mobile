package com.santisoft.patinajemobile.ui.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.model.torneos.Torneo;
import com.santisoft.patinajemobile.data.remote.EvaluacionesApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TorneosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Evento>> torneos = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public TorneosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Evento>> getTorneos() { return torneos; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void cargarTorneos() {
        isLoading.setValue(true);
        EvaluacionesApi api = RetrofitClient.get(getApplication()).evaluacionesApi();

        api.getTorneos().enqueue(new Callback<List<Torneo>>() {
            @Override
            public void onResponse(Call<List<Torneo>> call, Response<List<Torneo>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    // Mapeo de datos (Lógica de negocio) aquí, NO en el fragment
                    List<Evento> listaMapeada = new ArrayList<>();
                    for (Torneo t : response.body()) {
                        Evento e = new Evento();
                        e.id = t.torneoId; // Asumiendo que Evento tiene ID, sino agregalo
                        e.nombre = t.nombre;
                        e.lugar = t.lugar;
                        e.fechaInicio = t.fechaInicio;
                        listaMapeada.add(e);
                    }
                    torneos.postValue(listaMapeada);
                } else {
                    error.postValue("Error al cargar torneos");
                }
            }

            @Override
            public void onFailure(Call<List<Torneo>> call, Throwable t) {
                isLoading.setValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
}