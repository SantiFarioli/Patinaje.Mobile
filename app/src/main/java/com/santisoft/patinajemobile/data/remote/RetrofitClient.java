package com.santisoft.patinajemobile.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.santisoft.patinajemobile.BuildConfig;            // 👈 import correcto
import com.santisoft.patinajemobile.data.local.SessionManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.100:5100/";

    private static volatile RetrofitClient INSTANCE;

    private final Retrofit retrofit;
    private final AuthApi authApi;
    private final HomeApi homeApi;

    private RetrofitClient(Context ctx) {
        Context app = ctx.getApplicationContext();

        // Session/token
        SessionManager session = new SessionManager(app);

        // Logging
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.BASIC);

        // OkHttp con interceptor de auth y timeouts
        OkHttpClient ok = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(session::getToken))
                .addInterceptor(log)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        // Gson tolerante
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(ok)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        authApi = retrofit.create(AuthApi.class);
        homeApi = retrofit.create(HomeApi.class);
    }

    public static synchronized RetrofitClient get(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitClient(ctx);
        }
        return INSTANCE;
    }

    public Retrofit retrofit() { return retrofit; }

    public AuthApi authApi() { return authApi; }
    public HomeApi homeApi() { return homeApi; }
}
