package com.example.android.bakingapp.retrofit_services;

import com.example.android.bakingapp.network_utils.NetworkUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jiten on 9/4/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ApiClient {

    private static Retrofit mRetrofit = null;


    public static Retrofit getClient() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit
                    .Builder()
                    .baseUrl(NetworkUtils.BAKING_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
}
