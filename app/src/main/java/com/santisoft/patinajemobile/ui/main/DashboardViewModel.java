package com.santisoft.patinajemobile.ui.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.patinajemobile.data.model.dashboard.DashboardSummary;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.data.repository.DashboardRepository;
import com.santisoft.patinajemobile.util.Resource;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final DashboardRepository repo;

    private final MutableLiveData<Boolean> _refreshTrigger = new MutableLiveData<>();

    public final LiveData<Resource<DashboardSummary>> summary;
    public final LiveData<Resource<List<Evento>>> eventos;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repo = new DashboardRepository(application);

        // Al iniciar o refrescar, cargamos ambos
        summary = Transformations.switchMap(_refreshTrigger, v -> repo.getSummary());
        eventos = Transformations.switchMap(_refreshTrigger, v -> repo.getEventos());

        // Carga inicial
        refresh();
    }

    public void refresh() {
        _refreshTrigger.setValue(true);
    }
}
