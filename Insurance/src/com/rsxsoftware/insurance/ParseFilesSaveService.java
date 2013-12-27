package com.rsxsoftware.insurance;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.prefs.PhotoSave;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class ParseFilesSaveService extends IntentService {

    //TODO: check if there is a network connection
    //TODO: When connectivity changes run this service

    public static final String TAG = "ParseFileSaveService";

    public ParseFilesSaveService() {
        super(TAG);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ArrayList<String> listRemove = new ArrayList<String>();
        Set<String> storageValues = fetch(this);
        for (String storageValue : storageValues) {

            try {

                final PhotoSave photoSave = new PhotoSave(storageValue);
                final File filePath = photoSave.getFilePath();

                Log.d(TAG, "Parsing file: " + photoSave);
                final ParseFile parseFile = new ParseFile(FileUtils.readFileToByteArray(filePath));

                Log.d(TAG, "Saving parse file: " + photoSave);
                parseFile.save();
                Log.d(TAG, "Saved parse file: " + photoSave);

                final ParseObject object = ParseObject.create(photoSave.getClassName());
                object.setObjectId(photoSave.getObjectId());
                object.put(photoSave.getKey(), parseFile);
                Log.d(TAG, "Saving: " + photoSave);
                object.save();
                listRemove.add(storageValue);
                Log.d(TAG, "Saved: " + photoSave);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        storageValues = fetch(this);
        final boolean removed = storageValues.removeAll(listRemove);

        if (removed) {
            save(this, storageValues);
        }
    }

    public static Set<String> fetch(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Inventory", Context.MODE_PRIVATE);
        final Set<String> objectIds = sp.getStringSet("objectIds", new HashSet<String>());
        return objectIds;
    }

    public static boolean save(Context context, Set<String> set) {
        SharedPreferences sp = context.getSharedPreferences("Inventory", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putStringSet("objectIds", set);
        return edit.commit();
    }

    public static void add(String filePath, ParseObject object, String key, Context context) {
        final String valueToStore = new PhotoSave(object, key, filePath).createStorageValue();
        final Set<String> files = fetch(context);
        if (!files.contains(valueToStore)){
            files.add(valueToStore);
            save(context, files);
            context.startService(new Intent(context, ParseFilesSaveService.class));
        }
    }

}
