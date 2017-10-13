package com.example.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.IngredientDetailsAdapter;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

public class DetailsFragment extends Fragment {

    private static final String INGREDIENTS = "INGREDIENTS";
    private static final String STEPS = "STEPS";
    private static final String STEP_POSITION = "STEP_POSITION";


    private ArrayList<RecipeData.Ingredients> mRecipeIngredientsData;
    private ArrayList<RecipeData.Steps> mRecipeStepsData;
    private int stepPosition;

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(ArrayList<RecipeData.Ingredients> ingredients,
                                              ArrayList<RecipeData.Steps> steps, int stepPosition) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        if (ingredients != null) {
            args.putParcelableArrayList(INGREDIENTS, ingredients);
            args.putInt(STEP_POSITION, stepPosition);
        }
        if (steps != null) {
            args.putParcelableArrayList(STEPS, steps);
            args.putInt(STEP_POSITION, stepPosition);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeIngredientsData = getArguments().getParcelableArrayList(INGREDIENTS);
            mRecipeStepsData = getArguments().getParcelableArrayList(STEPS);
            stepPosition = getArguments().getInt(STEP_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = null;
        if (mRecipeIngredientsData != null && stepPosition == -1) {
            rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
            RecyclerView mIngredientsRecyclerView = rootView.findViewById(R.id.ingredient_recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mIngredientsRecyclerView.setLayoutManager(linearLayoutManager);
            IngredientDetailsAdapter mIngredientsAdapter = new IngredientDetailsAdapter(mRecipeIngredientsData);
            mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        } else if (mRecipeStepsData != null) {
            rootView = inflater.inflate(R.layout.fragment_steps, container, false);
            FragmentManager fragmentManager = getFragmentManager();
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mRecipeStepsData, stepPosition);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_fragment_container, stepDetailFragment)
                    .commit();
        }
        return rootView;
    }
}
