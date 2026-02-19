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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.data.model.patinadoras.PatinadoraDetail;
import com.santisoft.patinajemobile.databinding.FragmentDetallePatinadoraBinding;

public class DetallePatinadoraFragment extends Fragment {

    private FragmentDetallePatinadoraBinding binding;
    private DetallePatinadoraViewModel viewModel;
    private int currentPatinadorId = -1; // Para guardar el ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetallePatinadoraBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        // 👉 obtener el ID pasado desde el listado
        if (getArguments() != null) {
            currentPatinadorId = getArguments().getInt("patinadorId", -1);
        }

        if (currentPatinadorId != -1) {
            viewModel.loadPatinador(currentPatinadorId);
        }

        // observar el detalle
        viewModel.getPatinador().observe(getViewLifecycleOwner(), this::cargarHeader);

        // 👇 Click en el FAB para agregar evaluación
        binding.fabAddEvaluacion.setOnClickListener(v -> {
            if (currentPatinadorId != -1) {
                Bundle args = new Bundle();
                args.putInt("patinadorId", currentPatinadorId);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_detalle_to_agregar_evaluacion, args);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar tabs
        setupTabs();

        // Validar estado inicial del FAB
        if (binding.tabLayout.getSelectedTabPosition() == 3) {
            binding.fabAddEvaluacion.show();
        } else {
            binding.fabAddEvaluacion.hide();
        }

        // Listener para cambios de tab
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 3) {
                    binding.fabAddEvaluacion.show();
                } else {
                    binding.fabAddEvaluacion.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void cargarHeader(PatinadoraDetail p) {
        if (p == null)
            return;

        // Nombre
        binding.tvNombre.setText(p.nombre + " " + p.apellido);

        // Categoría
        binding.chipCategoria.setText(p.categoria != null ? p.categoria : "—");

        // Estado
        binding.chipEstado.setText(p.activo ? "Activa" : "Inactiva");
        if (p.activo) {
            binding.chipEstado.setChipBackgroundColorResource(R.color.success);
            binding.chipEstado.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            binding.chipEstado.setChipBackgroundColorResource(R.color.warning);
            binding.chipEstado.setTextColor(getResources().getColor(android.R.color.black));
        }

        // 👇 Buscamos la foto local basándonos en el NOMBRE de la patinadora
        String nombreFoto = "";
        if (p.nombre != null) {
            // Pasamos a minúsculas y sacamos acentos
            String nombreSeguro = p.nombre.toLowerCase()
                    .replace("í", "i")
                    .replace(" ", "_")
                    .trim();
            nombreFoto = "foto_" + nombreSeguro;
        }

        int resId = requireContext().getResources().getIdentifier(
                nombreFoto,
                "drawable",
                requireContext().getPackageName()
        );

        if (resId != 0) {
            Glide.with(this)
                    .load(resId)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.imgFoto);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.imgFoto);
        }
    }

    private void setupTabs() {
        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        DetallePagerAdapter adapter = new DetallePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText("Datos");
            else if (position == 1)
                tab.setText("Asistencias");
            else if (position == 2)
                tab.setText("Pagos");
            else if (position == 3)
                tab.setText("Evaluaciones");
        }).attach();
    }
}