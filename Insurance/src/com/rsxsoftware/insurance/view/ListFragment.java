package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.ParseObjectBase;
import com.rsxsoftware.insurance.business.ParseObjectInterface;
import com.rsxsoftware.insurance.view.bind.CapturePhoto;
import com.rsxsoftware.insurance.view.bind.OnCapturePhotoListener;

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

        final Header header = new Header(userActivity, this);
        header.setup();

        lv.addHeaderView(header.getView());
        adapter = createAdapter();
        lv.setAdapter(adapter);
        return view;
    }

    public void switchTo(UserActivity userActivity, FragmentBase fragmentTo, ParseObjectInterface selected) {

        if (fragmentTo != null) {
            fragmentTo.setSelected(selected);
            replaceFragment(userActivity, fragmentTo);
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

    private ListAdapter createAdapter() {

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        final ParseQuery query = new ParseQuery(getSelected().createChildObject().getTableName()).orderByAscending("desc");
                        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                        return query;
                    }
                };

        return new ListAdapter(userActivity, this, factory) {
            @Override
            protected FragmentBase newChildFragment() {
                return getNextFragment();
            }
        };
    }

    protected abstract FragmentBase getNextFragment();

    protected abstract String getHint();

    protected abstract TList newObjectInstance();

    protected abstract int getLayout();

    @Override
    public void onAttach(Activity activity) {
        this.userActivity = (UserActivity) activity;
        super.onAttach(activity);
    }

    public void refresh() {
        lv.setAdapter(createAdapter());
    }

    private CapturePhoto capturePhoto;



    public void capturePhoto(View iv, ParseObject object, String photo, int requestCode, final OnCapturePhotoListener onCapturePhotoListener) {
        capturePhoto = new CapturePhoto(requestCode, onCapturePhotoListener);
        capturePhoto.showPopup(iv, object, photo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (capturePhoto != null) {

            capturePhoto.onActivityResult(requestCode, resultCode, data);

        }
    }
}
