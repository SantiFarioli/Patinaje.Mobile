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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding vb;
    private DashboardViewModel vm;
    private EventosAdapter adapter; // 👈 Usamos TU adapter existente

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vb = FragmentDashboardBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(DashboardViewModel.class);

        // 1. Configurar Recycler usando TU EventosAdapter
        adapter = new EventosAdapter(); // Instanciamos el adapter vacío
        vb.recyclerEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.recyclerEventos.setAdapter(adapter); // Lo conectamos al recycler

        // 2. Observar Resumen (Contadores)
        vm.getSummary().observe(getViewLifecycleOwner(), summary -> {
            if (summary != null) {
                vb.tvCountPatinadoras.setText(String.valueOf(summary.totalPatinadoras));
                vb.tvCountEventos.setText(String.valueOf(summary.totalEventosProximos));
            }
        });

        // 3. Observar Lista de Eventos
        vm.getEventos().observe(getViewLifecycleOwner(), eventos -> {
            if (eventos != null && !eventos.isEmpty()) {
                vb.lblNoEventos.setVisibility(View.GONE);
                vb.recyclerEventos.setVisibility(View.VISIBLE);

                // 👇 MAGIA: Usamos tu método submit()
                adapter.submit(eventos);
            } else {
                vb.lblNoEventos.setVisibility(View.VISIBLE);
                vb.recyclerEventos.setVisibility(View.GONE);
            }
        });

        // 4. Navegación
        vb.cardPatinadoras.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_dashboard_to_patinadoras));

        vb.cardAsistencias.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_dashboard_to_tomar_asistencia));

        vb.cardTorneos.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.torneosFragment));

        vb.cardPagos.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.deudoresFragment));

        // Cargar datos
        vm.cargarDatos();
    }
}