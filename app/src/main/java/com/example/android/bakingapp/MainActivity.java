package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.android.bakingapp.recipe_data.RecipeData;
import com.example.android.bakingapp.recipe_data.RecipeLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<RecipeData>>,
        RecipeAdapter.BakingItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;

    private ArrayList<RecipeData> mRecipeData;

    public static final String RECIPE_DATA_STEPS = "RECIPE_DATA_STEPS";
    public static final String RECIPE_DATA_INGREDIENTS = "RECIPE_DATA_INGREDIENTS";

    private static final int RECIPE_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recipe_recycler_view);

        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this), LinearLayoutManager.VERTICAL, false);

        getSupportLoaderManager().initLoader(RECIPE_LOADER, null, this).forceLoad();
    }

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 250;
        return (int) (dpWidth / scalingFactor);
    }


    @Override
    public Loader<ArrayList<RecipeData>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<RecipeData>> loader, ArrayList<RecipeData> data) {
        if (mRecipeData == null) {
            mRecipeData = data;
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecipeAdapter mAdapter = new RecipeAdapter(data, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<RecipeData>> loader) {

    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        Log.v(TAG, String.valueOf(mRecipeData.get(adapterPosition).getSteps().size()));
        Intent recipeIntent = new Intent(this, RecipeStepAndIngredientList.class);
        recipeIntent.putExtra(RecipeStepAndIngredientList.RECIPE_NAME, mRecipeData.get(adapterPosition).getName());
        recipeIntent.putParcelableArrayListExtra(RECIPE_DATA_STEPS, mRecipeData.get(adapterPosition).getSteps());
        recipeIntent.putParcelableArrayListExtra(RECIPE_DATA_INGREDIENTS, mRecipeData.get(adapterPosition).getIngredients());
        startActivity(recipeIntent);
    }
}
