package com.santisoft.patinajemobile.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.util.Resource;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final HomeRepository repo;

    public final MediatorLiveData<DashboardSummary> summary = new MediatorLiveData<>();
    public final MediatorLiveData<List<Evento>> eventos = new MediatorLiveData<>();

    public HomeViewModel(@NonNull Application app) {
        super(app);
        repo = new HomeRepository(app);

        // cargar al iniciar
        cargarResumen();
        cargarEventos();
    }

    public void cargarResumen() {
        LiveData<Resource<DashboardSummary>> src = repo.fetchSummary();
        summary.addSource(src, res -> {
            if (res == null) return;
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                summary.setValue(res.data);
            }
        });
    }

    public void cargarEventos() {
        LiveData<Resource<List<Evento>>> src = repo.fetchEventos();
        eventos.addSource(src, res -> {
            if (res == null) return;
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                eventos.setValue(res.data);
            }
        });
    }

    public void buscar(String query) {
        // TODO: si luego querés filtrar eventos localmente o refetch al backend
    }
}
