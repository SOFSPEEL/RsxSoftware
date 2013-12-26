package com.rsxsoftware.insurance.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.R;
import com.rsxsoftware.insurance.business.ParseObjectBase;

import java.util.ArrayList;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public abstract class ListAdapter<T extends ParseObjectBase> extends ParseQueryAdapter<T> {

    private final UserActivity userActivity;
    private final ListFragment fragment;

    public ListAdapter(UserActivity userActivity, ListFragment fragment, QueryFactory<T> queryFactory) {
        super(userActivity, queryFactory);
        setTextKey("desc");
        setPaginationEnabled(true);
        this.userActivity = userActivity;
        this.fragment = fragment;
        setImageKey("blah");
        setPlaceholder(userActivity.getResources().getDrawable(android.R.drawable.progress_horizontal));
    }


    @Override
    public View getItemView(T object, View v, ViewGroup parent) {
        final View row = userActivity.getLayoutInflater().inflate(R.layout.list_row, null);
        final TextView tv = (TextView) row.findViewById(R.id.desc);
        tv.setText(object.getString("desc"));
        handleButtons(row, object);
        return row;

    }

    protected void handleButtons(final View row, final ParseObjectBase object) {

        final Button buttonChildren = (Button) row.findViewById(R.id.children);

        final String text = object.initTextForDetailsButton();
        buttonChildren.setText(text);
        object.fetchTextForDetailsButton(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null) {
                    buttonChildren.setText(text + "(" + i + ")");
                }
            }
        });


        buttonChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.switchTo(userActivity, newChildFragment(), object);
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
                        new DeleteItemDialog(object).show(userActivity.getFragmentManager(), "");
                    }
                });
            }

            private PopupView addEdit() {
                return new PopupView(R.string.edit, R.drawable.ic_menu_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.switchTo(userActivity, newChildFragment(), object);


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

                        object.put("master", true);
                        object.saveEventually();
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
                    fragment.refresh();
                }
            }).setNegativeButton(R.string.no, null).create();
            return alertDialog;
        }
    }

}
