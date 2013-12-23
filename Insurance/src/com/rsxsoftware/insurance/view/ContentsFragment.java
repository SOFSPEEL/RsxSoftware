package com.rsxsoftware.insurance.view;

import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.Content;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.List;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class ContentsFragment extends ListFragment<Content> {

    public ContentsFragment() {
    }




    @Override
    protected ListAdapter createAdapter() {
        return new ContentAdapter(getParseObjects());
    }

    @Override
    protected String[] getSelections() {
        return new String[0];
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


    private class ContentAdapter extends ListAdapter {

        public ContentAdapter(List<ParseObjectBase> parseObjects) {

            super(userActivity, ContentsFragment.this, parseObjects);
        }

        @Override
        protected FragmentBase newChildFragment() {
            return new ContentFragment();
        }

    }

}
