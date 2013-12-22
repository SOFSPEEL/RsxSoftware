package com.rsxsoftware.Admin;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by steve.fiedelberg on 12/19/13.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "ORL3ZTS0YA4r0nCNRZ4G7C6pXswluZN0axqAFUAx", "VIX0KqI2ew5XJf14eIZt72iBi6Zr1QhFAL1Q9FVS");
    }
}
