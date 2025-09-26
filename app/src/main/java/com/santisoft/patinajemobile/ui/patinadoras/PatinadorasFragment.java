package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraListItem;
import com.santisoft.patinajemobile.databinding.FragmentPatinadorasBinding;

public class PatinadorasFragment extends Fragment {

    private FragmentPatinadorasBinding vb;
    private PatinadorasViewModel vm;
    private PatinadorasAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vb = FragmentPatinadorasBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PatinadorasAdapter(this::abrirDetalle);
        vb.recyclerPatinadoras.setLayoutManager(new LinearLayoutManager(getContext()));
        vb.recyclerPatinadoras.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(PatinadorasViewModel.class);

        vm.patinadoras.observe(getViewLifecycleOwner(), lista -> {
            boolean vacio = (lista == null || lista.isEmpty());
            vb.lblNoPatinadoras.setVisibility(vacio ? View.VISIBLE : View.GONE);
            adapter.submit(lista);
        });

        vm.cargarPatinadoras();
    }

    private void abrirDetalle(PatinadoraListItem p) {
        Bundle args = new Bundle();
        args.putInt("id", p.patinadorId);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_patinadoras_to_detalle, args);
    }
}
