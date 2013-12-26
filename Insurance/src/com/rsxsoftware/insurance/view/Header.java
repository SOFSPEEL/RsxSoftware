package com.rsxsoftware.insurance.view;

import android.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import com.parse.*;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public class Header<TList extends ParseObjectBase> {

    private final UserActivity userActivity;
    private final ListFragment<TList> listFragment;
    private View header;

    public Header(UserActivity userActivity, ListFragment<TList> listFragment) {

        this.userActivity = userActivity;
        this.listFragment = listFragment;
    }


    public void setup() {
        header = userActivity.getLayoutInflater().inflate(R.layout.header, null);
        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) header.findViewById(R.id.autoComplete);

        autoComplete.setHint(listFragment.getHint());
        autoComplete.setThreshold(1);


        autoComplete.setAdapter(new AutoCompleteAdapter());

        listFragment.fetchSelections(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> selections, ParseException e) {

                if (e == null) {

                    setupAutoComplete(header, selections, autoComplete);

                }
            }
        });

        handleAddButton(header, autoComplete);
        handleRefreshButton(header);
        handleAddPredefined(header);

    }

    private AutoCompleteTextView setupAutoComplete(View header, final List<ParseObject> selections, AutoCompleteTextView autoComplete) {


        final ArrayList<String> strings = new ArrayList<String>();
        for (ParseObject obj : selections) {

            strings.add(obj.getString("desc"));
        }


        autoComplete.setAdapter(new ArrayAdapter<String>(header.getContext(),
                android.R.layout.simple_dropdown_item_1line, strings));

        return autoComplete;
    }


    private void handleAddPredefined(View header) {
        header.findViewById(R.id.addPredefinedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = userActivity.getFragmentManager();
                final ProgressFragment progressFragment = new ProgressFragment(fragmentManager, "Fetching");


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

    public View getView() {
        return header;
    }

    private class AutoCompleteAdapter extends ParseQueryAdapter implements Filterable {
        public AutoCompleteAdapter() {
            super(userActivity, new ParseQueryAdapter.QueryFactory<TList>() {
                @Override
                public ParseQuery<TList> create() {
                    return new ParseQuery(listFragment.getSelected().createChildObject().getTableName()).orderByAscending("desc");
                }
            }, android.R.layout.simple_dropdown_item_1line);
            setTextKey("desc");
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }
}
