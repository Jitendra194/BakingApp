package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.data.RecipeContract;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    public static void setData(String data) {
        ListRemoteViewsFactory.setRecipeName(data);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    @SuppressWarnings("CanBeFinal")
    private Context mContext;
    private static String mRecipeName;
    private Cursor mCursor;

    private static final String[] RECIPE_PROJECTION = {
            RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,
            RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME
    };


    public ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    public static void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    @Override
    public void onDataSetChanged() {

        Log.v("LIST_WIDGET_SERVICE", mRecipeName);
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI.buildUpon()
                .appendPath(RecipeContract.PATH_RECIPE_NAME)
                .appendPath(mRecipeName)
                .build();
        Log.v("URI", uri.toString());
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(uri,
                RECIPE_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (mCursor == null || mCursor.getCount() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_list_item);
        mCursor.moveToPosition(i);
        String ingredientName = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENT_NAME));

        views.setTextViewText(R.id.widget_ingredient_detail_text_view, ingredientName);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}