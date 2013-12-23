package com.rsxsoftware.insurance.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
* Created by steve.fiedelberg on 12/21/13.
*/
class ProgressFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String title = args.getString("title");
        return new ProgressDialog.Builder(getActivity()).setTitle(title + "...").create();

    }
}
