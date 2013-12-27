package com.rsxsoftware.insurance.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
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

        final boolean isAnonymous = ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser());
        if (!isAnonymous) {
            new InventoriesFragment().switchTo(this, new InventoriesFragment(), new UserFacade());
        } else {
            addFrag(new LoginFragment());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        new AlertDialog.Builder(this).setTitle("Are you sure you want to exit").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        }).show();

    }

    private int addFrag(Fragment fragment) {
        return getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
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