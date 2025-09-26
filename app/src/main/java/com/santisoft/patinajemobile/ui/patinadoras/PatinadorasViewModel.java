package com.santisoft.patinajemobile.ui.patinadoras;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;

import java.util.List;

public class PatinadorasViewModel extends AndroidViewModel {

    private final PatinadorasRepository repo;
    public final MutableLiveData<List<PatinadoraListItem>> patinadoras = new MutableLiveData<>();
    public final MutableLiveData<PatinadoraDetail> detalle = new MutableLiveData<>();

    public PatinadorasViewModel(@NonNull Application app) {
        super(app);
        repo = new PatinadorasRepository(app);
    }

    public void cargarPatinadoras() {
        repo.getPatinadoras(patinadoras::postValue);
    }

    public void cargarDetalle(int id) {
        repo.getDetalle(id, detalle::postValue);
    }
}
