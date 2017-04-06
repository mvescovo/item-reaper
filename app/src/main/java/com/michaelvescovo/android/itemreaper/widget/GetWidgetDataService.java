package com.michaelvescovo.android.itemreaper.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;
import com.michaelvescovo.android.itemreaper.items.ItemsActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.items.ItemsPresenter.ITEMS_CALLER;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.toByteArray;
import static com.michaelvescovo.android.itemreaper.widget.ItemWidgetProvider.ACTION_ITEM_CLICKED;
import static com.michaelvescovo.android.itemreaper.widget.WidgetListService.EXTRA_ITEMS;

/**
 * @author Michael Vescovo
 */

public class GetWidgetDataService extends IntentService {

    public static final String EXTRA_APP_WIDGET_IDS = "com.michaelvescovo.android.itemreaper.widget.extra.app_widget_ids";
    public static final String ACTION_GET_WIDGET_DATA = "com.michaelvescovo.android.itemreaper.widget.action.GET_WIDGET_DATA";

    @Inject
    public Repository mRepository;
    private FirebaseUser mFirebaseUser;
    private int[] mAppWidgetIds;
    private List<Item> mItems;
    private int mCount;

    public GetWidgetDataService() {
        super("GetWidgetDataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerWidgetComponent.builder()
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build()
                .inject(this);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mItems = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_WIDGET_DATA.equals(action)) {
                mAppWidgetIds = intent.getIntArrayExtra(EXTRA_APP_WIDGET_IDS);
                getItems();
            }
        }
    }

    private void getItems() {
        mRepository.getItemIds(mFirebaseUser.getUid(),
                new DataSource.GetItemIdsCallback() {
                    @Override
                    public void onItemIdsLoaded(@Nullable List<String> itemIds, boolean itemRemoved) {
                        if (itemIds != null) {
                            mItems.clear();
                            mCount = itemIds.size();
                            for (final String itemId : itemIds) {
                                getItem(itemId);
                            }
                        }
                    }
                });
    }

    private void getItem(final String itemId) {
        mRepository.getItem(itemId, ITEMS_CALLER, new DataSource.GetItemCallback() {
            @Override
            public void onItemLoaded(@Nullable Item item) {
                if (item != null) {
                    mItems.add(item);
                }
                if (mCount == mItems.size()) {
                    updateWidget(mAppWidgetIds);
                }
            }
        });
    }

    private void updateWidget(int[] appWidgetIds) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget);

            // WidgetListService intent for populating collection views.
            Intent collectionIntent = new Intent(getApplicationContext(), WidgetListService.class);
            byte[] bytes = null;
            try {
                bytes = toByteArray(mItems);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null) {
                collectionIntent.putExtra(EXTRA_ITEMS, bytes);
                rv.setRemoteAdapter(R.id.widget_list, collectionIntent);
                rv.setEmptyView(R.id.widget_list, R.id.widget_empty);
            }

            // Intent for opening the main app from the widget title.
            Intent itemReaperIntent = new Intent(getApplicationContext(), ItemsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, itemReaperIntent, 0);
            rv.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

            // Collection for ItemDetailsActivity intent when opening individual list item.
            Intent clickIntentTemplate = new Intent(getApplicationContext(), ItemWidgetProvider.class);
            clickIntentTemplate.setAction(ACTION_ITEM_CLICKED);
            PendingIntent clickPendingIntentTemplate = PendingIntent.getBroadcast(getApplicationContext(), 0,
                    clickIntentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
        showUpdateTime(getApplicationContext(), appWidgetManager, appWidgetIds);
    }

    /*
    * Show the time data was updated on the widget.
    * */
    private void showUpdateTime(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            Calendar c = Calendar.getInstance();
            DateFormat timeInstance = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            String timeUpdated = context.getString(R.string.time_updated) + timeInstance.format(c.getTime());
            rv.setTextViewText(R.id.time_updated, timeUpdated);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}
