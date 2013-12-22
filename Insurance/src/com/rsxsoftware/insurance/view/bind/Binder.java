package com.rsxsoftware.insurance.view.bind;

import android.app.Fragment;
import android.view.View;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.view.ContentFragment;
import com.rsxsoftware.insurance.view.Holder;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
public class Binder extends HashMap<Bind, Holder> {

    public Binder(Fragment fragment, ParseObject parseObject, View layout) {
        final Field[] fields = ContentFragment.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Bind.class)) {
                final Bind bind = field.getAnnotation(Bind.class);

                final Holder holder = new Holder(bind, fragment, field, parseObject, layout);
                put(bind, holder);
                holder.toView();

            }
        }
    }

    public void toObject() {
        for (Entry<Bind, Holder> entry : this.entrySet()){
            entry.getValue().toObject();
        }
    }
}
