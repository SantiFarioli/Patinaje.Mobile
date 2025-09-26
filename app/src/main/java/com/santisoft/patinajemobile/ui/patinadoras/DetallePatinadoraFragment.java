package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.patinajemobile.databinding.FragmentDetallePatinadoraBinding;

public class DetallePatinadoraFragment extends Fragment {

    private FragmentDetallePatinadoraBinding vb;
    private PatinadorasViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vb = FragmentDetallePatinadoraBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(PatinadorasViewModel.class);

        int id = getArguments() != null ? getArguments().getInt("id", -1) : -1;
        if (id != -1) vm.cargarDetalle(id);

        vm.detalle.observe(getViewLifecycleOwner(), det -> {
            if (det == null) return;
            vb.tvNombre.setText(det.nombre + " " + det.apellido);
            vb.tvCategoria.setText("Categoría: " + det.categoria);
            vb.tvFechaNac.setText("Nacimiento: " + det.fechaNacimiento);
            vb.tvFichaMedica.setText("Ficha médica: " + det.fichaMedica);
            vb.tvEstado.setText(det.activo ? "Activo" : "Inactivo");
            vb.tvExtras.setText(
                    "Gimnasio: " + (det.asisteGimnasio ? "Sí" : "No") + "\n" +
                            "Nutricionista: " + (det.asisteNutricionista ? "Sí" : "No") + "\n" +
                            "Psicólogo: " + (det.asistePsicologo ? "Sí" : "No")
            );
            vb.tvProfesor.setText("Profesor: " + det.profesorNombre);
            if (det.clubNombre != null) {
                vb.tvClub.setText("Club: " + det.clubNombre);
            }
        });

        vb.btnVolver.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );
    }
}
