package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout; // Para el layoutEmpty

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;

public class EvaluacionesPatinadoraFragment extends Fragment {

    private DetallePatinadoraViewModel vm;
    private RecyclerView recycler;
    private LinearLayout layoutEmpty; // 👈 Referencia al estado vacío

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 👇 Usamos TU layout nuevo
        return inflater.inflate(R.layout.fragment_evaluaciones_patinadora, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recyclerEvaluaciones);
        layoutEmpty = view.findViewById(R.id.layoutEmpty); // 👈 ID del LinearLayout vacío

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        vm = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        vm.getPatinador().observe(getViewLifecycleOwner(), p -> {
            // Lógica de visibilidad
            if (p != null && p.evaluaciones != null && !p.evaluaciones.isEmpty()) {
                recycler.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
                recycler.setAdapter(new EvaluacionesAdapter(p.evaluaciones));
            } else {
                recycler.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}