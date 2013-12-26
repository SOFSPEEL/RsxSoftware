package com.rsxsoftware.insurance.view;

import com.parse.*;
import com.rsxsoftware.insurance.business.Inventory;
import com.rsxsoftware.insurance.business.ParseObjectBase;
import com.rsxsoftware.insurance.business.ParseObjectFetch;
import com.rsxsoftware.insurance.business.ParseObjectInterface;

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
    public void fetchList(FindCallback updateListCallback) {

        final ParseObject object = getRealObject();
        if (object.getObjectId() != null) {
            final ParseObjectInterface childObject = createChildObject();

            final ParseQuery<ParseObjectBase> query = new ParseQuery(childObject.getTableName());
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.whereEqualTo("user", object).findInBackground(updateListCallback);
        }
    }

    @Override
    public ParseObject getRealObject() {
        return ParseUser.getCurrentUser();
    }
}
