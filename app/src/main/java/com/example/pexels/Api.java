package com.example.pexels;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Header;
import retrofit2.http.Query;


public interface Api {


    @GET("curated/?page=1&per_page=80")
    Call<PexelResponse> getWallpaper(

            @Header("Authorization") String credentials

    );

    @GET("search?")
    Call<PexelResponse> getSearch(

            @Header("Authorization") String credentials,
            @Query("query") String queryText
    );

}