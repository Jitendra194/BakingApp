package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.fragments.DetailsFragment;
import com.example.android.bakingapp.fragments.MasterListFragment;
import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

public class RecipeStepAndIngredientList extends AppCompatActivity implements MasterListFragment.MasterListListener {

    public static final String RECIPE_NAME = "RECIPE_NAME";

    private ArrayList<RecipeData.Steps> mRecipeStepsData;
    private ArrayList<RecipeData.Ingredients> mRecipeIngredientsData;

    public RecipeStepAndIngredientList() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_and_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        String mRecipeName = intent.getStringExtra(RECIPE_NAME);
        mRecipeStepsData = intent.getParcelableArrayListExtra(MainActivity.RECIPE_DATA_STEPS);
        mRecipeIngredientsData = intent.getParcelableArrayListExtra(MainActivity.RECIPE_DATA_INGREDIENTS);


        getSupportActionBar().setTitle(mRecipeName);

        if (findViewById(R.id.recipe_step_and_ingredient_fragment2) != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            MasterListFragment masterListFragment = MasterListFragment
                    .newInstance(mRecipeIngredientsData, mRecipeStepsData, mRecipeName, true);
            if (fragmentManager.findFragmentById(R.id.recipe_step_and_ingredient_fragment) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_and_ingredient_fragment, masterListFragment)
                        .commit();
            }

            DetailsFragment detailsFragment = DetailsFragment.newInstance(mRecipeIngredientsData, mRecipeStepsData, -1);
            if (fragmentManager.findFragmentById(R.id.recipe_step_and_ingredient_fragment2) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_and_ingredient_fragment2, detailsFragment)
                        .commit();
            }
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MasterListFragment masterListFragment = MasterListFragment
                    .newInstance(mRecipeIngredientsData, mRecipeStepsData, mRecipeName, false);
            if (fragmentManager.findFragmentById(R.id.recipe_step_and_ingredient_fragment) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_and_ingredient_fragment, masterListFragment)
                        .commit();
            }
        }
    }

    @Override
    public void listClickListener(int position) {
        if (findViewById(R.id.recipe_step_and_ingredient_fragment2) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            DetailsFragment detailsFragment = DetailsFragment.newInstance(mRecipeIngredientsData, mRecipeStepsData, position);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_and_ingredient_fragment2, detailsFragment)
                    .commit();
        }
    }

    @Override
    public void ingredientsClickListener() {
        if (findViewById(R.id.recipe_step_and_ingredient_fragment2) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            DetailsFragment detailsFragment = DetailsFragment.newInstance(mRecipeIngredientsData, mRecipeStepsData, -1);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_and_ingredient_fragment2, detailsFragment)
                    .commit();
        }
    }
}
