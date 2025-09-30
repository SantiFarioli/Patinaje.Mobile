package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.databinding.FragmentDatosPatinadoraBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatosPatinadoraFragment extends Fragment {

    private FragmentDatosPatinadoraBinding binding;
    private DetallePatinadoraViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDatosPatinadoraBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        viewModel.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p == null) return;

            // ==== Datos básicos ====
            binding.tvDni.setText("DNI: " + safe(p.dni));
            binding.tvDomicilio.setText("Domicilio: " + safe(p.domicilio));
            binding.tvFichaMedica.setText("Ficha médica: " + safe(p.fichaMedica));
            binding.tvEstado.setText("Estado: " + (p.activo ? "Activa" : "Inactiva"));

            // ==== Fecha de nacimiento + edad ====
            try {
                String fecha = p.fechaNacimiento.substring(0, 10); // yyyy-MM-dd
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Calendar nac = Calendar.getInstance();
                nac.setTime(sdf.parse(fecha));
                Calendar hoy = Calendar.getInstance();
                int edad = hoy.get(Calendar.YEAR) - nac.get(Calendar.YEAR);
                if (hoy.get(Calendar.DAY_OF_YEAR) < nac.get(Calendar.DAY_OF_YEAR)) {
                    edad--;
                }
                binding.tvFechaNac.setText("Nacimiento: " + fecha + " (" + edad + " años)");
            } catch (Exception e) {
                binding.tvFechaNac.setText("Nacimiento: " + safe(p.fechaNacimiento));
            }

            // ==== Tutores ====
            binding.containerTutores.removeAllViews();
            if (p.tutores != null && !p.tutores.isEmpty()) {
                for (PatinadoraDetail.TutorDto t : p.tutores) {
                    TextView tv = new TextView(getContext());
                    tv.setText("- " + t.nombre + " " + t.apellido
                            + " | Relación: " + safe(t.relacion)
                            + " | Tel: " + safe(t.telefono)
                            + " | Email: " + safe(t.email)
                            + " | Domicilio: " + safe(t.domicilio));
                    tv.setTextAppearance(R.style.Text_Body);
                    binding.containerTutores.addView(tv);
                }
            } else {
                TextView tv = new TextView(requireContext());
                tv.setText("No hay tutores registrados");
                binding.containerTutores.addView(tv);
            }
        });

        // ==== Botón volver ====
        binding.btnVolver.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );

        return binding.getRoot();
    }

    private String safe(Object o) {
        return o == null ? "—" : String.valueOf(o);
    }
}
