package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class UserActivity extends Activity {

    public static final String TAG = "Inventory";

    public static boolean saveArray(Context context, HashMap<String, String> map) {
        SharedPreferences sp = context.getSharedPreferences("list", MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet("objectIds", map.keySet());
        edit.putStringSet("path", new HashSet<String>(map.values()));
        return edit.commit();
    }

    public static Set<String> fetchArray(Context context) {
        SharedPreferences sp = context.getSharedPreferences("list", MODE_PRIVATE);
        final Set<String> objectIds = sp.getStringSet("objectIds", new HashSet<String>());
        return objectIds;
    }




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ParseAnalytics.trackAppOpened(getIntent());

        ParseUser.enableAutomaticUser();

        final LoginFragment loginFragment = new LoginFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, loginFragment).commit();

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                final HashMap<String, String> map = new HashMap<String, String>();
//                map.put("Junka", "junk1");
//                map.put("Junkb", "junk2");
//
//                final UserActivity context = UserActivity.this;
////                saveArray(context, map);
//                final Set<String> strings = fetchArray(context);
//
//                return null;
//            }
//
//
//        }.execute();



    }



//    private void loginUser() {
////        final ParseUser user = new ParseUser();
////        user.setUsername("Steve");
////        user.setPassword("Junk");
////        user.setEmail("steve.feidelberg@gmail.com");
////        user.signUpInBackground(new SignUpCallback() {
////            @Override
////            public void done(ParseException e) {
//
//            }
////        });
//    }}


    protected int getLayoutId() {
        return R.layout.fragment_container;
    }


}