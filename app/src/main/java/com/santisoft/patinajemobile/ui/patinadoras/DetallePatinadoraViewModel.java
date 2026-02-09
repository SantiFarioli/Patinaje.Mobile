package com.santisoft.patinajemobile.ui.patinadoras;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.data.repository.PatinadorasRepository;

public class DetallePatinadoraViewModel extends AndroidViewModel {

    private final PatinadorasRepository repository;
    private final MutableLiveData<PatinadoraDetail> patinador = new MutableLiveData<>();

    // 👇 Campos formateados para la vista (Strict MVVM: Logic in VM)
    public final LiveData<String> edadFormatted;

    public DetallePatinadoraViewModel(@NonNull Application application) {
        super(application);
        repository = new PatinadorasRepository(application);

        this.edadFormatted = androidx.lifecycle.Transformations.map(patinador, p -> {
            if (p == null || p.fechaNacimiento == null)
                return "—";
            return getNacimientoConEdad(p.fechaNacimiento);
        });
    }

    public LiveData<PatinadoraDetail> getPatinador() {
        return patinador;
    }

    // 👇 nombre corregido
    public void loadPatinador(int id) {
        repository.getDetalle(id, result -> patinador.postValue(result));
    }

    private String getNacimientoConEdad(String fecha) {
        try {
            String f = fecha.substring(0, 10);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Calendar nac = java.util.Calendar.getInstance();
            nac.setTime(sdf.parse(f));
            java.util.Calendar hoy = java.util.Calendar.getInstance();
            int edad = hoy.get(java.util.Calendar.YEAR) - nac.get(java.util.Calendar.YEAR);
            if (hoy.get(java.util.Calendar.DAY_OF_YEAR) < nac.get(java.util.Calendar.DAY_OF_YEAR)) {
                edad--;
            }
            return f + " (" + edad + " años)";
        } catch (Exception e) {
            return fecha != null ? fecha : "—";
        }
    }
}
