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

            final ParseQuery<ParseObjectBase> query = new ParseQuery(getChildTableName());
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
            query.whereEqualTo(getTableName().toLowerCase(), object).findInBackground(updateListCallback);
        }
    }
}
