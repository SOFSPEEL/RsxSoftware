package com.rsxsoftware.exceptionthrower.business;

import com.parse.ParseObject;

/**
 * Created by steve.fiedelberg on 12/20/13.
 */
public interface ParseObjectInterface {
    String getTableName();

    String getRelationName();

    ParseObjectInterface createChildObject();

    ParseObject getRealObject();
}
