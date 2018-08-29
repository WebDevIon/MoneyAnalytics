package com.example.android.moneyanalytics.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.ui.AddExpenseActivity;
import com.example.android.moneyanalytics.ui.AddGoalActivity;
import com.example.android.moneyanalytics.ui.AddIncomeActivity;
import com.example.android.moneyanalytics.ui.SavingsFragment;

/**
 * Implementation of App Widget functionality.
 */
public class MoneyAnalyticsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(AddGoalActivity.PREFS_NAME, 0);
        String savings = prefs.getString(AddGoalActivity.SAVING_GOAL_KEY, SavingsFragment.NO_GOAL_SET);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.money_analytics_widget);
        views.setTextViewText(R.id.widget_saving_tv, savings);

        // Set the pending intent for the Add Income activity.
        Intent incomeIntent = new Intent(context, AddIncomeActivity.class);
        PendingIntent incomePendingIntent = PendingIntent.getActivity(context,
                0, incomeIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_add_income_btn, incomePendingIntent);

        // Set the pending intent for the Add Expense activity.
        Intent expenseIntent = new Intent(context, AddExpenseActivity.class);
        PendingIntent expensePendingIntent = PendingIntent.getActivity(context,
                0, expenseIntent, 0);
        views.setOnClickPendingIntent(R.id.widget_add_expense_btn, expensePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

