package com.santisoft.patinajemobile.ui.main;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.pagos.PagoPendiente;
import com.santisoft.patinajemobile.data.repository.PagosRepository;
import java.util.List;

public class DeudoresViewModel extends AndroidViewModel {

    private final PagosRepository repo;

    // LiveData
    private final MutableLiveData<List<PagoPendiente>> listaPendientes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public DeudoresViewModel(@NonNull Application application) {
        super(application);
        repo = new PagosRepository(application);
    }

    public LiveData<List<PagoPendiente>> getPendientes() { return listaPendientes; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void cargarDatos() {
        isLoading.setValue(true);
        repo.cargarPendientes(listaPendientes, error);
    }

    // --- LÓGICA DE NEGOCIO (MVVM Estricto) ---

    // 1. Objeto de Transferencia (DTO) para decirle a la vista qué pintar exactamente
    public static class DeudaState {
        public final String titulo;
        public final int colorTitulo; // R.color.xxx
        public final String monto;
        public final int colorMonto; // R.color.xxx
        public final String subtitulo;

        public DeudaState(String t, int cT, String m, int cM, String s) {
            this.titulo = t; this.colorTitulo = cT;
            this.monto = m; this.colorMonto = cM;
            this.subtitulo = s;
        }
    }

    // 2. Método que calcula (El Fragment solo llama a esto)
    public DeudaState calcularEstadoGeneral(List<PagoPendiente> lista) {
        if (lista == null || lista.isEmpty()) {
            return new DeudaState(
                    "SIN DEUDAS",
                    R.color.success, // Verde
                    "$ 0",
                    R.color.success,
                    "¡Todo al día!"
            );
        }

        double total = 0;
        for (PagoPendiente p : lista) {
            total += p.monto;
        }

        return new DeudaState(
                "TOTAL POR COBRAR",
                R.color.md_error, // Rojo
                String.format("$ %,.0f", total),
                R.color.md_error,
                lista.size() + " pagos pendientes"
        );
    }
}