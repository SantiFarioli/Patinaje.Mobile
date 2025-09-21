// com/santisoft/patinajemobile/ui/login/LoginActivity.java
package com.santisoft.patinajemobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.patinajemobile.databinding.ActivityLoginBinding;
import com.santisoft.patinajemobile.ui.main.MainActivity;
import com.santisoft.patinajemobile.util.Resource;

public class LoginActivity extends ComponentActivity {

    private ActivityLoginBinding vb;
    private LoginViewModel vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        vb.btnLogin.setOnClickListener(v -> {
            String email = vb.etEmail.getText().toString().trim();
            String pass  = vb.etPassword.getText().toString().trim();
            doLogin(email, pass);
        });
    }

    private void doLogin(String email, String pass) {
        vm.login(email, pass).observe(this, res -> {
            if (res == null) return;
            if (res.status == Resource.Status.LOADING) {
                vb.progress.setVisibility(View.VISIBLE);
                vb.btnLogin.setEnabled(false);
            } else if (res.status == Resource.Status.SUCCESS) {
                vb.progress.setVisibility(View.GONE);
                vb.btnLogin.setEnabled(true);
                // guardar token si querés, y navegar
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (res.status == Resource.Status.ERROR) {
                vb.progress.setVisibility(View.GONE);
                vb.btnLogin.setEnabled(true);
                Toast.makeText(this, res.message != null ? res.message : "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
