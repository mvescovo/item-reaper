package com.michaelvescovo.android.itemreaper.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsActivity;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.widget.GetWidgetDataService.ACTION_GET_WIDGET_DATA;
import static com.michaelvescovo.android.itemreaper.widget.GetWidgetDataService.EXTRA_APP_WIDGET_IDS;

/**
 * @author Michael Vescovo
 */

public class ItemWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_DATA_UPDATED = "data_updated";
    public static final String ACTION_ITEM_CLICKED = "item_clicked";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_DATA_UPDATED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }

        if (intent.getAction().equals(ACTION_ITEM_CLICKED)) {
            String itemId = intent.getStringExtra(EXTRA_ITEM_ID);
            Intent detailIntent = new Intent(context, ItemDetailsActivity.class);
            detailIntent.putExtra(EXTRA_ITEM_ID, itemId);
            context.startActivity(detailIntent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent getDataIntent = new Intent(context, GetWidgetDataService.class);
        getDataIntent.setAction(ACTION_GET_WIDGET_DATA);
        getDataIntent.putExtra(EXTRA_APP_WIDGET_IDS, appWidgetIds);
        context.startService(getDataIntent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.removeAllViews(appWidgetId);
        }
    }
}
