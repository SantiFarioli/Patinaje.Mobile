package com.santisoft.patinajemobile.data.remote;

import com.santisoft.patinajemobile.data.model.login.LoginRequest;
import com.santisoft.patinajemobile.data.model.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);
}
