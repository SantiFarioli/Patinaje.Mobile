package com.santisoft.patinajemobile.data.repository;

import android.content.Context;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.patinajemobile.data.local.SessionManager;
import com.santisoft.patinajemobile.data.model.login.LoginRequest;
import com.santisoft.patinajemobile.data.model.login.LoginResponse;
import com.santisoft.patinajemobile.data.remote.AuthApi;
import com.santisoft.patinajemobile.data.remote.RetrofitClient;
import com.santisoft.patinajemobile.util.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthApi api;
    private final SessionManager session;

    public AuthRepository(Context ctx) {
        RetrofitClient rc = RetrofitClient.get(ctx);
        this.api = rc.authApi();
        this.session = new SessionManager(ctx);
    }

    @MainThread
    public LiveData<Resource<LoginResponse>> login(String email, String password) {
        MutableLiveData<Resource<LoginResponse>> live = new MutableLiveData<>();
        live.setValue(Resource.loading());

        api.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override public void onResponse(Call<LoginResponse> call, Response<LoginResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    session.saveToken(resp.body().token);
                    live.setValue(Resource.success(resp.body()));
                } else {
                    live.setValue(Resource.error("Credenciales inválidas"));
                }
            }

            @Override public void onFailure(Call<LoginResponse> call, Throwable t) {
                live.setValue(Resource.error(t.getMessage()));
            }
        });

        return live;
    }

    public void logout() {
        session.clear();
    }
}
