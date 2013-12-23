package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.ParseObjectBase;
import com.rsxsoftware.insurance.business.ParseObjectInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 10/2/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ListFragment<TList extends ParseObjectBase> extends FragmentBase {

    public static final String TAG = "Inventory";
    protected UserActivity userActivity;
    protected ListAdapter adapter;
    protected ListView lv;
    private List<ParseObjectBase> parseObjects = new ArrayList<ParseObjectBase>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayout(), container, false);

        lv = (ListView) view.findViewById(R.id.lv);
        final View header = inflater.inflate(R.layout.header, null);
        new Header(userActivity, this).setup(header);

        lv.addHeaderView(header);
        adapter = createAdapter();
        lv.setAdapter(adapter);
        return view;
    }


    public List<ParseObjectBase> getParseObjects() {
        return parseObjects;
    }

    public void switchTo(UserActivity userActivity, FragmentBase fragmentTo, ParseObjectInterface selected) {

        if (fragmentTo != null) {

            replaceFragment(userActivity, fragmentTo);

            fragmentTo.setSelected(selected);
            if (fragmentTo instanceof ListFragment) {
                ListFragment fragmentListTo = (ListFragment) fragmentTo;
                selected.fetchList(fragmentListTo.createUpdateListCallback());
            }
        }
    }

    private void replaceFragment(UserActivity userActivity, FragmentBase listWithAdapterFragment) {
        final FragmentTransaction transaction = userActivity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, listWithAdapterFragment).addToBackStack(null).commit();
    }


    protected void addToAdapter(ParseObject object) {
        parseObjects.add((ParseObjectBase) object);

        adapter.notifyDataSetChanged();

        lv.smoothScrollToPosition(parseObjects.size());
    }

    protected abstract ListAdapter createAdapter();


    protected abstract String[] getSelections();

    protected abstract String getHint();

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }


    protected abstract TList newObjectInstance();

    protected abstract int getLayout();

    @Override
    public void onAttach(Activity activity) {
        this.userActivity = (UserActivity) activity;
        super.onAttach(activity);
    }

    protected FindCallback<ParseObjectBase> createUpdateListCallback() {
        return new FindCallback<ParseObjectBase>() {
            @Override
            public void done(List<ParseObjectBase> list, ParseException e) {
                if (e == null) {

                    parseObjects = list;
                    adapter.setParseObjects(parseObjects);


                } else {
                    Log.e(TAG, "Fetch failed", e);
                }
            }
        };
    }
}
