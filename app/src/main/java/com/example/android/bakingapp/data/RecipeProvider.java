package com.example.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jiten on 10/8/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
public class RecipeProvider extends ContentProvider {

    private static final int CODE_RECIPE_INGREDIENTS = 100;
    private static final int CODE_RECIPE_INGREDIENTS_WITH_NAME = 101;


    private RecipeDbHelper mRecipeDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE, CODE_RECIPE_INGREDIENTS);
        uriMatcher.addURI(authority, RecipeContract.PATH_RECIPE
                + "/" + RecipeContract.PATH_RECIPE_NAME + "/*", CODE_RECIPE_INGREDIENTS_WITH_NAME);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mRecipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case CODE_RECIPE_INGREDIENTS:
                cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME, strings, s, strings1, null, null, s1);
                break;
            case CODE_RECIPE_INGREDIENTS_WITH_NAME:
                String recipeName = uri.getLastPathSegment();
                String selection = RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + "=?";
                String[] selectionArguments = new String[]{recipeName};

                cursor = db.query(RecipeContract.RecipeEntry.TABLE_NAME,
                        strings,
                        selection,
                        selectionArguments,
                        null,
                        null,
                        s1);
                break;
            default:
                throw new IllegalArgumentException("Query is not supported for " + uri);
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        Log.v("MAIN RECIPE NAME", String.valueOf(values.length));
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE_INGREDIENTS:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, value);
                        Log.v("MAIN RECIPE NAME", value.getAsString(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME));
                        Log.v("MAIN RECIPE NAME", value.getAsString(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME));
                        if (_id != -1) {
                            rowsInserted++;
                            Log.v("ROW_INSERTED", String.valueOf(_id));
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            default:
                break;
        }
        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
