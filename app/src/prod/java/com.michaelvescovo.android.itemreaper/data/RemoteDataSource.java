package com.michaelvescovo.android.itemreaper.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Vescovo
 */

class RemoteDataSource implements DataSource {

    private DatabaseReference mDatabase;

    RemoteDataSource() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void getItemIds(@NonNull String userId, @NonNull final GetItemIdsCallback callback) {
        DatabaseReference itemIds = FirebaseDatabase.getInstance()
                .getReference("users/" + userId + "/itemIds/");
        itemIds.keepSynced(true);

        String path = "users/" + userId + "/itemIds/";
        ValueEventListener itemIdsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    callback.onItemIdsLoaded(null, false);
                } else {
                    GenericTypeIndicator<Map<String, Boolean>> t =
                            new GenericTypeIndicator<Map<String, Boolean>>() {
                            };
                    Map<String, Boolean> itemIds = dataSnapshot.getValue(t);
                    if (itemIds != null) {
                        callback.onItemIdsLoaded(Lists.newArrayList(itemIds.keySet()), false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.removeEventListener(itemIdsListener);
        mDatabase.child(path).addValueEventListener(itemIdsListener);
    }

    @Override
    public void refreshItemIds() {
        // Nothing to do here.
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
        mDatabase.child("items").child(userId).child("private").child(itemId)
                .addValueEventListener(itemListener);
    }

    @Override
    public void getNewItemId(@NonNull String userId, @NonNull GetNewItemIdCallback callback) {
        String key = mDatabase.child("items").child(userId).child("private").push().getKey();
        callback.onNewItemIdLoaded(key);
    }

    @Override
    public void refreshItems() {
        // Nothing to do here.
    }

    @Override
    public void refreshItem(@NonNull String itemId) {
        // Nothing to do here.
    }

    @Override
    public void saveItem(@NonNull String userId, @NonNull Item item) {
        Map<String, Object> itemValues = item.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userId + "/itemIds/" + item.getId(), true);
        childUpdates.put("/items/" + userId + "/private/" + item.getId(), itemValues);
        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void deleteItem(@NonNull String userId, @NonNull Item item) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userId + "/itemIds/" + item.getId(), null);
        childUpdates.put("/items/" + userId + "/private/" + item.getId(), null);
        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void deleteAllItems(@NonNull String userId) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + userId + "/itemIds/", null);
        mDatabase.updateChildren(childUpdates);
    }
}
