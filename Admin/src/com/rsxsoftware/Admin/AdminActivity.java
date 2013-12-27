package com.rsxsoftware.Admin;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.parse.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
    }

    public void createRoleAdmin(View view) {
        // By specifying no write privileges for the ACL, we can ensure the role cannot be altered.


        ParseUser.logInInBackground("Steve", "Junk", new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (e == null) {
                    showToast("Have User");
                    ParseACL roleACL = new ParseACL();
                    roleACL.setPublicReadAccess(true);
                    final ParseRole role = new ParseRole("Administrator", roleACL);
                    role.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
//                            if (e == null) {
                            showToast("Create role");
                            role.getUsers().add(parseUser);
                            role.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        showToast("Role assigned to user");
                                    }
                                }
                            });
                        }
//                        }
                    });
                }

            }
        });


    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void IsAdmin(View view) {
        ParseUser.logInInBackground("Steve", "Junk", new LogInCallback() {
            @Override
            public void done(final ParseUser parseUser, ParseException e) {
                if (e == null) {
                    showToast("Have User");
                    new ParseQuery<ParseRole>("Role").whereEqualTo("name", "Administrator").findInBackground(new FindCallback<ParseRole>() {
                        @Override
                        public void done(List<ParseRole> parseRoles, ParseException e) {
                            if (e == null) {
                                int i = 0;
                            }
                        }
                    });

                }
            }

        });
    }

    public void loadCsv(View view) throws IOException {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    doLoadCsv(ParseUser.logIn("Steve", "Junk"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    private void doLoadCsv(ParseUser parseUser) throws Exception {
        final HashMap<String, List<String>> map = new HashMap<String, List<String>>();
        final List csv = FileUtils.readLines(new File("mnt/sdcard", "HomeInventory.csv"));
        for (Object obj : csv) {
            String line = (String) obj;
            final String[] items = line.split(",");
            final String room = items[0];
            final String content = items[1];
            if (!map.containsKey(room)) {
                map.put(room, new ArrayList<String>());
            }
            final List<String> contents = map.get(room);
            contents.add(content);
        }

        ParseObject inv = new ParseObject("Inventory");
        inv = setParms(inv, "Master Inventory", true);
        inv.getRelation("users").add(parseUser);
        inv.save();
        final HashMap<String, ParseObject> mapContents = new HashMap<String, ParseObject>();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            final ParseObject room = new ParseObject("Room");

            final ParseRelation<ParseObject> inventories = room.getRelation("inventories");
            setParms(room, entry.getKey(), false);

            inventories.add(inv);
            room.save();

            for (String content : entry.getValue()) {

                ParseObject contentObj = mapContents.get(content);
                if (contentObj == null) {
                    contentObj = new ParseObject("Content");
                    setParms(contentObj, content, false);
                    mapContents.put(content, contentObj);

                }

                final ParseRelation<ParseObject> rooms = contentObj.getRelation("rooms");
                rooms.add(room);
                contentObj.put("room", room);
                contentObj.save();
            }
        }


    }

    private ParseObject setParms(ParseObject obj, String desc, boolean master) {
        obj.put("desc", desc);
        obj.put("master", master);
        return obj;
    }
}
