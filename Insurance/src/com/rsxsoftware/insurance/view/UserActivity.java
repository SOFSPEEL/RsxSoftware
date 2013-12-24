package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.os.Bundle;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.rsxsoftware.insurance.R;

/**
 * Created by steve.fiedelberg on 12/14/13.
 */
public class UserActivity extends Activity {

    public static final String TAG = "Inventory";


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
////                saveFilesToSaveEventually(context, map);
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