package com.santisoft.patinajemobile.ui.patinadoras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetallePatinadoraBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DetallePatinadoraViewModel.class);

        // 👉 obtener el ID pasado desde el listado
        int id = getArguments().getInt("patinadorId", -1);
        if (id != -1) {
            viewModel.loadPatinador(id); // 👈 método correcto del ViewModel
        }

        // observar el detalle
        viewModel.getPatinador().observe(getViewLifecycleOwner(), this::cargarHeader);

        // configurar tabs
        setupTabs();

        return binding.getRoot();
    }

    private void cargarHeader(PatinadoraDetail p) {
        if (p == null) return;

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

        // Foto
        if (p.fotoUrl != null && !p.fotoUrl.isEmpty()) {
            Glide.with(this)
                    .load(p.fotoUrl)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.imgFoto);
        } else {
            binding.imgFoto.setImageResource(R.drawable.ic_person);
        }
    }

    private void setupTabs() {
        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        DetallePagerAdapter adapter = new DetallePagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Datos");
            else if (position == 1) tab.setText("Asistencias");
            else if (position == 2) tab.setText("Pagos");
            else if (position == 3) tab.setText("Evaluaciones");
        }).attach();
    }
}
