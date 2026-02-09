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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentDatosPatinadoraBinding;

public class DatosPatinadoraFragment extends Fragment {

    private FragmentDatosPatinadoraBinding binding;
    private DetallePatinadoraViewModel viewModel;
    private TutoresAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentDatosPatinadoraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        // Configurar RecyclerView
        adapter = new TutoresAdapter();
        binding.recyclerTutores.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerTutores.setAdapter(adapter);

        // Observar datos del patinador
        viewModel.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p == null)
                return;

            binding.tvDni.setText(safe(p.dni));
            binding.tvDomicilio.setText(safe(p.domicilio));
            binding.tvFicha.setText(safe(p.fichaMedica));
            binding.tvEstado.setText(p.activo ? "Activa" : "Inactiva");

            // Tutores
            adapter.submit(p.tutores);
        });

        // Observar datos formateados (Lógica en VM)
        viewModel.edadFormatted.observe(getViewLifecycleOwner(), edad -> {
            binding.tvNacimiento.setText(edad);
        });

        binding.btnVolver.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());
    }

    // Helper simple para null-safety, permitido en vista para evitar crash
    // Si queremos ser puristas, esto también debería ir al VM.
    private String safe(Object o) {
        return o == null ? "—" : String.valueOf(o);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evitar memory leaks con ViewBinding
    }
}
