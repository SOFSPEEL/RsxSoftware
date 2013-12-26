package com.rsxsoftware.insurance.view;

import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Inventory;

public class InventoriesFragment extends ListFragment<Inventory> {


    public static final String TAG = "Inventory";

    public InventoriesFragment() {
    }


    @Override
    protected FragmentBase getNextFragment() {
        return new RoomsFragment();
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


}
