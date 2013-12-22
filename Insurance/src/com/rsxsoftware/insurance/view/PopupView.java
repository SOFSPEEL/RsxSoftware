package com.rsxsoftware.insurance.view;

import android.view.View;

/**
* Created by steve.fiedelberg on 12/19/13.
*/
public class PopupView {
    private int stringId;
    private int drawableId;
    private View.OnClickListener listener;

    public PopupView(int stringId, int drawableId, View.OnClickListener listener) {

        this.stringId = stringId;
        this.drawableId = drawableId;
        this.listener = listener;

    }

    public int getStringId() {
        return stringId;
    }

    public int getDrawableId() {
        return drawableId;
    }


    public View.OnClickListener getListener() {
        return listener;
    }
}
