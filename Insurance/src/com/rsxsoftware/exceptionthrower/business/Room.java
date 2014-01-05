package com.rsxsoftware.exceptionthrower.business;

import com.parse.ParseClassName;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 9/29/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
@ParseClassName("Room")
public class Room extends ParseObjectBase {

    public static final String NAME = "Room";

    public Room() {
        super(NAME);
    }

    @Override
    public String getTableName() {
        return Room.NAME;
    }

    @Override
    public String getRelationName() {
        return "inventories";
    }

    @Override
    public ParseObjectInterface createChildObject() {
        return new Content();
    }

    @Override
    public ParseObjectBase deepCopy() {

        final Room room = new Room();
        room.put("desc", get("desc"));
        return room;
    }

    @Override
    protected ParseObjectBase createObject() {
        return new Room();
    }
}
