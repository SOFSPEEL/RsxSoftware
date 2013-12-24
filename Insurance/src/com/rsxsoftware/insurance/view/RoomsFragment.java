package com.rsxsoftware.insurance.view;

import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.ParseObjectBase;
import com.rsxsoftware.insurance.business.Room;

import java.util.List;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class RoomsFragment extends ListFragment<Room> {

    public RoomsFragment() {
    }


    @Override
    protected ListAdapter createAdapter() {
        return new RoomAdapter(getParseObjects());
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

    private class RoomAdapter extends ListAdapter {

        public RoomAdapter(List<ParseObjectBase> parseObjects) {

            super(userActivity, RoomsFragment.this, parseObjects);
        }


        @Override
        protected FragmentBase newChildFragment() {
            return new ContentsFragment();
        }
    }

}
