package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by steve.fiedelberg on 12/21/13.
 */
class PredefinedListFragmentDialog extends DialogFragment {
    private final List<Inventory> inventories;

    public PredefinedListFragmentDialog(List<Inventory> inventories) {

        this.inventories = inventories;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        return new AlertDialog.Builder(activity).setTitle(R.string.pick_predifined).setSingleChoiceItems(
                new ArrayAdapter<Inventory>(activity,
                        android.R.layout.simple_list_item_1, inventories) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        final TextView view = (TextView) super.getView(position, convertView, parent);
                        view.setText(inventories.get(position).getString("desc"));
                        return view;
                    }
                }, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Inventory inventory = inventories.get(which);
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        params.put("objectId", inventory.getObjectId());
                        ParseCloud.callFunctionInBackground("copyInventory", params, new FunctionCallback<Object>() {
                            @Override
                            public void done(Object o, ParseException e) {
                                Toast.makeText(activity, "Saved on server: " + o, Toast.LENGTH_LONG).show();
                                dismiss();
                            }
                        });
                    }
                }
        ).create();
    }
}
