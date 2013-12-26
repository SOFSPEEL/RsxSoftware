package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import com.rsxsoftware.insurance.R;

/**
 * Created by steve.fiedelberg on 12/19/13.
 */
public class ListPopupWindowMenu extends ListPopupWindow {


    public ListPopupWindowMenu(final Activity activity, View anchorView, final PopupView[] popupViews) {
        super(activity);
        setAnchorView(anchorView);
        setVerticalOffset(20);
        setWidth(300);
        setAdapter(activity, popupViews);
        setModal(false);
    }

    @Override
    public void postShow() {
        super.postShow();
        getAnchorView().setSelected(true);
    }

    private void setAdapter(final Activity activity, final PopupView[] popupViews) {
        setAdapter(new ArrayAdapter<PopupView>(activity, R.layout.popup_row, popupViews) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                final PopupView popupView = getItem(position);

                final View layout = activity.getLayoutInflater().inflate(R.layout.popup_row, null);

                ((ImageView) layout.findViewById(R.id.imageView)).setImageResource(popupView.getDrawableId());
                ((TextView) layout.findViewById(R.id.textView)).setText(popupView.getStringId());

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        popupView.getListener().onClick(v);
                    }
                });
                return layout;

            }
        });
    }
}
