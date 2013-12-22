package com.rsxsoftware.insurance.view.bind;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.parse.ParseObject;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class BinderNumber extends BinderBase {
    @Override
    public void toView(Bind bind, View view, ParseObject object) {

        final Number number = object.getNumber(bind.key());
        ((TextView) view).setText(number != null ? Double.toString(number.doubleValue()) : "");
    }

    @Override
    public void toObject(Bind bind, View view, ParseObject object) {
        final String value = ((TextView) view).getText().toString();
        object.put(bind.key(), !TextUtils.isEmpty(value) ? Double.parseDouble(value) : 0);
    }
}
