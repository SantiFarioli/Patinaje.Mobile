package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentPagosPatinadoraBinding; // Usamos el binding del layout que reutilizaste

public class DeudoresFragment extends Fragment {

    // ViewBinding
    private FragmentPagosPatinadoraBinding binding;
    private DeudoresViewModel vm;
    private DeudoresAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos con Binding
        binding = FragmentPagosPatinadoraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar ViewModel
        vm = new ViewModelProvider(this).get(DeudoresViewModel.class);

        // 2. Configurar UI inicial (Título)
        binding.tvResumen.setText("Reporte de Deudas");
        binding.tvResumen.setVisibility(View.VISIBLE);

        // 3. Configurar RecyclerView
        adapter = new DeudoresAdapter();
        binding.recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPagos.setAdapter(adapter);

        // 4. OBSERVAR DATOS (La vista reacciona, no pide)
        vm.getPendientes().observe(getViewLifecycleOwner(), lista -> {
            if (lista != null) {
                adapter.submit(lista);
                if (lista.isEmpty()) {
                    binding.tvResumen.setText("¡Todo al día! No hay deudas.");
                }
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Pedir al ViewModel que inicie la carga
        vm.cargarDatos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Limpiar binding para evitar fugas de memoria
    }
}