package com.rsxsoftware.insurance.business;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public abstract class ParseObjectFetch extends ParseObject implements ParseObjectInterface {
    public ParseObjectFetch(String theClassName) {
        super(theClassName);
    }

    public void fetchList(FindCallback updateListCallback) {

        final ParseObject object = getRealObject();
        if (object.getObjectId() != null) {

            fetchList(object).findInBackground(updateListCallback);
        }
    }

    protected ParseQuery<ParseObjectBase> fetchList(ParseObject object) {
        final ParseObjectInterface childObject = createChildObject();
        ParseQuery<ParseObjectBase> query = new ParseQuery(childObject.getTableName());
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query = query.whereEqualTo(childObject.getRelationName(), object);
        return query;
    }
}
