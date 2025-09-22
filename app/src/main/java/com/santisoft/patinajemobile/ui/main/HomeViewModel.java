package com.santisoft.patinajemobile.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.DashboardSummary;
import com.santisoft.patinajemobile.data.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<DashboardSummary> _summary = new MutableLiveData<>();
    public LiveData<DashboardSummary> summary = _summary;

    private final MutableLiveData<List<Evento>> _eventos = new MutableLiveData<>();
    public LiveData<List<Evento>> eventos = _eventos;

    public HomeViewModel(@NonNull Application app) {
        super(app);
        // MOCK: estos datos vendrán del backend luego
        _summary.setValue(new DashboardSummary(56, 3));

        List<Evento> list = new ArrayList<>();
        list.add(new Evento("Entrenamiento selectivo", "Mar 24, 18:00", "Pista Central"));
        list.add(new Evento("Torneo Apertura", "Jue 26, 09:00", "Club Unión"));
        list.add(new Evento("Reunión de padres", "Vie 27, 19:30", "Zoom"));
        _eventos.setValue(list);
    }

    public void buscar(String query){
        // TODO: implementar búsqueda (filtrar eventos / patinadoras) – por ahora no hace nada
    }
}
