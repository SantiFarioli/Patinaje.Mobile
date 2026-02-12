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
import com.santisoft.patinajemobile.util.Resource;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding vb;
    private DashboardViewModel vm;
    private EventosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        vb = FragmentDashboardBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(DashboardViewModel.class);

        // 1. Configurar Recycler
        adapter = new EventosAdapter();
        vb.recyclerEventos.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.recyclerEventos.setAdapter(adapter);

        adapter.setListener(evento -> {
            DashboardFragmentDirections.ActionDashboardToDetalle action = DashboardFragmentDirections
                    .actionDashboardToDetalle(evento);
            NavHostFragment.findNavController(this).navigate(action);
        });

        // 2. Observar Resumen
        vm.summary.observe(getViewLifecycleOwner(), res -> {
            if (res == null)
                return;
            // No mostramos loading explícito para los contadores, dejamos el 0 por defecto
            // o shimmer
            if (res.status == Resource.Status.SUCCESS && res.data != null) {
                vb.tvCountPatinadoras.setText(String.valueOf(res.data.totalPatinadoras));
                vb.tvCountEventos.setText(String.valueOf(res.data.totalEventosProximos));
            } else if (res.status == Resource.Status.ERROR) {
                // Opcional: mostrar error visual
            }
        });

        // 3. Observar Lista de Eventos
        vm.eventos.observe(getViewLifecycleOwner(), res -> {
            if (res == null)
                return;

            switch (res.status) {
                case LOADING:
                    vb.progress.setVisibility(View.VISIBLE);
                    vb.recyclerEventos.setVisibility(View.GONE);
                    vb.lblNoEventos.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    vb.progress.setVisibility(View.GONE);
                    if (res.data != null && !res.data.isEmpty()) {
                        vb.recyclerEventos.setVisibility(View.VISIBLE);
                        vb.lblNoEventos.setVisibility(View.GONE);
                        adapter.submit(res.data);
                    } else {
                        vb.recyclerEventos.setVisibility(View.GONE);
                        vb.lblNoEventos.setVisibility(View.VISIBLE);
                    }
                    break;
                case ERROR:
                    vb.progress.setVisibility(View.GONE);
                    vb.lblNoEventos.setVisibility(View.VISIBLE);
                    vb.lblNoEventos.setText(res.message);
                    break;
            }
        });

        // 4. Navegación (Quick Actions)
        vb.cardPatinadoras.setOnClickListener(
                v -> NavHostFragment.findNavController(this).navigate(R.id.action_dashboard_to_patinadoras));

        vb.cardAsistencias.setOnClickListener(
                v -> NavHostFragment.findNavController(this).navigate(R.id.action_dashboard_to_tomar_asistencia));

        vb.cardTorneos.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.torneosFragment));

        vb.cardPagos.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.deudoresFragment));

        vb.tvVerTodo.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.torneosFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vb = null;
    }
}
