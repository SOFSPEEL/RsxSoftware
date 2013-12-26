package com.rsxsoftware.insurance.view;

import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Room;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class RoomsFragment extends ListFragment<Room> {

    public RoomsFragment() {
    }

    @Override
    protected FragmentBase getNextFragment() {
        return new ContentsFragment();
    }

    @Override
    protected String getHint() {
        return "Room Name (Eg. Master Bedroom)";
    }


    @Override
    protected Room newObjectInstance() {
        return new Room();
    }

    @Override
    protected int getLayout() {
        return R.layout.list;
    }


}
