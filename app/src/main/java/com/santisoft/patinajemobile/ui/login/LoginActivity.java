package com.santisoft.patinajemobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.patinajemobile.databinding.ActivityLoginBinding;
import com.santisoft.patinajemobile.ui.main.HomeActivity;
import com.santisoft.patinajemobile.util.Resource;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding vb;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        vb.btnLogin.setOnClickListener(v -> {
            vm.login(
                    vb.etEmail.getText().toString().trim(),
                    vb.etPassword.getText().toString().trim());
        });

        vm.loginResult.observe(this, res -> {
            if (res == null)
                return;

            switch (res.status) {
                case LOADING:
                    vb.progress.setVisibility(View.VISIBLE);
                    vb.btnLogin.setEnabled(false);
                    break;

                case SUCCESS:
                    vb.progress.setVisibility(View.GONE);
                    vb.btnLogin.setEnabled(true);
                    // TODO: guardar token en SharedPreferences si cbRemember está marcado
                    startActivity(
                            new Intent(this, HomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                    break;

                case ERROR:
                    vb.progress.setVisibility(View.GONE);
                    vb.btnLogin.setEnabled(true);
                    Toast.makeText(this,
                            res.message != null ? res.message : "Error en el login",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void doLogin(String email, String pass) {
        // Method deprecated/removed in favor of vm.login triggers
    }
}
