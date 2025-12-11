package com.santisoft.patinajemobile.ui.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData; // Importante
import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.repository.DashboardRepository;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final DashboardRepository repo;

    // Instanciamos los LiveData aquí para que conserven el estado
    private final MutableLiveData<DashboardSummary> summary = new MutableLiveData<>();
    private final MutableLiveData<List<Evento>> eventos = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repo = new DashboardRepository(application);
    }

    public LiveData<DashboardSummary> getSummary() { return summary; }
    public LiveData<List<Evento>> getEventos() { return eventos; }

    public void cargarDatos() {
        repo.loadSummary(summary);
        repo.loadEventos(eventos);
    }
}