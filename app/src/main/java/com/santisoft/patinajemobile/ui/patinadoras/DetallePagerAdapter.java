package com.santisoft.patinajemobile.ui.patinadoras;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetallePagerAdapter extends FragmentStateAdapter {

    public DetallePagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new DatosPatinadoraFragment();
            case 1: return new AsistenciasPatinadoraFragment();  // 👈 Asistencias en el tab correcto
            case 2: return new PagosPatinadoraFragment();
            case 3: return new EvaluacionesPatinadoraFragment();
            default: return new DatosPatinadoraFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Número de pestañas
    }
}
