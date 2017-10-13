package com.example.android.bakingapp.retrofit_services;

import com.example.android.bakingapp.network_utils.NetworkUtils;
import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jiten on 9/4/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public interface ApiService {
    @GET(NetworkUtils.BAKING_JSON_URL)
    Call<ArrayList<RecipeData>> getRecipeData();
}
