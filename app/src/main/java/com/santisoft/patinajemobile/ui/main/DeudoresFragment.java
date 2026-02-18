package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentPagosPatinadoraBinding;

public class DeudoresFragment extends Fragment {

    // Usamos FragmentPagosPatinadoraBinding porque reutilizamos ese diseño
    private FragmentPagosPatinadoraBinding binding;
    private DeudoresViewModel vm;
    private DeudoresAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPagosPatinadoraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(DeudoresViewModel.class);

        adapter = new DeudoresAdapter();
        binding.recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPagos.setAdapter(adapter);

        // OBSERVAR DATOS
        vm.getPendientes().observe(getViewLifecycleOwner(), lista -> {
            if (lista != null) {
                // 1. Actualizar la lista (Adapter)
                adapter.submit(lista);

                // 2. Actualizar el Header (MVVM ESTRICTO)
                // Le pedimos al ViewModel que haga los cálculos
                DeudoresViewModel.DeudaState estado = vm.calcularEstadoGeneral(lista);

                // Solo aplicamos lo que dice el ViewModel, sin pensar
                binding.tvEstadoGeneral.setText(estado.titulo);
                binding.tvEstadoGeneral.setTextColor(ContextCompat.getColor(requireContext(), estado.colorTitulo));

                binding.tvTotalDeuda.setText(estado.monto);
                binding.tvTotalDeuda.setTextColor(ContextCompat.getColor(requireContext(), estado.colorMonto));

                binding.tvProximoVencimiento.setText(estado.subtitulo);
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        vm.cargarDatos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}