package com.example.android.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by jiten on 10/8/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.bakingapp";
    @SuppressWarnings("WeakerAccess")
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPE = "recipes";
    public static final String PATH_RECIPE_NAME = RecipeEntry.COLUMN_RECIPE_NAME;

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECIPE);
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_NAME = "recipeName";
        public static final String COLUMN_INGREDIENT_NAME = "ingredientName";
    }
}
