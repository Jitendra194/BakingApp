package com.example.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

/**
 * Created by jiten on 9/8/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class IngredientDetailsAdapter extends RecyclerView.Adapter<IngredientDetailsAdapter.StepsViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private ArrayList<RecipeData.Ingredients> mRecipeIngredientsData;

    public IngredientDetailsAdapter(ArrayList<RecipeData.Ingredients> ingredients) {
        mRecipeIngredientsData = ingredients;
    }

    @Override
    public IngredientDetailsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipeIngredientsData != null) {
            return mRecipeIngredientsData.size();
        }
        return 0;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        @SuppressWarnings("CanBeFinal")
        private TextView mIngredientTitle;
        @SuppressWarnings("CanBeFinal")
        private TextView mIngredientMeasure;
        private TextView mIngredientQuantity;

        public StepsViewHolder(View itemView) {
            super(itemView);

            mIngredientTitle = itemView.findViewById(R.id.ingredient_detail_text_view);
            mIngredientMeasure = itemView.findViewById(R.id.measure_detail_text_view);
            mIngredientQuantity = itemView.findViewById(R.id.quantity_detail_text_view);
        }

        public void bind(int position) {

            String ingredientName = mRecipeIngredientsData.get(position).getIngredient();
            String ingredientQuantity = String.valueOf(mRecipeIngredientsData.get(position).getQuantity());
            String ingredientMeasure = mRecipeIngredientsData.get(position).getMeasure();


            mIngredientTitle.setText(ingredientName);
            mIngredientQuantity.setText(ingredientQuantity);
            mIngredientMeasure.setText(ingredientMeasure);
        }
    }
}
