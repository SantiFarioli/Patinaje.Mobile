package com.santisoft.patinajemobile.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.santisoft.patinajemobile.data.model.login.LoginResponse;
import com.santisoft.patinajemobile.data.repository.AuthRepository;
import com.santisoft.patinajemobile.util.Resource;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository repo;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        // El repo usa RetrofitClient.get(app) por dentro
        this.repo = new AuthRepository(app);
    }

    public LiveData<Resource<LoginResponse>> login(String email, String password) {
        return repo.login(email, password);
    }
}
