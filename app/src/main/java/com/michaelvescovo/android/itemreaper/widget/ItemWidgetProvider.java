package com.michaelvescovo.android.itemreaper.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity;
import com.michaelvescovo.android.itemreaper.item_details.ItemDetailsActivity;
import com.michaelvescovo.android.itemreaper.items.ItemsActivity;

import static com.michaelvescovo.android.itemreaper.items.ItemsActivity.EXTRA_EDIT_NEW_ITEM;

/**
 * @author Michael Vescovo
 */

public class ItemWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_DATA_UPDATED = "data_updated";
    public static final String ACTION_EDIT_NEW_ITEM = "action_edit_new_item";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_DATA_UPDATED)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        boolean largeScreen = context.getResources().getBoolean(R.bool.large_layout);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

            // WidgetListService intent for populating collection views.
            Intent collectionIntent = new Intent(context, WidgetListService.class);
            rv.setRemoteAdapter(R.id.widget_list, collectionIntent);
            rv.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Intent for opening the main app from the widget title.
            Intent itemReaperIntent = new Intent(context, ItemsActivity.class);
            PendingIntent itemReaperPendingIntent = PendingIntent.getActivity(context, 0,
                    itemReaperIntent, 0);
            rv.setOnClickPendingIntent(R.id.widget_title, itemReaperPendingIntent);

            // Intent for adding an item when clicking the add item button.
            Intent addItemIntent = largeScreen
                    ? new Intent(context, ItemsActivity.class)
                    : new Intent(context, EditItemActivity.class);
            addItemIntent.putExtra(EXTRA_EDIT_NEW_ITEM, true);
            addItemIntent.setAction(ACTION_EDIT_NEW_ITEM);
            PendingIntent addItemPendingIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(addItemIntent)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.widget_add_item, addItemPendingIntent);

            // Collection for ItemDetailsActivity intent when opening individual list item.
            Intent clickIntentTemplate = largeScreen
                    ? new Intent(context, ItemsActivity.class)
                    : new Intent(context, ItemDetailsActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
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
