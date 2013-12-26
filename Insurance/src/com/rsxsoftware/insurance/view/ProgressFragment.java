package com.rsxsoftware.insurance.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
* Created by steve.fiedelberg on 12/21/13.
*/
class ProgressFragment extends DialogFragment {

    public ProgressFragment(FragmentManager fragmentManager, String title) {
        final Bundle args = new Bundle();
        args.putString("title", title);
        setArguments(args);
        show(fragmentManager.beginTransaction(), "dialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String title = args.getString("title");
        return new ProgressDialog.Builder(getActivity()).setTitle(title + "...").create();

    }
}
