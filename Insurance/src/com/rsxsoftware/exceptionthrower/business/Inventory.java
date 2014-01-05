package com.rsxsoftware.exceptionthrower.business;

import com.parse.ParseClassName;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 9/29/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */

@ParseClassName("Inventory")
public class Inventory extends ParseObjectBase {

    public static final String NAME = "Inventory";

    public Inventory() {
        super(NAME);
    }

    @Override
    public ParseObjectBase deepCopy() {
        final ParseObjectBase copy = super.deepCopy();
        return copy;
    }

    @Override
    protected ParseObjectBase createObject() {
        return new Inventory();
    }


    @Override
    public String getTableName() {
        return Inventory.NAME;
    }

    @Override
    public String getRelationName() {
        return "users";
    }

    @Override
    public ParseObjectInterface createChildObject() {
        return new Room();
    }



}
