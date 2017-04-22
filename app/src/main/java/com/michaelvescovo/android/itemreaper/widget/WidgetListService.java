package com.michaelvescovo.android.itemreaper.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michaelvescovo.android.itemreaper.ItemReaperApplication;
import com.michaelvescovo.android.itemreaper.R;
import com.michaelvescovo.android.itemreaper.data.DataSource;
import com.michaelvescovo.android.itemreaper.data.Item;
import com.michaelvescovo.android.itemreaper.data.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static com.michaelvescovo.android.itemreaper.edit_item.EditItemActivity.EXTRA_ITEM_ID;
import static com.michaelvescovo.android.itemreaper.util.MiscHelperMethods.getPriceFromTotalCents;

/**
 * @author Michael Vescovo
 */

public class WidgetListService extends RemoteViewsService {

    @Inject
    public Repository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerWidgetComponent.builder()
                .repositoryComponent(((ItemReaperApplication) getApplication())
                        .getRepositoryComponent())
                .build()
                .inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    private class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private List<Item> mItems;
        private CountDownLatch mCountDownLatch;

        ListRemoteViewsFactory(Context context) {
            mContext = context;
            mItems = new ArrayList<>();
        }

        @Override
        public void onCreate() {
            // Nothing to do here.
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public void onDataSetChanged() {
            mCountDownLatch = new CountDownLatch(1);
            getItems();
            try {
                mCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void getItems() {
            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                mRepository.getItemsList(firebaseUser.getUid(),
                        new DataSource.GetItemsListCallback() {
                            @Override
                            public void onItemsLoaded(@Nullable List<Item> items) {
                                if (mCountDownLatch.getCount() == 0) {
                                    // Item changed externally. Initiate refresh.
                                    Intent updateWidgetIntent = new Intent(mContext,
                                            ItemWidgetProvider.class);
                                    updateWidgetIntent.setAction(
                                            ItemWidgetProvider.ACTION_DATA_UPDATED);
                                    mContext.sendBroadcast(updateWidgetIntent);
                                } else {
                                    if (items != null) {
                                        mItems = ImmutableList.copyOf(items);
                                    }
                                    mCountDownLatch.countDown();
                                }
                            }
                        });
            } else {
                mCountDownLatch.countDown();
            }
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            if (position < mItems.size()) {
                String category = mItems.get(position).getCategory();
                if (category == null || category.equals("")) {
                    rv.setTextViewText(R.id.category, getString(R.string.edit_category_empty));
                } else {
                    rv.setTextViewText(R.id.category, category);
                }
                String type = mItems.get(position).getType();
                if (type == null || type.equals("")) {
                    rv.setTextViewText(R.id.type, getString(R.string.edit_type_empty));
                } else {
                    rv.setTextViewText(R.id.type, type);
                }
                String colour = mItems.get(position).getMainColour();
                if (colour == null || colour.equals("")) {
                    rv.setTextViewText(R.id.colour, getString(R.string.edit_main_colour_empty));
                } else {
                    rv.setTextViewText(R.id.colour, colour);
                }
                long expiry = mItems.get(position).getExpiry();
                if (expiry == -1) {
                    rv.setTextViewText(R.id.expiry, getString(R.string.edit_expiry_date_empty));
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(expiry);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(
                            R.string.edit_date_format), Locale.ENGLISH);
                    String expiryString = simpleDateFormat.format(calendar.getTime());
                    rv.setTextViewText(R.id.expiry, expiryString);
                    Calendar almostExpiredDate = Calendar.getInstance();
                    almostExpiredDate.add(Calendar.MONTH, 1);
                    if (calendar.compareTo(almostExpiredDate) < 1) {
                        rv.setInt(R.id.expiry, "setTextColor", ContextCompat.getColor(mContext,
                                R.color.red));
                    } else {
                        TypedValue typedValue = new TypedValue();
                        TypedArray typedArray = mContext.obtainStyledAttributes(
                                typedValue.data, new int[]{android.R.attr.textColorPrimary}
                        );
                        int textColourPrimary = typedArray.getColor(0, 0);
                        typedArray.recycle();
                        rv.setInt(R.id.expiry, "setTextColor", textColourPrimary);
                    }
                }
                int price = mItems.get(position).getPricePaid();
                String priceString;
                if (price == -1) {
                    priceString = getString(R.string.edit_price_paid_empty);
                } else {
                    priceString = getString(R.string.edit_price_paid_prefix)
                            + getPriceFromTotalCents(price);
                }
                rv.setTextViewText(R.id.paid, priceString);

                Intent fillInIntent = new Intent();
                fillInIntent.putExtra(EXTRA_ITEM_ID, mItems.get(position).getId());
                rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
            }
            return rv;
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

        @Override
        public void onDestroy() {
            // Nothing to do here.
        }
    }
}
