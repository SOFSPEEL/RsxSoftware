package com.rsxsoftware.insurance.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.parse.ParseObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class Prefs {
    public static Set<String> fetchFilesToSaveEventually(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Inventory", Context.MODE_PRIVATE);
        final Set<String> objectIds = sp.getStringSet("objectIds", new HashSet<String>());
        return objectIds;
    }

    public static boolean saveFilesToSaveEventually(Context context, Set<String> set) {
        SharedPreferences sp = context.getSharedPreferences("Inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet("objectIds", set);
        return edit.commit();
    }

    public static void updateFilesToSaveEventually(Context context, ParseObject object, String key, String filePath) {
        final String valueToStore = new PhotoSave(object.getObjectId(), key, filePath).createStorageValue();
        final Set<String> files = fetchFilesToSaveEventually(context);
        if (!files.contains(valueToStore)){
            files.add(valueToStore);
            saveFilesToSaveEventually(context, files);
        }
    }

}
