package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.example.android.bakingapp.recipe_data.RecipeData;
import com.example.android.bakingapp.services.BakingIntentService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<String> recipeNames
            , int recipeIndex, int recipeListSize, int appWidgetId, ArrayList<RecipeData> recipeData) {
        RemoteViews remoteViews;
        remoteViews = setUpWidget(context, recipeNames, recipeIndex, recipeListSize, recipeData);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews setUpWidget(Context context, ArrayList<String> recipeNames,
                                           int recipeIndex, int recipeListSize, ArrayList<RecipeData> recipeData) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        if (recipeIndex < recipeListSize && recipeIndex > -1) {

            if (recipeIndex == 0) {
                views.setViewVisibility(R.id.widget_previous, View.INVISIBLE);
                views.setViewVisibility(R.id.widget_next, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.widget_previous, View.VISIBLE);
            }
            if (recipeIndex == recipeListSize - 1) {
                views.setViewVisibility(R.id.widget_next, View.INVISIBLE);
                views.setViewVisibility(R.id.widget_previous, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.widget_next, View.VISIBLE);
            }

            views.setTextViewText(R.id.widget_recipe_title, recipeNames.get(recipeIndex));

            if (recipeIndex > 0) {
                Intent previousIntent = new Intent(context, BakingIntentService.class);
                previousIntent.setAction(BakingIntentService.ACTION_INGREDIENTS_PREVIOUS);
                previousIntent.putExtra(BakingIntentService.RECIPE_POSITION, recipeIndex - 1);
                previousIntent.putExtra(BakingIntentService.RECIPE_SIZE, recipeListSize);
                previousIntent.putExtra(BakingIntentService.RECIPE_NAMES, recipeNames);
                PendingIntent previousPendingIntent = PendingIntent.getService(context, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_previous, previousPendingIntent);
            }

            if (recipeIndex < recipeListSize) {
                Intent nextIntent = new Intent(context, BakingIntentService.class);
                nextIntent.setAction(BakingIntentService.ACTION_INGREDIENTS_NEXT);
                nextIntent.putExtra(BakingIntentService.RECIPE_POSITION, recipeIndex + 1);
                nextIntent.putExtra(BakingIntentService.RECIPE_SIZE, recipeListSize);
                nextIntent.putExtra(BakingIntentService.RECIPE_NAMES, recipeNames);
                PendingIntent nextPendingIntent = PendingIntent.getService(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_next, nextPendingIntent);
            }

            Intent recipeIntent = new Intent(context, RecipeStepAndIngredientList.class);
            recipeIntent.putExtra(
                    RecipeStepAndIngredientList.RECIPE_NAME, recipeNames.get(recipeIndex));
            recipeIntent.putParcelableArrayListExtra
                    (MainActivity.RECIPE_DATA_INGREDIENTS, recipeData.get(recipeIndex).getIngredients());
            recipeIntent.putParcelableArrayListExtra
                    (MainActivity.RECIPE_DATA_STEPS, recipeData.get(recipeIndex).getSteps());
            PendingIntent recipeDetailsPendingIntent =
                    PendingIntent.getActivity(context, 0, recipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_recipe_title, recipeDetailsPendingIntent);


            Intent listIntent = new Intent(context, ListWidgetService.class);
            views.setRemoteAdapter(R.id.widget_ingredient_list, listIntent);
        }
        return views;
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,
                                     ArrayList<String> recipeNames, int recipeIndex, int recipeListSize,
                                     int[] appWidgetIds, ArrayList<RecipeData> recipeData) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeNames, recipeIndex, recipeListSize, appWidgetId, recipeData);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakingIntentService.startActionIngredients(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

