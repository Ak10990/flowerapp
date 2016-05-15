package com.android.flowerapp.api;


import com.android.flowerapp.models.Flowers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FlowerApiService {

    @GET("/NO/Backend/flower.php")
    Call<Flowers> listFlowers();
}