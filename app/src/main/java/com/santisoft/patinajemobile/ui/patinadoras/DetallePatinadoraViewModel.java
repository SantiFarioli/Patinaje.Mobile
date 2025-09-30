package com.santisoft.patinajemobile.ui.patinadoras;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;

public class DetallePatinadoraViewModel extends AndroidViewModel {

    private final PatinadorasRepository repository;
    private final MutableLiveData<PatinadoraDetail> patinador = new MutableLiveData<>();

    public DetallePatinadoraViewModel(@NonNull Application application) {
        super(application);
        repository = new PatinadorasRepository(application);
    }

    public LiveData<PatinadoraDetail> getPatinador() {
        return patinador;
    }

    // 👇 nombre corregido
    public void loadPatinador(int id) {
        repository.getDetalle(id, result -> patinador.postValue(result));
    }
}
