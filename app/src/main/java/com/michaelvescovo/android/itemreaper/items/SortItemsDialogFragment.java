package com.michaelvescovo.android.itemreaper.items;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.michaelvescovo.android.itemreaper.R;

/**
 * Created by Michael Vescovo.
 */

public class SortItemsDialogFragment extends AppCompatDialogFragment {

    private static final int SORT_BY_EXPIRY = 0;
    private static final int SORT_BY_PURCHASE_DATE = 1;

    SortItemsDialogListener mListener;
    int mCurrentSort;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentSort = SORT_BY_EXPIRY;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_title)
                .setSingleChoiceItems(R.array.sort_methods, mCurrentSort,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case SORT_BY_EXPIRY:
                                        mListener.onSortByExpirySelected();
                                        break;
                                    case SORT_BY_PURCHASE_DATE:
                                        mListener.onSortByPurchaseDateSelected();
                                        break;
                                }
                            }
                        })
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SortItemsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SortItemsDialogListener");
        }
    }

    interface SortItemsDialogListener {
        public void onSortByExpirySelected();

        public void onSortByPurchaseDateSelected();
    }
}
