package com.santisoft.patinajemobile.data.remote;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    public interface TokenProvider {
        @Nullable String getToken();
    }

    private final TokenProvider provider;

    public AuthInterceptor(TokenProvider provider) {
        this.provider = provider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String token = provider.getToken();
        if (token != null && !token.isEmpty()) {
            Request authed = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(authed);
        }
        return chain.proceed(original);
    }
}
