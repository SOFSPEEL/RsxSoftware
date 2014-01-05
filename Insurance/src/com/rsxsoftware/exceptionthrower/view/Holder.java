package com.rsxsoftware.exceptionthrower.view;

import android.app.Fragment;
import android.view.View;
import com.parse.ParseObject;
import com.rsxsoftware.exceptionthrower.view.bind.Bind;
import com.rsxsoftware.exceptionthrower.view.bind.BinderBase;

import java.lang.reflect.Field;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class Holder {

    private final Bind bind;
    private final ParseObject object;
    private BinderBase converter;
    private View view;

    public Holder(Bind bind, Fragment fragment, Field field, ParseObject object, View layout) {
        this.bind = bind;
        this.object = object;
        try {
            converter = (BinderBase) bind.Converter().newInstance();
            view = layout.findViewById(bind.id());
            field.setAccessible(true);
            field.set(fragment, view);

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void toView() {
        converter.toView(bind, view, object);
    }

    public void toObject() {
        converter.toObject(bind, view, object);
    }
}
