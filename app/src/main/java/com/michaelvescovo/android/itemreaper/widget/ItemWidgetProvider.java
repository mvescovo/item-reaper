package com.michaelvescovo.android.itemreaper.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsActivity;
import com.michaelvescovo.android.itemreaper.items.ItemsActivity;
import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;

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
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

            // WidgetListService intent for populating collection views.
            Intent collectionIntent = new Intent(context, WidgetListService.class);
            rv.setRemoteAdapter(R.id.widget_list, collectionIntent);
            rv.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Intent for opening the main app from the widget title.
            Intent itemReaperIntent = new Intent(context, ItemsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, itemReaperIntent, 0);
            rv.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

            // Collection for ItemDetailsActivity intent when opening individual list item.
            Intent clickIntentTemplate = new Intent(context, ItemWidgetProvider.class);
            clickIntentTemplate.setAction(ACTION_ITEM_CLICKED);
            PendingIntent clickPendingIntentTemplate = PendingIntent.getBroadcast(context, 0,
                    clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
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