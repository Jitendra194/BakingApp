package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.recipe_data.RecipeData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jiten on 9/4/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private ArrayList<RecipeData> mData;

    private final BakingItemClickListener mBakingItemClickListener;

    public RecipeAdapter(ArrayList<RecipeData> data, BakingItemClickListener listener) {
        mData = data;
        mBakingItemClickListener = listener;
    }

    public interface BakingItemClickListener {
        @SuppressWarnings("UnusedParameters")
        void onItemClick(View view, int adapterPosition);
    }


    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @SuppressWarnings("CanBeFinal")
        private TextView mRecipeName;
        @SuppressWarnings("CanBeFinal")
        private ImageView mRecipeImage;
        @SuppressWarnings("CanBeFinal")
        private Context mContext;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mRecipeName = itemView.findViewById(R.id.main_recipe_name);
            mRecipeImage = itemView.findViewById(R.id.main_recipe_image);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            mRecipeName.setText(mData.get(position).name);
            if (!("".equals(mData.get(position).getImage()))) {
                Picasso.with(mContext).load(mData.get(position).getImage()).into(mRecipeImage);
            }
        }

        @Override
        public void onClick(View view) {
            mBakingItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
