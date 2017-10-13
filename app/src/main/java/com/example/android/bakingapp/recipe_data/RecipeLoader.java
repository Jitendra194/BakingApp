package com.example.android.bakingapp.recipe_data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.bakingapp.data.RecipeContract;
import com.example.android.bakingapp.retrofit_services.ApiClient;
import com.example.android.bakingapp.retrofit_services.ApiService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by jiten on 9/4/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class RecipeLoader extends AsyncTaskLoader<ArrayList<RecipeData>> {

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<RecipeData> loadInBackground() {
        ApiService service = ApiClient.getClient().create(ApiService.class);
        Call<ArrayList<RecipeData>> call = service.getRecipeData();
        try {
            ArrayList<RecipeData> mRecipeData = call.execute().body();

            Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;

            Cursor mCursor = getContext().getContentResolver().query(uri, null, null, null, null);

            if (mCursor != null && mCursor.getCount() == 0) {


                int size = 0;
                int contentValuesCount = 0;

                for (int j = 0; j < mRecipeData.size(); j++) {
                    for (int i = 0; i < mRecipeData.get(j).getIngredients().size(); i++) {
                        size++;
                    }
                }
                ContentValues[] contentValues = new ContentValues[size];
                for (int j = 0; j < mRecipeData.size(); j++) {
                    for (int i = 0; i < mRecipeData.get(j).getIngredients().size(); i++) {
                        ContentValues values = new ContentValues();
                        values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, mRecipeData.get(j).getName());
                        values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME,
                                mRecipeData.get(j).getIngredients().get(i).getIngredient());
                        contentValues[contentValuesCount] = values;
                        contentValuesCount++;
                    }
                    if (contentValuesCount == size) {
                        break;
                    }
                }
                getContext().getContentResolver().bulkInsert(uri, contentValues);
            }
            mCursor.close();
            return mRecipeData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
