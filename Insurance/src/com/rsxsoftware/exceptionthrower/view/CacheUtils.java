package com.rsxsoftware.exceptionthrower.view;

import com.parse.ParseQuery;

/**
 * Created by steve.fiedelberg on 12/27/13.
 */
public class CacheUtils {
    public static ParseQuery createQuerySetCachePolicy(String tableName) {
        final ParseQuery parseQuery = new ParseQuery(tableName);
        parseQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        return parseQuery;

    }
}
