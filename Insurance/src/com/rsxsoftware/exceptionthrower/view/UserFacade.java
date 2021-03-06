package com.rsxsoftware.exceptionthrower.view;

import com.parse.*;
import com.rsxsoftware.exceptionthrower.business.Inventory;
import com.rsxsoftware.exceptionthrower.business.ParseObjectFetch;
import com.rsxsoftware.exceptionthrower.business.ParseObjectInterface;

/**
 * Created by steve.fiedelberg on 12/20/13.
 * Facade which is not truly as user, it wraps a ParseUser to allow a fetch of a users inventories
 */
@ParseClassName("UserFacade")
public class UserFacade extends ParseObjectFetch {

    public UserFacade() {
        super("UserFacade");
    }

    @Override
    public String getTableName() {
        return "User";
    }

    @Override
    public String getRelationName() {
        return null;
    }

    @Override
    public ParseObjectInterface createChildObject() {
        return new Inventory();
    }

    @Override
    public ParseObject getRealObject() {
        return ParseUser.getCurrentUser();
    }
}
