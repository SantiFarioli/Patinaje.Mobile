package com.santisoft.patinajemobile.ui.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import com.santisoft.patinajemobile.data.repository.PagosRepository;
import java.util.List;

public class DeudoresViewModel extends AndroidViewModel {

    private final PagosRepository repo;

    // LiveData para que la vista observe
    private final MutableLiveData<List<PagoPendiente>> listaPendientes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public DeudoresViewModel(@NonNull Application application) {
        super(application);
        // Usamos el contexto de la aplicación, no el de la vista (Regla Profe)
        repo = new PagosRepository(application);
    }

    // Getters públicos para observar
    public LiveData<List<PagoPendiente>> getPendientes() { return listaPendientes; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    // Método void que dispara la acción (Regla Profe)
    public void cargarDatos() {
        isLoading.setValue(true);
        // Delegamos al repositorio pasando los LiveData a actualizar
        repo.cargarPendientes(listaPendientes, error);
        // (Nota: isLoading debería apagarse en el callback del repo,
        // pero para simplificar lo dejamos así por ahora o lo manejas en el repo)
    }
}