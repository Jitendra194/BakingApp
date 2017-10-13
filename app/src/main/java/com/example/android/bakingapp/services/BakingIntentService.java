package com.example.android.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.BakingWidget;
import com.example.android.bakingapp.ListWidgetService;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.RecipeContract;
import com.example.android.bakingapp.recipe_data.RecipeData;
import com.example.android.bakingapp.retrofit_services.ApiClient;
import com.example.android.bakingapp.retrofit_services.ApiService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by jiten on 10/1/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class BakingIntentService extends IntentService {

    public static final String ACTION_INGREDIENTS_PREVIOUS = "ACTION_INGREDIENTS_PREVIOUS";
    public static final String ACTION_INGREDIENTS_NEXT = "ACTION_INGREDIENTS_NEXT";

    public static final String INGREDIENT_POSITION = "INGREDIENT_POSITION";
    public static final String RECIPE_POSITION = "RECIPE_POSITION";
    public static final String RECIPE_SIZE = "RECIPE_SIZE";
    public static final String RECIPE_NAMES = "RECIPE_NAMES";


    private Cursor mCursor;

    private int recipeListSize = 0;
    private int mIngredientPosition;
    private int mRecipePosition;
    private ArrayList<RecipeData> mRecipeData;

    public BakingIntentService() {
        super("BakingIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handleActionUpdateWidget(intent);
    }

    private void handleActionUpdateWidget(Intent intent) {

        getRecipeData();

        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        mCursor = getContentResolver().query(uri, null, null, null, null);
        int numberOfIngredients = 0;
        if (mCursor != null) {
            numberOfIngredients = mCursor.getCount();
        }
        mCursor.moveToPosition(mIngredientPosition);

        if (ACTION_INGREDIENTS_NEXT.equals(intent.getAction())) {
            mIngredientPosition = intent.getIntExtra(INGREDIENT_POSITION, 0);
            mRecipePosition = intent.getIntExtra(RECIPE_POSITION, 0);
        } else if (ACTION_INGREDIENTS_PREVIOUS.equals(intent.getAction())) {
            mIngredientPosition = intent.getIntExtra(INGREDIENT_POSITION, 0);
            mRecipePosition = intent.getIntExtra(RECIPE_POSITION, 0);
        }

        ArrayList<String> mRecipeNames;
        if (intent.getIntExtra(RECIPE_SIZE, 0) == recipeListSize) {
            mRecipeNames = getRecipeListSize(numberOfIngredients);
            recipeListSize = mRecipeNames.size();
        } else {
            mRecipeNames = intent.getStringArrayListExtra(RECIPE_NAMES);
            recipeListSize = intent.getIntExtra(RECIPE_SIZE, 0);
        }

        mCursor.close();

        ListWidgetService.setData(mRecipeData.get(mRecipePosition).getName());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_list);
        BakingWidget.updateWidgets(this, appWidgetManager, mRecipeNames,
                mRecipePosition, recipeListSize, appWidgetIds, mRecipeData);
    }

    public static void startActionIngredients(Context context) {
        Intent intent = new Intent(context, BakingIntentService.class);
        context.startService(intent);
    }

    private ArrayList<String> getRecipeListSize(int numberOfIngredients) {
        ArrayList<String> recipeNames = new ArrayList<>();
        String recipeName1 = "";
        @SuppressWarnings("UnusedAssignment") String recipeName2 = "";
        for (int i = 0; i < numberOfIngredients; i++) {
            if (i == numberOfIngredients - 1) {
                recipeNames.add(recipeName1);
                break;
            }
            mCursor.moveToPosition(i);
            recipeName1 = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
            mCursor.moveToPosition(i + 1);
            recipeName2 = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));


            if (!recipeName1.equals(recipeName2)) {
                recipeNames.add(recipeName1);
            }
        }
        return recipeNames;
    }

    private void getRecipeData() {
        if (mRecipeData == null) {
            ApiService service = ApiClient.getClient().create(ApiService.class);
            Call<ArrayList<RecipeData>> call = service.getRecipeData();
            try {
                mRecipeData = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
