package com.android.flowerapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.flowerapp.api.FlowerApiClient;
import com.android.flowerapp.api.FlowerApiService;
import com.android.flowerapp.helpers.FlowerModelHelper;
import com.android.flowerapp.models.Flower;
import com.android.flowerapp.models.Flowers;
import com.android.flowerapp.utils.AlertToastUtils;
import com.android.flowerapp.utils.Constants;
import com.android.flowerapp.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akanksha on 15/5/16.
 */
public class FetchDataService extends IntentService {

    public FetchDataService() {
        super(FetchDataService.class.getSimpleName());
    }

    public FetchDataService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadFlowersFromApi();
    }

    private void loadFlowersFromApi() {
        if (NetworkUtils.isInternetConnected(this)) {
            FlowerApiClient apiClient = new FlowerApiClient();
            FlowerApiService apiService = apiClient.getApiService();
            final Call<Flowers> call = apiService.listFlowers();
            if (call != null) {
                call.enqueue(new Callback<Flowers>() {
                    @Override
                    public void onResponse(Call<Flowers> call, Response<Flowers> response) {
                        ArrayList<Flower> flowers = FlowerModelHelper.getFlowersFromData(response.body().getFlowersData());
                        AlertToastUtils.stopProgressDialog();
                        if (flowers.size() > 0) {
                            onResult(flowers);
                        } else {
                            onError("No Results Found");
                        }
                    }

                    @Override
                    public void onFailure(Call<Flowers> call, Throwable t) {
                        AlertToastUtils.stopProgressDialog();
                        onError(t.getMessage());
                    }
                });
            }
        } else {
            AlertToastUtils.stopProgressDialog();
        }
    }

    private void onResult(ArrayList<Flower> flowers) {
        Intent intent = new Intent(Constants.FETCHED_API_SUCCESS);
        intent.putParcelableArrayListExtra(Constants.FETCHED_API_RESULT, flowers);
        LocalBroadcastManager.getInstance(FetchDataService.this)
                .sendBroadcast(intent);
    }

    private void onError(String errMessage) {
        Intent intent = new Intent(Constants.FETCHED_API_FAILURE);
        intent.putExtra(Constants.FETCHED_API_ERROR, errMessage);
        LocalBroadcastManager.getInstance(FetchDataService.this)
                .sendBroadcast(intent);
    }
}
