package com.rsxsoftware.insurance.view;

import android.app.Fragment;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.business.ParseObjectInterface;

/**
 * Created by steve.fiedelberg on 12/15/13.
 * Test
 */
public abstract class FragmentBase extends Fragment {

    private ParseObjectInterface selected;

    public ParseObjectInterface getSelected() {
        return selected;
    }


    public void setSelected(ParseObjectInterface object) {
        this.selected = object;
    }

    public ParseObject getRealObject() {
        return selected.getRealObject();
    }

}
