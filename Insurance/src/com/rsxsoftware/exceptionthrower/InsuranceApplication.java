package com.rsxsoftware.exceptionthrower;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rsxsoftware.exceptionthrower.business.Content;
import com.rsxsoftware.exceptionthrower.business.Inventory;
import com.rsxsoftware.exceptionthrower.business.Room;
import com.rsxsoftware.exceptionthrower.view.UserFacade;

/**
 * Created with IntelliJ IDEA.
 * User: steve.fiedelberg
 * Date: 9/29/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class InsuranceApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseUser.enableAutomaticUser();

        ParseObject.registerSubclass(UserFacade.class);
        ParseObject.registerSubclass(Inventory.class);
        ParseObject.registerSubclass(Room.class);
        ParseObject.registerSubclass(Content.class);
        Parse.initialize(this, "ORL3ZTS0YA4r0nCNRZ4G7C6pXswluZN0axqAFUAx", "VIX0KqI2ew5XJf14eIZt72iBi6Zr1QhFAL1Q9FVS");
    }
}
