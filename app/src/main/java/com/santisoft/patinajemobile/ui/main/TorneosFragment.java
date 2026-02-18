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
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.patinajemobile.R;

public class TorneosFragment extends Fragment {

    private TorneosViewModel vm; // Referencia al VM

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Usamos el layout nuevo que creaste
        return inflater.inflate(R.layout.fragment_torneos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Inicializar ViewModel
        vm = new ViewModelProvider(this).get(TorneosViewModel.class);

        // 2. Configurar RecyclerView
        RecyclerView recycler = view.findViewById(R.id.rvTorneos);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        EventosAdapter adapter = new EventosAdapter();
        adapter.setListener(evento -> {
            // Navegación segura usando SafeArgs (esto estaba bien)
            // Asegurate que en tu nav_graph la acción espere un objeto Evento o el ID
            // Si da error acá, avisame para revisar el nav_graph
             /*
             TorneosFragmentDirections.ActionTorneosToDetalle action =
                 TorneosFragmentDirections.actionTorneosToDetalle(evento);
             NavHostFragment.findNavController(this).navigate(action);
             */
            // Por ahora, navegación simple para probar (si no tenés args configurados):
            NavHostFragment.findNavController(this).navigate(R.id.detalleTorneoFragment);
        });
        recycler.setAdapter(adapter);

        // 3. OBSERVAR (Pattern Observer)
        vm.getTorneos().observe(getViewLifecycleOwner(), lista -> {
            if (lista != null) {
                adapter.submit(lista);
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // 4. Pedir datos
        vm.cargarTorneos();
    }
}