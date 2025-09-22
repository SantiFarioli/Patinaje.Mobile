package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationBarView;
import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding vb;
    private HomeViewModel vm;
    private EventosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        // === Toolbar (MaterialToolbar) ===
        setSupportActionBar(vb.topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Patinaje Artístico • Inicio");
        }

        // Toolbar actions (notificaciones / perfil)
        vb.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_notifications) {
                Toast.makeText(this, "Notificaciones", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.action_profile) {
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Acciones rápidas
        vb.cardPatinadoras.setOnClickListener(v -> toast("Patinadoras"));
        vb.cardTorneos.setOnClickListener(v -> toast("Torneos"));
        vb.cardPagos.setOnClickListener(v -> toast("Pagos"));
        vb.cardEvaluaciones.setOnClickListener(v -> toast("Evaluaciones"));

        // Búsqueda
        vb.tilSearch.setEndIconOnClickListener(v -> {
            String q = String.valueOf(vb.etSearch.getText()).trim();
            vm.buscar(q);
        });

        // Lista eventos
        adapter = new EventosAdapter();
        vb.recyclerEventos.setLayoutManager(new LinearLayoutManager(this));
        vb.recyclerEventos.setAdapter(adapter);

        // Bottom navigation
        vb.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    return true;
                } else if (item.getItemId() == R.id.nav_center) {
                    toast("Centro");
                    return true;
                } else if (item.getItemId() == R.id.nav_agenda) {
                    toast("Agenda");
                    return true;
                }
                return false;
            }
        });

        // FAB
        vb.fabAdd.setOnClickListener(v -> toast("Acción rápida (+)"));

        // VM
        vm = new ViewModelProvider(this).get(HomeViewModel.class);
        vm.summary.observe(this, s -> {
            if (s == null) return;
            vb.tvCountPatinadoras.setText(String.valueOf(s.totalPatinadoras));
            vb.tvCountEventos.setText(String.valueOf(s.eventosSemana));
        });
        vm.eventos.observe(this, list -> {
            boolean vacio = (list == null || list.isEmpty());
            vb.lblNoEventos.setVisibility(vacio ? View.VISIBLE : View.GONE);
            adapter.submit(list);
        });
    }

    private void toast(String s) { Toast.makeText(this, s, Toast.LENGTH_SHORT).show(); }
}
