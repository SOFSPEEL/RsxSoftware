package com.rsxsoftware.insurance.view;

import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.parse.*;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectBase;

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


    public void setup(final View header) {

        listFragment.fetchSelections(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                final AutoCompleteTextView autoComplete = setupAutoComplete(header, list);
                handleAddButton(header, autoComplete);
            }
        });


        handleRefreshButton(header);
        handleAddPredefined(header);

    }

    private AutoCompleteTextView setupAutoComplete(View header, final List<ParseObject> selections) {
        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) header.findViewById(R.id.autoComplete);

        final ArrayAdapter<ParseObject> adapter = new ArrayAdapter<ParseObject>(header.getContext(),
                android.R.layout.simple_dropdown_item_1line, selections){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final TextView tv = (TextView) super.getView(position, convertView, parent);
                                 tv.setText(selections.get(position).getString("desc"));
                return tv;
            }
        };
        autoComplete.setAdapter(adapter);
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
                final Bundle args = new Bundle();
                args.putString("title", "Fetching");
                progressFragment.setArguments(args);
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

    private void handleAddButton(View header, final AutoCompleteTextView autoComplete) {
        header.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Editable text = autoComplete.getText();
                final String value = text.toString();

                if (!TextUtils.isEmpty(value)) {
                    autoComplete.setText("");
                    final TList newObject = listFragment.newObjectInstance();
                    newObject.put("desc", value);
                    final ParseRelation<ParseObject> relation = newObject.getRelation(newObject.getRelationName());
                    relation.add(listFragment.getRealObject());
                    newObject.saveEventually();
                    listFragment.addToAdapter(newObject);
                }

            }
        });
    }

}
