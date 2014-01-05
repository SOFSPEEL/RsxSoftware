package com.rsxsoftware.exceptionthrower.business;

import com.parse.CountCallback;
import com.parse.ParseClassName;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 9/29/13
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
@ParseClassName("Content")
public class Content extends ParseObjectBase {
    public static final String NAME = "Content";

    public Content() {
        super(NAME);
    }

    @Override
    public String getTableName() {
        return Content.NAME;
    }

    @Override
    public String getRelationName() {
        return "rooms";
    }

    @Override
    public String initTextForDetailsButton() {
        return "Pic";
    }

    @Override
    public ParseObjectInterface createChildObject() {
        return null;
    }

    @Override
    public void fetchTextForDetailsButton(CountCallback callback) {
        callback.done(count("receipt") + count("photo"), null);
    }

    private int count(String receipt) {
        return get(receipt) != null ? 1 : 0;
    }

    @Override
    public ParseObjectBase deepCopy() {
        final Content content = new Content();
        content.put("desc", get("desc"));
        return content;
    }

    @Override
    protected ParseObjectBase createObject() {
        return new Content();
    }
}
