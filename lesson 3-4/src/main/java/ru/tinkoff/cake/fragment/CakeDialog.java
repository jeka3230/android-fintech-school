package ru.tinkoff.cake.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class CakeDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static DialogFragment newInstance(String title, String message) {
        Bundle b = new Bundle();
        b.putString(EXTRA_TITLE, title);
        b.putString(EXTRA_MESSAGE, message);
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.setArguments(b);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String title = arguments.getString(EXTRA_TITLE);
        String message = arguments.getString(EXTRA_MESSAGE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
