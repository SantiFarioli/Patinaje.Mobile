package com.santisoft.patinajemobile.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.santisoft.patinajemobile.R;
import com.santisoft.patinajemobile.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding vb;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        // Toolbar
        setSupportActionBar(vb.topAppBar);
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

        // ✅ Obtener NavController de forma segura con NavHostFragment
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Vincular Toolbar y BottomNav con Navigation
            NavigationUI.setupActionBarWithNavController(this, navController);
            NavigationUI.setupWithNavController(vb.bottomNav, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}
