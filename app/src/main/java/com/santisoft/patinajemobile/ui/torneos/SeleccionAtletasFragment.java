package com.santisoft.patinajemobile.ui.torneos;

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
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.databinding.FragmentSeleccionAtletasBinding;
import com.santisoft.patinajemobile.ui.patinadoras.PatinadorasViewModel;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;

public class SeleccionAtletasFragment extends Fragment {

    private FragmentSeleccionAtletasBinding vb;
    private SeleccionAtletasViewModel vm;
    private SeleccionAtletasAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        vb = FragmentSeleccionAtletasBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup Toolbar
        vb.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // 2. Setup RecyclerView
        adapter = new SeleccionAtletasAdapter();
        vb.rvAtletas.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.rvAtletas.setAdapter(adapter);

        // 3. ViewModel & Data
        vm = new ViewModelProvider(this).get(SeleccionAtletasViewModel.class);

        // Observar lista de atletas
        vm.getAtletas().observe(getViewLifecycleOwner(), lista -> {
            if (lista != null) {
                adapter.submit(lista);
            }
        });

        // Observar éxito de inscripción
        vm.getInscripcionExitosa().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                new cn.pedant.SweetAlert.SweetAlertDialog(requireContext(),
                        cn.pedant.SweetAlert.SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("¡Solicitud Enviada!")
                        .setContentText("Tu lista de atletas ha sido enviada a la delegada para su revisión.")
                        .setConfirmText("Entendido")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();
                            // Retorno al listado de Torneos para "cerrar el circuito"
                            androidx.navigation.Navigation.findNavController(view).popBackStack(R.id.torneosFragment,
                                    false);
                        })
                        .show();
                vm.onInscripcionHandled();
            }
        });

        // Observar Errores
        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                vm.onErrorHandled();
            }
        });

        // Cargar datos
        vm.cargarAtletas();

        // 4. Botón Confirmar
        vb.btnConfirmar.setOnClickListener(v -> {
            List<PatinadoraListItem> seleccionadas = adapter.getSelectedItems();
            vm.confirmarInscripcion(seleccionadas);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vb = null;
    }
}
