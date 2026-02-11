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

import com.bumptech.glide.Glide;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.eventos.Evento;
import com.santisoft.patinajemobile.databinding.FragmentDetalleTorneoBinding;

public class DetalleTorneoFragment extends Fragment {

    private FragmentDetalleTorneoBinding vb;
    private DetalleTorneoViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        vb = FragmentDetalleTorneoBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(this).get(DetalleTorneoViewModel.class);

        // 1. Obtener argumentos (SafeArgs o Bundle manual por ahora)
        if (getArguments() != null) {
            Evento evento = (Evento) getArguments().getSerializable("evento");
            if (evento != null) {
                vm.setEvento(evento);
            }
        }

        // 2. Toolbar Back
        vb.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        // 3. Observar Datos del Evento
        vm.getEvento().observe(getViewLifecycleOwner(), e -> {
            if (e == null)
                return;

            vb.tvTitulo.setText(e.nombre);
            vb.tvFecha.setText("📅 " + (e.fechaInicio != null ? e.fechaInicio : "Fecha por confirmar"));
            vb.tvDescripcion.setText(e.descripcion != null ? e.descripcion : "Sin descripción disponible.");
            vb.tvLugar.setText(e.lugar != null ? e.lugar : "Ubicación por confirmar");

            // Cargar imagen
            String seed = e.nombre != null ? e.nombre.replaceAll("\\s+", "") : "default";
            Glide.with(this)
                    .load("https://picsum.photos/seed/" + seed + "/800/400")
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(vb.ivHeader);
        });

        // 4. Botón Cómo llegar
        vb.btnComoLlegar.setOnClickListener(v -> vm.abrirMapa());

        // 5. Observar Intent de Mapa
        vm.getMapIntent().observe(getViewLifecycleOwner(), intent -> {
            if (intent != null) {
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Google Maps no está instalado", Toast.LENGTH_SHORT).show();
                }
                vm.onMapOpened();
            }
        });

        // 6. Configurar FAB (Futuro)
        vb.fabInscribir.setOnClickListener(
                v -> Toast.makeText(getContext(), "Próximamente: Inscripciones", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vb = null;
    }
}
