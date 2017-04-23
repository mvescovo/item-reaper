package com.michaelvescovo.android.itemreaper.items;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.michaelvescovo.android.itemreaper.R;

/**
 * Created by Michael Vescovo.
 */

public class SortItemsDialogFragment extends AppCompatDialogFragment {

    public static final String EXTRA_SORT_BY = "sort_by";
    public static final int SORT_BY_EXPIRY = 0;
    public static final int SORT_BY_PURCHASE_DATE = 1;

    SortItemsDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int currentSort = getArguments().getInt(EXTRA_SORT_BY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.sort_title)
                .setSingleChoiceItems(R.array.sort_methods, currentSort,
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
        void onSortByExpirySelected();

        void onSortByPurchaseDateSelected();
    }
}
