package com.santisoft.patinajemobile.ui.torneos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.data.repository.PatinadorasRepository;

import java.util.List;

public class SeleccionAtletasViewModel extends AndroidViewModel {

    private final PatinadorasRepository repo;
    private final MutableLiveData<List<PatinadoraListItem>> _atletas = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _inscripcionExitosa = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    public SeleccionAtletasViewModel(@NonNull Application application) {
        super(application);
        repo = new PatinadorasRepository(application);
    }

    public LiveData<List<PatinadoraListItem>> getAtletas() {
        return _atletas;
    }

    public LiveData<Boolean> getInscripcionExitosa() {
        return _inscripcionExitosa;
    }

    public LiveData<String> getError() {
        return _error;
    }

    public void cargarAtletas() {
        repo.getPatinadoras(_atletas::postValue);
    }

    public void confirmarInscripcion(List<PatinadoraListItem> seleccionadas) {
        if (seleccionadas == null || seleccionadas.isEmpty()) {
            _error.setValue("Debe seleccionar al menos una atleta.");
            return;
        }

        // Simulación de envío al servidor
        // Aquí iría la llamada al repo para inscribir
        // repo.inscribirAtletas(seleccionadas, ...);

        // Simulamos éxito inmediato
        _inscripcionExitosa.setValue(true);
    }

    public void onInscripcionHandled() {
        _inscripcionExitosa.setValue(false);
    }

    public void onErrorHandled() {
        _error.setValue(null);
    }
}
