package com.rsxsoftware.insurance.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by steve.fiedelberg on 12/18/13.
 */
public class InstantAutoComplete extends AutoCompleteTextView {

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public InstantAutoComplete(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return getText().length() >= 0;
    }

    @Override
    public int getThreshold() {
        return 0;
    }

//    @Override
//    public boolean enoughToFilter() {
//        return true;
//    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction,
//
//                                  Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (!focused) {
//            showDropDown();
//        }
//    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction,
//                                  Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        if (focused) {
//            if (getText().toString().length() == 0) {
//                // We want to trigger the drop down, replace the text.
//                setText("");
//            }
//        }
//    }
}
