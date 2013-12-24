package com.rsxsoftware.insurance.view.bind;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.rsxsoftware.insurance.ParseFileSaveEventuallyService;
import com.rsxsoftware.insurance.prefs.Prefs;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by steve.fiedelberg on 12/23/13.
 */
public class ParseFileExtended extends ParseFile {

    public ParseFileExtended(String s, byte[] byteArray) {
        super(s, byteArray);
    }

    public void saveEventually(Context context, Bind bind, ParseObject object) {

        try {
            final File photosDir = getPhotosDir(context);
            final String fileName = getFileName(object, bind.key());
            final File filePath = new File(photosDir, fileName);
            FileUtils.writeByteArrayToFile(filePath, getData());
            final Set<String> files = Prefs.fetchFilesToSaveEventually(context);

            final String absolutePath = filePath.getAbsolutePath();
            if (!files.contains(absolutePath)) {
                files.add(absolutePath);
                Prefs.saveFilesToSaveEventually(context, files);
            }
            context.startService(new Intent(context, ParseFileSaveEventuallyService.class));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static File getPhotosDir(Context context) {
        return new File(context.getFilesDir(), "Photos");
    }

    public static String getFileName(ParseObject object, String key) {
        return object.getObjectId() + "-" + key + ".jpg";
    }


    public static Pair<String, String> parseFile(String file) {

        final String[] split = file.split("-");

        return new Pair<String, String>(new File(split[0]).getName(), split[1].substring(0, split[1].length()-4));
    }
}
