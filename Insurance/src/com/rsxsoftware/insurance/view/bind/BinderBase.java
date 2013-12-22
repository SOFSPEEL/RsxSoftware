package com.rsxsoftware.insurance.view.bind;

import android.view.View;
import com.parse.ParseObject;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public abstract class BinderBase {
    public abstract void toView(Bind bind, View view, ParseObject object);


    public abstract void toObject(Bind bind, View view, ParseObject object);
}
