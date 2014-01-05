package com.rsxsoftware.exceptionthrower.business;

import com.parse.CountCallback;
import com.parse.ParseObject;

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

    public String initTextForDetailsButton() {
        return createChildObject().getTableName();
    }

    public void fetchTextForDetailsButton(CountCallback callback) {
        fetchList(this).countInBackground(callback);
    }
}
