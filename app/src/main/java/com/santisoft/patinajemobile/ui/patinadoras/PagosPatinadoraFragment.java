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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentPagosPatinadoraBinding;

public class PagosPatinadoraFragment extends Fragment {

    private FragmentPagosPatinadoraBinding binding;
    private DetallePatinadoraViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPagosPatinadoraBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        binding.recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        vm.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p != null && p.pagos != null && !p.pagos.isEmpty()) {
                binding.recyclerPagos.setAdapter(new PagosAdapter(p.pagos));
                binding.tvResumen.setText(getResumenPagos(p));
                binding.tvResumen.setVisibility(View.VISIBLE);
            } else {
                binding.tvResumen.setText("Sin pagos registrados.");
            }
        });

        return binding.getRoot();
    }

    private String getResumenPagos(com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail p) {
        int pendientes = (int) p.pagos.stream().filter(x -> x.estado.equalsIgnoreCase("pendiente")).count();
        double totalPagado = p.pagos.stream()
                .filter(x -> x.estado.equalsIgnoreCase("pagado"))
                .mapToDouble(x -> x.monto)
                .sum();

        return String.format("💵 Total pagado este mes: $%,.0f   🔴 Pendientes: %d", totalPagado, pendientes);
    }
}

