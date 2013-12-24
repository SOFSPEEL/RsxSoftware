package com.rsxsoftware.insurance.view;

import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.List;

public class InventoriesFragment extends ListFragment<Inventory> {


    public static final String TAG = "Inventory";

    public InventoriesFragment() {
    }


    @Override
    protected ListAdapter createAdapter() {
        return new InventoryAdapter(getParseObjects());
    }

    @Override
    protected String getHint() {
        return "Inventory Name (Eg. House xxx)";
    }


    @Override
    protected Inventory newObjectInstance() {
        return new Inventory();
    }


    @Override
    protected int getLayout() {
        return R.layout.list;
    }


    private class InventoryAdapter extends ListAdapter {

        public InventoryAdapter(List<ParseObjectBase> parseObjects) {

            super(userActivity, InventoriesFragment.this, parseObjects);
        }



        @Override
        protected FragmentBase newChildFragment() {
            return new RoomsFragment();
        }
    }
}
