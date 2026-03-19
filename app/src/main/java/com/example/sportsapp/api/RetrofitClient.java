package com.example.sportsapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// handles retrofit instance + api access
public class RetrofitClient {

    private static final String BASE_URL = "https://newsapi.org/";
    private static Retrofit retrofit = null;

    // build retrofit once (singleton)
    private static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    // 🔴 THIS IS WHAT YOUR HomeFragment EXPECTS
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}