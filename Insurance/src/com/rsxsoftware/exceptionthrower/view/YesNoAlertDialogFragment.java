package com.rsxsoftware.exceptionthrower.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

/**
* Created by steve.fiedelberg on 12/27/13.
*/
class YesNoAlertDialogFragment extends DialogFragment {

    private final String title;
    private final DialogInterface.OnClickListener onClickListener;

    public YesNoAlertDialogFragment(String title, DialogInterface.OnClickListener onClickListener) {

        this.title = title;
        this.onClickListener = onClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("Are you sure?");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("Yes", onClickListener);
        alertDialogBuilder.setNegativeButton("Not", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();

    }


    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialog");
    }
}
