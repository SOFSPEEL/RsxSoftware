package com.rsxsoftware.exceptionthrower.view.bind;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by steve.fiedelberg on 12/15/13.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
    String key();
    Class<?> Converter();

    int id();

    int requestCode() default 0;

    String text() default "";
}
