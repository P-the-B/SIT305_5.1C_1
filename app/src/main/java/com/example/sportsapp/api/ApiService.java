package com.example.sportsapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// defines endpoints
public interface ApiService {

    @GET("v2/top-headlines")
    Call<NewsResponse> getSportsNews(
            @Query("category") String category,
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );
}