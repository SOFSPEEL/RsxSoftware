package com.rsxsoftware.insurance;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;
import com.parse.ParseFile;
import com.rsxsoftware.insurance.business.Content;
import com.rsxsoftware.insurance.prefs.Prefs;
import com.rsxsoftware.insurance.view.bind.ParseFileExtended;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class ParseFileSaveEventuallyService extends IntentService {

    //TODO: check if there is a network connection
    //TODO: When connectivity changes run this service

    public static final String TAG = "ParseFileSaveEventuallyService";

    public ParseFileSaveEventuallyService() {
        super("ParseFileSaveEventuallyService");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ArrayList<String> listRemove = new ArrayList<String>();
        final Set<String> files = Prefs.fetchFilesToSaveEventually(this);
        for (String file : files) {

            try {
                final File fileAbsolute = new File(file);
                final ParseFile parseFile = new ParseFile(FileUtils.readFileToByteArray(fileAbsolute));

                Log.d(TAG, "Saving parse file: " + fileAbsolute);
                parseFile.save();
                Log.d(TAG, "Saved parse file: " + fileAbsolute);
                final Pair<String, String> pair = ParseFileExtended.parseFile(file);
                final String objectId = pair.first;
                final String key = pair.second;

                final Content content = new Content();
                content.setObjectId(objectId);
                content.put(key, parseFile);
                Log.d(TAG, "Saving content: " + objectId + " with file: " + fileAbsolute);
                content.save();
                listRemove.add(file);
                Log.d(TAG, "Saved content: " + objectId + " with file: " + fileAbsolute);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        final boolean removed = files.removeAll(listRemove);

        if (removed) {
            Prefs.saveFilesToSaveEventually(this, files);
        }

    }
}
