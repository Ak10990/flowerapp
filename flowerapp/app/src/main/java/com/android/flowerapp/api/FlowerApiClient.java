package com.android.flowerapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlowerApiClient {

    /**
     * Api EndPoints
     */
    private static final String API_HOMEPAGE = "http://development.easystartup.org";

    private FlowerApiService apiService;

    public FlowerApiClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_HOMEPAGE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(FlowerApiService.class);
    }

    public FlowerApiService getApiService() {
        return apiService;
    }

}