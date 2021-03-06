package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.michaelvescovo.android.itemreaper.util.Constants.SORT_BY_EXPIRY_STRING;

/**
 * @author Michael Vescovo
 */

class RemoteDataSource implements DataSource {

    private DatabaseReference mDatabase;
    private Query mCurrentItemsQuery;
    private ValueEventListener mItemsListener;
    private String mCurrentSort;

    RemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCurrentSort = SORT_BY_EXPIRY_STRING;
    }

    @Override
    public void getItems(@NonNull String userId, @NonNull String sortBy, @NonNull String caller,
                         @NonNull final GetItemsCallback callback) {
        if (mCurrentItemsQuery == null || !mCurrentSort.equals(sortBy)) {
            mCurrentSort = sortBy;
            mCurrentItemsQuery = mDatabase.child("items").child(userId).child("private")
                    .child("current").orderByChild(sortBy);
        } else if (mItemsListener != null) {
            mCurrentItemsQuery.removeEventListener(mItemsListener);
        }
        mItemsListener = mCurrentItemsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    items.add(item);
                }
                callback.onItemsLoaded(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull String userId, @NonNull String caller,
                        @NonNull final GetItemCallback callback) {
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);
                callback.onItemLoaded(item);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.removeEventListener(itemListener);
        mDatabase.child("items").child(userId).child("private").child("current").child(itemId)
                .addValueEventListener(itemListener);
    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {
        String key = mDatabase.child("items").child(userId).child("private").child("current")
                .push().getKey();
        callback.onNewItemIdLoaded(key);
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        if (item.getDeceased()) {
            mDatabase.child("items").child(userId).child("private").child("current")
                    .child(item.getId()).removeValue();
            mDatabase.child("items").child(userId).child("private").child("expired")
                    .child(item.getId()).setValue(item);
        } else {
            mDatabase.child("items").child(userId).child("private").child("expired")
                    .child(item.getId()).removeValue();
            mDatabase.child("items").child(userId).child("private").child("current")
                    .child(item.getId()).setValue(item);
        }
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        if (item.getDeceased()) {
            mDatabase.child("items").child(userId).child("private").child("expired")
                    .child(item.getId()).removeValue();
        } else {
            mDatabase.child("items").child(userId).child("private").child("current")
                    .child(item.getId()).removeValue();
        }
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        mDatabase.child("items").child(userId).removeValue();
    }
}
