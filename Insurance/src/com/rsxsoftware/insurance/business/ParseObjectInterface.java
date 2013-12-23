package com.rsxsoftware.insurance.business;

import com.parse.FindCallback;
import com.parse.ParseObject;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public interface ParseObjectInterface {
    String getTableName();

    String getRelationName();

    ParseObjectInterface createChildObject();

    void fetchList(FindCallback updateListCallback);

    ParseObject getRealObject();
}
