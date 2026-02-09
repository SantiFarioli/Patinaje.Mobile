package com.santisoft.patinajemobile.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.santisoft.patinajemobile.data.model.login.LoginResponse;
import com.santisoft.patinajemobile.data.repository.AuthRepository;
import com.santisoft.patinajemobile.util.Resource;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository repo;
    private final MutableLiveData<String[]> _loginTrigger = new MutableLiveData<>();
    public final LiveData<Resource<LoginResponse>> loginResult;

    public LoginViewModel(@NonNull Application app) {
        super(app);
        this.repo = new AuthRepository(app);

        this.loginResult = androidx.lifecycle.Transformations.switchMap(_loginTrigger, data -> {
            String email = data[0];
            String password = data[1];
            if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                MutableLiveData<Resource<LoginResponse>> err = new MutableLiveData<>();
                err.setValue(Resource.error("Por favor complete email y contraseña", null));
                return err;
            }
            return repo.login(email, password);
        });
    }

    public void login(String email, String password) {
        _loginTrigger.setValue(new String[] { email, password });
    }
}
