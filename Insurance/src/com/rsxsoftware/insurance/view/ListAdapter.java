package com.rsxsoftware.insurance.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public abstract class ListAdapter<T extends ParseObjectBase> extends BaseAdapter {

    private final UserActivity userActivity;
    private final ListFragment fragment;
    private List<ParseObjectBase> parseObjects;

    public ListAdapter(UserActivity userActivity, ListFragment fragment, List<ParseObjectBase> parseObjects) {
        this.userActivity = userActivity;
        this.fragment = fragment;
        this.parseObjects = parseObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View row = userActivity.getLayoutInflater().inflate(R.layout.list_row, null);
        final TextView tv = (TextView) row.findViewById(R.id.desc);
        final List<ParseObjectBase> parseObjects = fragment.getParseObjects();
        final ParseObjectBase object = parseObjects.get(position);
        tv.setText(object.getString("desc"));
        handleButtons(row, object);
        return row;

    }

    @Override
    public int getCount() {
        return parseObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return parseObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected void handleButtons(final View row, final ParseObjectBase child) {

        final Button buttonChildren = (Button) row.findViewById(R.id.children);
        final String childTableName = child.getChildTableName();
        buttonChildren.setText(childTableName);

        child.getTextForDetailsButton(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {

                buttonChildren.setText(childTableName + "s(" + i + ")");

            }
        });


        buttonChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.switchTo(userActivity, newChildFragment(), child, false);
            }
        });


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<PopupView> popupViews = new ArrayList<PopupView>();
                popupViews.add(addEdit());
                popupViews.add(addDelete());

                if (isInRole(ParseUser.getCurrentUser())) {
                    popupViews.add(addSaveCopyAsMaster());
                }

                final ListPopupWindowMenu listPopupWindowMenu = new ListPopupWindowMenu(userActivity, row, popupViews.toArray(new PopupView[]{}));
                listPopupWindowMenu.show();

            }


            private PopupView addDelete() {
                return new PopupView(R.string.delete, R.drawable.ic_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DeleteItemDialog(child).show(userActivity.getFragmentManager(), "");
                    }
                });
            }

            private PopupView addEdit() {
                return new PopupView(R.string.edit, R.drawable.ic_menu_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.switchTo(userActivity, newChildFragment(), child, false);


                    }
                });
            }

            /**
             * Clicking on this button will make the item selected into a master inventory, meaning users can
             * later copy it as a starting point for their inventory/room/etc.
             */
            private PopupView addSaveCopyAsMaster() {
                return new PopupView(R.string.save_as_master, android.R.drawable.ic_menu_save, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        child.put("master", true);
                        child.saveEventually();
                    }
                });
            }
        }
        );

    }


    private boolean isInRole(ParseUser currentUser) {
        return true; //TODO: how to?
    }

    protected abstract FragmentBase newChildFragment();

    public void setParseObjects(List<ParseObjectBase> parseObjects) {
        this.parseObjects = parseObjects;
        notifyDataSetChanged();
    }

    private class DeleteItemDialog extends DialogFragment {
        private final ParseObjectBase object;

        public DeleteItemDialog(ParseObjectBase object) {

            this.object = object;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String youSure = getString(R.string.are_you_sure, object.getTableName(), object.get("desc"));

            final AlertDialog alertDialog = new AlertDialog.Builder(userActivity).setTitle(youSure).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    object.deleteEventually();
                    parseObjects.remove(object);
                    ListAdapter.this.notifyDataSetChanged();
                }
            }).setNegativeButton(R.string.no, null).create();
            return alertDialog;
        }
    }

}
