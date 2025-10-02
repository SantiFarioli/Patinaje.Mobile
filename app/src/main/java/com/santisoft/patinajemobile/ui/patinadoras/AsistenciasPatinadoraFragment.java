package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.databinding.FragmentAsistenciasPatinadoraBinding;

public class AsistenciasPatinadoraFragment extends Fragment {

    private FragmentAsistenciasPatinadoraBinding binding;
    private DetallePatinadoraViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAsistenciasPatinadoraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        binding.recyclerAsistencias.setLayoutManager(new LinearLayoutManager(getContext()));

        vm.getPatinador().observe(getViewLifecycleOwner(), p -> {
            if (p != null && p.asistencias != null) {
                binding.recyclerAsistencias.setAdapter(new AsistenciasAdapter(p.asistencias));
            }
        });
    }
}
