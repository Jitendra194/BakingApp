package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.IngredientsAndStepDetailsActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeStepAndIngredientList;
import com.example.android.bakingapp.StepsAdapter;
import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

/**
 * Created by jiten on 9/5/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MasterListFragment extends Fragment implements StepsAdapter.OnStepClickListener {

    private static final String INGREDIENTS = "INGREDIENTS";
    private static final String STEPS = "STEPS";
    private static final String TWO_PANE_LAYOUT = "TWO_PANE_LAYOUT";

    private String mRecipeName;

    private ArrayList<RecipeData.Steps> mRecipeStepsData;
    private ArrayList<RecipeData.Ingredients> mRecipeIngredientsData;
    private boolean isTwoPaneLayout = false;

    private MasterListListener mCallback;

    public MasterListFragment() {
    }

    public interface MasterListListener {
        void listClickListener(int position);

        void ingredientsClickListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MasterListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeStepsData = getArguments()
                    .getParcelableArrayList(STEPS);
            mRecipeIngredientsData = getArguments().getParcelableArrayList(INGREDIENTS);
            mRecipeName = getArguments().getString(RecipeStepAndIngredientList.RECIPE_NAME);
            isTwoPaneLayout = getArguments().getBoolean(TWO_PANE_LAYOUT, false);
        }
    }

    public static MasterListFragment newInstance(ArrayList<RecipeData.Ingredients> ingredients,
                                                 ArrayList<RecipeData.Steps> steps, String recipeName, boolean isTwoPane) {
        MasterListFragment fragment = new MasterListFragment();
        Bundle args = new Bundle();
        if (ingredients != null) {
            args.putParcelableArrayList(INGREDIENTS, ingredients);
        }
        if (steps != null) {
            args.putParcelableArrayList(STEPS, steps);
        }
        args.putString(RecipeStepAndIngredientList.RECIPE_NAME, recipeName);
        args.putBoolean(TWO_PANE_LAYOUT, isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        CardView mIngredientsCardView = rootView.findViewById(R.id.ingredients_card_view);
        RecyclerView mStepsRecyclerView = rootView.findViewById(R.id.recipe_steps_recycler_view);
        LinearLayoutManager mLinearLayoutManager = new
                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(mLinearLayoutManager);
        StepsAdapter mStepsAdapter = new StepsAdapter(mRecipeStepsData, this);
        mStepsRecyclerView.setAdapter(mStepsAdapter);

        TextView mRecipeNameView = rootView.findViewById(R.id.title_text_view);
        mRecipeNameView.setText(mRecipeName);
        mIngredientsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTwoPaneLayout) {
                    Intent ingredientDetailsIntent =
                            new Intent(getContext(), IngredientsAndStepDetailsActivity.class);
                    ingredientDetailsIntent.putExtra(RecipeStepAndIngredientList.RECIPE_NAME, mRecipeName);
                    ingredientDetailsIntent
                            .putParcelableArrayListExtra(IngredientsAndStepDetailsActivity.INGREDIENT_DATA,
                                    mRecipeIngredientsData);
                    ingredientDetailsIntent.putExtra(IngredientsAndStepDetailsActivity.STEP_POSITION_DATA, -1);
                    startActivity(ingredientDetailsIntent);
                } else {
                    mCallback.ingredientsClickListener();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onClickListener(int position) {
        if (!isTwoPaneLayout) {
            Intent stepsIntent = new Intent(getContext(), IngredientsAndStepDetailsActivity.class);
            stepsIntent.putExtra(RecipeStepAndIngredientList.RECIPE_NAME, mRecipeName);
            stepsIntent.putParcelableArrayListExtra(
                    IngredientsAndStepDetailsActivity.STEP_DATA, mRecipeStepsData);
            stepsIntent.putExtra(IngredientsAndStepDetailsActivity.STEP_POSITION_DATA, position);
            startActivity(stepsIntent);
        } else {
            mCallback.listClickListener(position);
        }
    }
}
