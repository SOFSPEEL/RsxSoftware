package com.rsxsoftware.insurance.view;

import android.app.*;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public class Header<TList extends ParseObjectBase> {

    private final UserActivity userActivity;
    private final ListFragment<TList> listFragment;

    public Header(UserActivity userActivity, ListFragment<TList> listFragment) {

        this.userActivity = userActivity;
        this.listFragment = listFragment;
    }


    public void setup(View header) {

        final String[] selections = listFragment.getSelections();
        Arrays.sort(selections);

        final InstantAutoComplete autoComplete = setupAutoComplete(header, selections);

        handleAddButton(header, autoComplete);
        handleRefreshButton(header);
        handleAddPredefined(header);

    }

    private InstantAutoComplete setupAutoComplete(View header, String[] selections) {
        final InstantAutoComplete autoComplete = (InstantAutoComplete) header.findViewById(R.id.autoComplete);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(header.getContext(),
                android.R.layout.simple_dropdown_item_1line, selections);
        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(300);
        autoComplete.setHint(listFragment.getHint());
//        autoComplete.setText("");
        autoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence constraint = autoComplete.getText();
                adapter.getFilter().filter(constraint);
                autoComplete.showDropDown();
            }
        });
        return autoComplete;
    }


    private void handleAddPredefined(View header) {
        header.findViewById(R.id.addPredefinedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressFragment progressFragment = new ProgressFragment();
                final FragmentManager fragmentManager = userActivity.getFragmentManager();
                progressFragment.show(fragmentManager.beginTransaction(), "dialog");

                new ParseQuery<Inventory>("Inventory").whereEqualTo("master", true).findInBackground(new FindCallback<Inventory>() {
                    @Override
                    public void done(List<Inventory> inventories, ParseException e) {
                        progressFragment.dismiss();

                        final PredefinedListFragmentDialog predefinedListFragmentDialog = new PredefinedListFragmentDialog(inventories);
                        predefinedListFragmentDialog.show(fragmentManager.beginTransaction(), "dialog");

                    }

                });
            }

        });
    }

    private void handleRefreshButton(View header) {
        header.findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listFragment.getSelected().fetchList(listFragment.createUpdateListCallback());
            }
        });
    }

    private void handleAddButton(View header, final InstantAutoComplete autoComplete) {
        header.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Editable text = autoComplete.getText();
                final String value = text.toString();

                if (!TextUtils.isEmpty(value)) {
                    autoComplete.setText("");
                    final TList newObject = listFragment.newObjectInstance();
                    newObject.put("desc", value);
                    newObject.put(newObject.getParentTableName().toLowerCase(), listFragment.getRealObject());
                    newObject.saveEventually();
                    listFragment.addToAdapter(newObject);
                }

            }
        });
    }

}