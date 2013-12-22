package com.rsxsoftware.insurance.business;

import com.parse.CountCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by steve.fiedelberg on 12/17/13.
 */
public abstract class ParseObjectBase extends ParseObjectFetch {

    public ParseObjectBase(String theClassName) {
        super(theClassName);
    }


    public ParseObjectBase deepCopy() {
        final ParseObjectBase copy = createObject();
        copy.put("desc", get("desc"));
        return copy;
    }

    protected abstract ParseObjectBase createObject();

    @Override
    public ParseObject getRealObject() {
        return this;
    }

    public void getTextForDetailsButton(CountCallback callback) {
        final String childTableName = getChildTableName();

        final boolean hasChildren = childTableName != null;
        if (hasChildren) {
            if (getObjectId() != null) {
                final ParseQuery<ParseObjectBase> query = new ParseQuery(childTableName);
                query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                query.whereEqualTo(getTableName().toLowerCase(), this).countInBackground(callback);
            }
        }

    }
}
