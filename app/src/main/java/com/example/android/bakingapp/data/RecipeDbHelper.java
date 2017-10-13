package com.example.android.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingapp.data.RecipeContract.RecipeEntry;

/**
 * Created by jiten on 10/8/2017.
 */

@SuppressWarnings({"WeakerAccess", "DefaultFileTemplate"})
public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_STORE_TABLE = "CREATE TABLE "
                + RecipeEntry.TABLE_NAME
                + " ("
                + RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, "
                + RecipeEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_STORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
