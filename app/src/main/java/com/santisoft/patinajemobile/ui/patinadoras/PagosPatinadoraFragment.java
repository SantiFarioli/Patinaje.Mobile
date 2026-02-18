package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
        // Inflar con el nuevo binding (asegurate de hacer Rebuild Project si sale error acá)
        binding = FragmentPagosPatinadoraBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        binding.recyclerPagos.setLayoutManager(new LinearLayoutManager(getContext()));

        vm.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p != null && p.pagos != null) {
                // 1. Configurar Adapter (Lista Timeline)
                binding.recyclerPagos.setAdapter(new PagosAdapter(p.pagos));

                // 2. Calcular Estado del Header (Delegado al ViewModel)
                DetallePatinadoraViewModel.PaymentState estado = vm.calcularEstadoPagos(p);

                if (estado != null) {
                    aplicarEstadoHeader(estado);
                }
            } else {
                // Manejo opcional de estado vacío
                binding.tvEstadoGeneral.setText("Sin datos");
                binding.tvTotalDeuda.setText("$0");
            }
        });

        return binding.getRoot();
    }

    // Método puramente visual
    private void aplicarEstadoHeader(DetallePatinadoraViewModel.PaymentState s) {
        binding.tvEstadoGeneral.setText(s.textoEstado);
        binding.tvEstadoGeneral.setTextColor(ContextCompat.getColor(requireContext(), s.colorEstado));

        binding.tvTotalDeuda.setText(s.textoMonto);
        binding.tvTotalDeuda.setTextColor(ContextCompat.getColor(requireContext(), s.colorMonto));

        binding.tvProximoVencimiento.setText(s.textoVencimiento);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}