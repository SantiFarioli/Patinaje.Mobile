package com.santisoft.patinajemobile.ui.patinadoras;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.data.repository.PatinadorasRepository;

public class DetallePatinadoraViewModel extends AndroidViewModel {

    private final PatinadorasRepository repository;
    private final MutableLiveData<PatinadoraDetail> patinador = new MutableLiveData<>();

    // 👇 Campos formateados para la vista
    public final LiveData<String> edadFormatted;

    public DetallePatinadoraViewModel(@NonNull Application application) {
        super(application);
        repository = new PatinadorasRepository(application);

        this.edadFormatted = Transformations.map(patinador, p -> {
            if (p == null || p.fechaNacimiento == null)
                return "—";
            return getNacimientoConEdad(p.fechaNacimiento);
        });
    }

    public LiveData<PatinadoraDetail> getPatinador() {
        return patinador;
    }

    public void loadPatinador(int id) {
        repository.getDetalle(id, result -> patinador.postValue(result));
    }

    // --- LÓGICA DE NEGOCIO PARA PAGOS (MVVM Estricto) ---

    // Clase DTO para transportar el estado visual a la vista
    public static class PaymentState {
        public final String textoEstado;
        public final int colorEstado; // Resource ID (R.color.xxx)
        public final String textoMonto;
        public final int colorMonto;
        public final String textoVencimiento;

        public PaymentState(String tE, int cE, String tM, int cM, String tV) {
            this.textoEstado = tE; this.colorEstado = cE;
            this.textoMonto = tM; this.colorMonto = cM;
            this.textoVencimiento = tV;
        }
    }

    public PaymentState calcularEstadoPagos(PatinadoraDetail p) {
        if (p == null || p.pagos == null) return null;

        // 1. Calcular deuda total
        double totalDeuda = p.pagos.stream()
                .filter(pag -> !"pagado".equalsIgnoreCase(pag.estado))
                .mapToDouble(pag -> pag.monto)
                .sum();

        boolean tieneDeuda = totalDeuda > 0;

        if (tieneDeuda) {
            // BUSCAR PRÓXIMO VENCIMIENTO
            String proxVenc = p.pagos.stream()
                    .filter(pag -> !"pagado".equalsIgnoreCase(pag.estado))
                    .findFirst() // Asumimos que la lista viene ordenada o tomamos el primero
                    .map(pag -> pag.fechaVencimiento != null && pag.fechaVencimiento.length() >= 10
                            ? pag.fechaVencimiento.substring(0, 10)
                            : "-")
                    .orElse("-");

            return new PaymentState(
                    "DEUDA PENDIENTE",
                    R.color.md_error, // Rojo
                    String.format("$%,.0f", totalDeuda),
                    R.color.md_error,
                    "Vence: " + proxVenc
            );
        } else {
            // ESTADO AL DÍA
            return new PaymentState(
                    "AL DÍA",
                    R.color.success, // Verde (Asegurate de tener este color en colors.xml)
                    "$0",
                    R.color.black, // O un verde oscuro
                    "¡Todo pagado!"
            );
        }
    }

    // --- Helpers ---

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