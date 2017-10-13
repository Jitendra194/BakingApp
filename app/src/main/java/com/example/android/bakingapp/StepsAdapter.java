package com.example.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.recipe_data.RecipeData;

import java.util.ArrayList;

/**
 * Created by jiten on 9/5/2017.
 */

@SuppressWarnings({"CanBeFinal", "DefaultFileTemplate"})
public class StepsAdapter extends
        RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private ArrayList<RecipeData.Steps> mRecipeStepsData;

    private OnStepClickListener listener;

    public StepsAdapter(ArrayList<RecipeData.Steps> data, OnStepClickListener listener) {
        mRecipeStepsData = data;
        this.listener = listener;
    }

    public interface OnStepClickListener {
        void onClickListener(int position);
    }

    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.short_recipe_step_item, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeStepsData.size();
    }

    @SuppressWarnings("CanBeFinal")
    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mStepNumber;
        private TextView mStepShortDescription;


        public StepsViewHolder(View itemView) {
            super(itemView);

            mStepNumber = itemView.findViewById(R.id.recipe_step_title_number);
            mStepShortDescription = itemView.findViewById(R.id.recipe_step_short_description);

            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

            int stepNumber = mRecipeStepsData.get(position).getId();
            String stepNumberText = "Step " + stepNumber;
            mStepNumber.setText(stepNumberText);
            mStepShortDescription.setText(mRecipeStepsData.get(position).getShortDescription());
        }

        @Override
        public void onClick(View view) {
            listener.onClickListener(getAdapterPosition());
        }
    }
}
