package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.fragments.DetailsFragment;
import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

public class IngredientsAndStepDetailsActivity extends AppCompatActivity {

    public static final String INGREDIENT_DATA = "INGREDIENT_DATA";
    public static final String STEP_DATA = "STEP_DATA";
    public static final String STEP_POSITION_DATA = "STEP_POSITION_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_recipe_step_details);

        Intent data = getIntent();
        String mRecipeName = data.getStringExtra(RecipeStepAndIngredientList.RECIPE_NAME);
        ArrayList<RecipeData.Ingredients> mRecipeIngredientsData = data.getParcelableArrayListExtra(INGREDIENT_DATA);
        ArrayList<RecipeData.Steps> mRecipeStepsData = data.getParcelableArrayListExtra(STEP_DATA);
        int stepPosition = data.getIntExtra(STEP_POSITION_DATA, 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mRecipeName);
        }

        FragmentManager manager = getSupportFragmentManager();
        DetailsFragment detailsFragment = DetailsFragment.newInstance(mRecipeIngredientsData, mRecipeStepsData, stepPosition);
        manager.beginTransaction()
                .replace(R.id.ingredients_and_steps_container, detailsFragment)
                .commit();
    }

}
