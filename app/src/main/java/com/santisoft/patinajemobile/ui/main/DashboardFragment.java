package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding vb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vb = FragmentDashboardBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Navegar a Lista de Patinadoras
        vb.cardPatinadoras.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_dashboard_to_patinadoras)
        );

        // 2. Navegar a Tomar Asistencia (Desde la Card)
        vb.cardAsistencias.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_dashboard_to_tomar_asistencia)
        );

        // 3. Toasts temporales para los botones que faltan implementar
        View.OnClickListener notImplemented = v ->
                Toast.makeText(getContext(), "Próximamente", Toast.LENGTH_SHORT).show();

        vb.cardTorneos.setOnClickListener(notImplemented);
        vb.cardPagos.setOnClickListener(notImplemented);
    }
}