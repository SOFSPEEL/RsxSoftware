package com.rsxsoftware.exceptionthrower.view;

import com.rsxsoftware.exceptionthrower.R;
import com.rsxsoftware.exceptionthrower.business.Content;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class ContentsFragment extends ListFragment<Content> {

    public ContentsFragment() {
    }


    @Override
    protected FragmentBase getNextFragment() {
        return new ContentFragment();
    }

    @Override
    protected String getHint() {
        return "Item Name (Eg. Chair)";
    }

    @Override
    protected Content newObjectInstance() {
        return new Content();
    }

    @Override
    protected int getLayout() {
        return R.layout.list;
    }




}
