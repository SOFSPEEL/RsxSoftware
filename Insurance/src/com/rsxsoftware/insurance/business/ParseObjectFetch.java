package com.rsxsoftware.insurance.business;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rsxsoftware.insurance.view.CacheUtils;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public abstract class ParseObjectFetch extends ParseObject implements ParseObjectInterface {
    public ParseObjectFetch(String theClassName) {
        super(theClassName);
    }

    protected ParseQuery<ParseObjectBase> fetchList(ParseObject object) {
        final ParseObjectInterface childObject = createChildObject();
        ParseQuery<ParseObjectBase> query = CacheUtils.createQuerySetCachePolicy(childObject.getTableName());
        return query.whereEqualTo(childObject.getRelationName(), object);
    }
}
