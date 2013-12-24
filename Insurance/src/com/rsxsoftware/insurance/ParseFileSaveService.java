package com.rsxsoftware.insurance;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.parse.ParseFile;
import com.rsxsoftware.insurance.business.Content;
import com.rsxsoftware.insurance.prefs.PhotoSave;
import com.rsxsoftware.insurance.prefs.Prefs;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class ParseFileSaveService extends IntentService {

    //TODO: check if there is a network connection
    //TODO: When connectivity changes run this service

    public static final String TAG = "ParseFileSaveService";

    public ParseFileSaveService() {
        super(TAG);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ArrayList<String> listRemove = new ArrayList<String>();
        Set<String> storageValues = Prefs.fetchFilesToSaveEventually(this);
        for (String storageValue : storageValues) {

            try {

                final PhotoSave photoSave = new PhotoSave(storageValue);
                final File filePath = photoSave.getFilePath();

                Log.d(TAG, "Parsing file: " + photoSave);
                final ParseFile parseFile = new ParseFile(FileUtils.readFileToByteArray(filePath));

                Log.d(TAG, "Saving parse file: " + photoSave);
                parseFile.save();
                Log.d(TAG, "Saved parse file: " + photoSave);

                final Content content = new Content();

                content.setObjectId(photoSave.getObjectId());
                content.put(photoSave.getKey(), parseFile);
                Log.d(TAG, "Saving content: " + photoSave);
                content.save();
                listRemove.add(storageValue);
                Log.d(TAG, "Saved content: " + photoSave);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        storageValues = Prefs.fetchFilesToSaveEventually(this);
        final boolean removed = storageValues.removeAll(listRemove);

        if (removed) {
            Prefs.saveFilesToSaveEventually(this, storageValues);
        }
    }


}
