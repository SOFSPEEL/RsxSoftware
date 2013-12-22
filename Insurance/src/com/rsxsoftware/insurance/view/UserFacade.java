package com.rsxsoftware.insurance.view;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectFetch;

/**
 * Created by steve.fiedelberg on 12/20/13.
 * Facade which is not truly as user, it wraps a ParseUser to allow a fetch of a users inventories
 */
@ParseClassName("UserFacade")
public class UserFacade extends ParseObjectFetch {
    private  ParseUser currentUser;

    public UserFacade() {
        super("UserFacade");
    }

    public UserFacade(ParseUser currentUser) {
        super("UserFacade");
        this.currentUser = currentUser;
    }

    @Override
    public String getTableName() {
        return "User";
    }

    @Override
    public String getChildTableName() {
        return Inventory.NAME;
    }

    @Override
    public String getParentTableName() {
        return null;
    }



    @Override
    public ParseObject getRealObject() {
        return currentUser;
    }
}
