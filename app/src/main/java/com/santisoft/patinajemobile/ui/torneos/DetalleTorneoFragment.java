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
        // Asegurar titulo vacio en toolbar porque usamos el del banner
        vb.toolbar.setTitle("");

        // 3. Observar Datos del Evento
        vm.getEvento().observe(getViewLifecycleOwner(), e -> {
            if (e == null)
                return;

            // A. Header Image (Desde VM/Mapper)
            Glide.with(this)
                    .load(vm.getImagenTorneo())
                    .centerCrop()
                    .placeholder(R.drawable.poli_ejemplo)
                    .into(vb.ivHeader);

            // B. Ficha Técnica (Banner Rojo)
            vb.tvTitulo.setText(e.nombre);

            // Fecha
            String fechaTexto = "Fecha por confirmar";
            if (e.fechaInicio != null) {
                try {
                    // Simple parse para mostrar algo limpio
                    String rawFecha = e.fechaInicio.replace("T00:00:00", "").trim();
                    java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd",
                            java.util.Locale.getDefault());
                    java.util.Date date = inputFormat.parse(rawFecha.split("T")[0]);
                    java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd 'de' MMMM, yyyy",
                            java.util.Locale.getDefault());
                    fechaTexto = outputFormat.format(date);
                } catch (Exception ex) {
                    fechaTexto = e.fechaInicio;
                }
            }
            vb.tvFecha.setText(fechaTexto);

            // Ubicación (Ciudad)
            vb.tvUbicacion.setText(e.lugar != null ? e.lugar : "Ubicación desconocida");

            // Dirección (Exacta desde VM)
            vb.tvDireccion.setText(vm.getDireccionTorneo());

            // C. Mapa Estatico
            Glide.with(this)
                    .load(R.drawable.como_llegar)
                    .centerCrop()
                    .into(vb.ivMapaStatic);
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

        // 6. Configurar FAB - Navegación a Selección de Atletas
        vb.fabInscribir.setOnClickListener(v -> {
            // CORRECCIÓN: Usamos Navigation y el ID exacto del nav_graph.xml
            androidx.navigation.Navigation.findNavController(v)
                    .navigate(R.id.action_detalleTorneo_to_seleccionAtletas);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        vb = null;
    }
}
